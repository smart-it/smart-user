/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase;

import com.google.inject.Inject;
import com.smartitengineering.common.dao.search.CommonFreeTextSearchDao;
import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.dao.impl.hbase.spi.RowCellIncrementor;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.observer.ObserverNotification;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.SecuredObjectService;
import com.smartitengineering.user.service.impl.hbase.domain.AutoId;
import com.smartitengineering.user.service.impl.hbase.domain.KeyableObject;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKey;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKeyIndex;
import java.util.Collection;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class SecuredObjectServiceImpl implements SecuredObjectService {

  @Inject
  private CommonWriteDao<SecuredObject> writeDao;
  @Inject
  private CommonReadDao<SecuredObject, Long> readDao;
  @Inject
  private CommonWriteDao<UniqueKeyIndex> uniqueKeyIndexWriteDao;
  @Inject
  private CommonReadDao<UniqueKeyIndex, UniqueKey> uniqueKeyIndexReadDao;
  @Inject
  private CommonWriteDao<AutoId> autoIdWriteDao;
  @Inject
  private CommonReadDao<AutoId, String> autoIdReadDao;
  @Inject
  private CRUDObservable observable;
  @Inject
  protected CommonFreeTextSearchDao<SecuredObject> freeTextSearchDao;
  @Inject
  private RowCellIncrementor<SecuredObject, AutoId, String> idIncrementor;
  private boolean autoIdInitialized = false;
  protected transient Logger logger = LoggerFactory.getLogger(getClass());

  protected boolean checkAndInitializeAutoId(String autoId) throws RuntimeException {
    AutoId id = autoIdReadDao.getById(autoId);
    if (id == null) {
      id = new AutoId();
      id.setValue(Long.MAX_VALUE);
      id.setId(autoId);
      try {
        autoIdWriteDao.save(id);
        return true;
      }
      catch (RuntimeException ex) {
        logger.error("Could not initialize secured object auto id!", ex);
        throw ex;
      }
    }
    else {
      return true;
    }
  }

  protected void checkAndInitializeAutoId() {
    if (!autoIdInitialized) {
      autoIdInitialized = checkAndInitializeAutoId(KeyableObject.SECURED_OBJECT.name());
    }
  }

  protected UniqueKey getUniqueKeyOfIndexForSecObjName(SecuredObject securedObject) {
    final String name = securedObject.getName();
    return getUniqueKeyOfIndexForName(name, securedObject.getOrganization().getUniqueShortName());
  }

  protected UniqueKey getUniqueKeyOfIndexForName(final String name, final String orgShortName) {
    UniqueKey key = new UniqueKey();
    key.setKey(new StringBuilder("name_").append(name).toString());
    key.setObject(KeyableObject.SECURED_OBJECT);
    key.setOrgId(orgShortName);
    return key;
  }

  protected UniqueKey getUniqueKeyOfIndexForSecObjObjId(SecuredObject securedObject) {
    final String objectId = securedObject.getObjectID();
    return getUniqueKeyOfIndexForObjId(objectId, securedObject.getOrganization().getUniqueShortName());
  }

  protected UniqueKey getUniqueKeyOfIndexForObjId(final String objId, final String orgShortName) {
    UniqueKey key = new UniqueKey();
    key.setKey(new StringBuilder("objId_").append(objId).toString());
    key.setObject(KeyableObject.SECURED_OBJECT);
    key.setOrgId(orgShortName);
    return key;
  }

  @Override
  public void save(SecuredObject securedObject) {
    checkAndInitializeAutoId();
    validateSecuredObject(securedObject);
    final Date date = new Date();
    securedObject.setCreationDate(date);
    securedObject.setLastModifiedDate(date);
    try {
      long nextId = idIncrementor.incrementAndGet(KeyableObject.SECURED_OBJECT.name(), -1l);
      UniqueKey key = getUniqueKeyOfIndexForSecObjName(securedObject);
      UniqueKeyIndex index = new UniqueKeyIndex();
      index.setObjId(String.valueOf(nextId));
      index.setId(key);
      uniqueKeyIndexWriteDao.save(index);
      key = getUniqueKeyOfIndexForSecObjObjId(securedObject);
      index = new UniqueKeyIndex();
      index.setObjId(String.valueOf(nextId));
      index.setId(key);
      uniqueKeyIndexWriteDao.save(index);
      securedObject.setId(nextId);
      writeDao.save(securedObject);
      observable.notifyObserver(ObserverNotification.CREATE_SECURED_OBJECT, securedObject);
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.SECURED_OBJECT_OBJECT_ID;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void update(SecuredObject securedObject) {
    if (securedObject.getId() == null) {
      throw new IllegalArgumentException("ID of SecuredObject not set to be updated!");
    }
    final Date date = new Date();
    securedObject.setLastModifiedDate(date);
    validateSecuredObject(securedObject);
    SecuredObject oldSecuredObject = readDao.getById(securedObject.getId());
    if (oldSecuredObject == null) {
      throw new IllegalArgumentException("Trying to update non-existent secured object!");
    }
    securedObject.setCreationDate(oldSecuredObject.getCreationDate());
    try {
      if (!securedObject.getName().equals(oldSecuredObject.getName())) {
        final UniqueKey oldIndexKey = getUniqueKeyOfIndexForSecObjName(oldSecuredObject);
        UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(oldIndexKey);
        if (index == null) {
          index = new UniqueKeyIndex();
          index.setId(oldIndexKey);
          index.setObjId(String.valueOf(securedObject.getId()));
        }
        uniqueKeyIndexWriteDao.delete(index);
        index.setId(getUniqueKeyOfIndexForSecObjName(securedObject));
        uniqueKeyIndexWriteDao.save(index);
      }
      if (!securedObject.getObjectID().equals(oldSecuredObject.getObjectID())) {
        final UniqueKey oldIndexKey = getUniqueKeyOfIndexForSecObjObjId(oldSecuredObject);
        UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(oldIndexKey);
        if (index == null) {
          index = new UniqueKeyIndex();
          index.setId(oldIndexKey);
          index.setObjId(String.valueOf(securedObject.getId()));
        }
        uniqueKeyIndexWriteDao.delete(index);
        index.setId(getUniqueKeyOfIndexForSecObjObjId(securedObject));
        uniqueKeyIndexWriteDao.save(index);
      }
      writeDao.update(securedObject);
      observable.notifyObserver(ObserverNotification.UPDATE_SECURED_OBJECT, securedObject);
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.SECURED_OBJECT_NAME;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void delete(SecuredObject securedObject) {
    try {
      observable.notifyObserver(ObserverNotification.DELETE_SECURED_OBJECT, securedObject);
      writeDao.delete(securedObject);
      UniqueKey indexKey = getUniqueKeyOfIndexForSecObjName(securedObject);
      UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(indexKey);
      if (index != null) {
        uniqueKeyIndexWriteDao.delete(index);
      }
      indexKey = getUniqueKeyOfIndexForSecObjObjId(securedObject);
      index = uniqueKeyIndexReadDao.getById(indexKey);
      if (index != null) {
        uniqueKeyIndexWriteDao.delete(index);
      }
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" +
          UniqueConstrainedField.SECURED_OBJECT_OBJECT_ID;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public SecuredObject getById(Long id) {
    return readDao.getById(id);
  }

  @Override
  public Collection<SecuredObject> getByOrganization(String organizationName) {
    StringBuilder q = new StringBuilder();
    q.append("id: ").append("securedObject\\:").append("*");
    q.append(" AND ").append(" organizationUniqueShortName: ").append(organizationName).append('*');
    return freeTextSearchDao.search(QueryParameterFactory.getStringLikePropertyParam("q", q.toString()), QueryParameterFactory.
        getOrderByParam("organization", com.smartitengineering.dao.common.queryparam.Order.valueOf("ASC")));
  }

  @Override
  public SecuredObject getByOrganizationAndObjectID(String organizationName, String objectID) {
    UniqueKey uniqueKey = getUniqueKeyOfIndexForObjId(objectID, organizationName);
    UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(uniqueKey);
    if (index != null) {
      long securedObjectId = NumberUtils.toLong(index.getObjId(), Long.MIN_VALUE);
      if (securedObjectId > Long.MIN_VALUE) {
        return getById(securedObjectId);
      }
    }
    return null;
  }

  @Override
  public SecuredObject getByOrganizationAndName(String organizationName, String name) {
    UniqueKey uniqueKey = getUniqueKeyOfIndexForName(name, organizationName);
    UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(uniqueKey);
    if (index != null) {
      long securedObjectId = NumberUtils.toLong(index.getObjId(), Long.MIN_VALUE);
      if (securedObjectId > Long.MIN_VALUE) {
        return getById(securedObjectId);
      }
    }
    return null;
  }

  @Override
  public void validateSecuredObject(SecuredObject securedObject) {
    if (StringUtils.isBlank(securedObject.getObjectID())) {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.SECURED_OBJECT_OBJECT_ID.
          name());
    }
    UniqueKey key = getUniqueKeyOfIndexForSecObjObjId(securedObject);
    UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(key);
    if (index == null) {
      return;
    }
    if (securedObject.getId() != null) {
      if (!String.valueOf(securedObject.getId()).equals(index.getObjId())) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.SECURED_OBJECT_OBJECT_ID.
            name());
      }
    }
    else {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.SECURED_OBJECT_OBJECT_ID.
          name());
    }
    //Check name is unique too
    if (StringUtils.isBlank(securedObject.getName())) {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.SECURED_OBJECT_NAME.
          name());
    }
    key = getUniqueKeyOfIndexForSecObjName(securedObject);
    index = uniqueKeyIndexReadDao.getById(key);
    if (index == null) {
      return;
    }
    if (securedObject.getId() != null) {
      if (!String.valueOf(securedObject.getId()).equals(index.getObjId())) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.SECURED_OBJECT_NAME.
            name());
      }
    }
    else {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.SECURED_OBJECT_NAME.
          name());
    }
  }
}
