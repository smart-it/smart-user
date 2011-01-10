/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase;

import com.google.inject.Inject;
import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.dao.impl.hbase.spi.RowCellIncrementor;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.observer.ObserverNotification;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.impl.hbase.domain.AutoId;
import com.smartitengineering.user.service.impl.hbase.domain.KeyableObject;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKey;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKeyIndex;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class PrivilegeServiceImpl implements PrivilegeService {

  @Inject
  private CommonWriteDao<Privilege> writeDao;
  @Inject
  private CommonReadDao<Privilege, Long> readDao;
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
  private RowCellIncrementor<Privilege, AutoId, String> idIncrementor;
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
        logger.error("Could not initialize channel auto id!", ex);
        throw ex;
      }
    }
    else {
      return true;
    }
  }

  protected void checkAndInitializeAutoId() {
    if (!autoIdInitialized) {
      autoIdInitialized = checkAndInitializeAutoId(KeyableObject.PRIVILEGE.name());
    }
  }

  protected UniqueKey getUniqueKeyOfIndexForPrivilege(Privilege privilege) {
    final String name = privilege.getName();
    return getUniqueKeyOfIndexForName(name, privilege.getParentOrganization().getUniqueShortName());
  }

  protected UniqueKey getUniqueKeyOfIndexForName(final String primaryEmail, final String orgShortName) {
    UniqueKey key = new UniqueKey();
    key.setKey(primaryEmail);
    key.setObject(KeyableObject.PRIVILEGE);
    key.setOrgId(orgShortName);
    return key;
  }

  @Override
  public void create(Privilege privilege) {
    checkAndInitializeAutoId();
    validatePrivilege(privilege);
    try {
      long nextId = idIncrementor.incrementAndGet(KeyableObject.PRIVILEGE.name(), -1l);
      UniqueKey key = getUniqueKeyOfIndexForPrivilege(privilege);
      UniqueKeyIndex index = new UniqueKeyIndex();
      index.setObjId(String.valueOf(nextId));
      index.setId(key);
      privilege.setId(nextId);
      uniqueKeyIndexWriteDao.save(index);
      writeDao.save(privilege);
      observable.notifyObserver(ObserverNotification.CREATE_PRIVILEGE, privilege);
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.PRIVILEGE_NAME;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void update(Privilege privilege) {
    if (privilege.getId() == null) {
      throw new IllegalArgumentException("ID of privilege not set to be updated!");
    }
    validatePrivilege(privilege);
    Privilege oldPrivilege = readDao.getById(privilege.getId());
    if (oldPrivilege == null) {
      throw new IllegalArgumentException("Trying to update non-existent privilege!");
    }
    try {
      if (!privilege.getName().equals(oldPrivilege.getName())) {
        final UniqueKey oldIndexKey = getUniqueKeyOfIndexForPrivilege(oldPrivilege);
        UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(oldIndexKey);
        if (index == null) {
          index = new UniqueKeyIndex();
          index.setId(oldIndexKey);
          index.setObjId(String.valueOf(privilege.getId()));
        }
        uniqueKeyIndexWriteDao.delete(index);
        index.setId(getUniqueKeyOfIndexForPrivilege(privilege));
        uniqueKeyIndexWriteDao.save(index);
      }
      writeDao.update(privilege);
      observable.notifyObserver(ObserverNotification.UPDATE_PRIVILEGE, privilege);
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.PRIVILEGE_NAME;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void delete(Privilege privilege) {
    try {
      writeDao.delete(privilege);
      observable.notifyObserver(ObserverNotification.DELETE_PRIVILEGE, privilege);
      final UniqueKey indexKey = getUniqueKeyOfIndexForPrivilege(privilege);
      UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(indexKey);
      if (index != null) {
        uniqueKeyIndexWriteDao.delete(index);
      }
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" +
          UniqueConstrainedField.PRIVILEGE_NAME;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public Set<Privilege> getPrivilegesByIds(Long... ids) {
    return getPrivilegesByIds(Arrays.asList(ids));
  }

  @Override
  public Set<Privilege> getPrivilegesByIds(List<Long> ids) {
    return readDao.getByIds(ids);
  }

  //WRONG API
  @Override
  public Privilege getPrivilegeByName(String name) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Collection<Privilege> getPrivilegesByOrganizationNameAndObjectID(String organizationName, String objectID) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Privilege getPrivilegeByOrganizationAndPrivilegeName(String organizationName, String privilegename) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Collection<Privilege> getPrivilegesByOrganizationAndUser(String organizationName, String userName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Collection<Privilege> getPrivilegesByOrganization(String organization) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void validatePrivilege(Privilege privilege) {
    if (StringUtils.isBlank(privilege.getName())) {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.PRIVILEGE_NAME.
          name());
    }
    UniqueKey key = getUniqueKeyOfIndexForPrivilege(privilege);
    UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(key);
    if (index == null) {
      return;
    }
    if (privilege.getId() != null) {
      if (!String.valueOf(privilege.getId()).equals(index.getObjId())) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.PRIVILEGE_NAME.
            name());
      }
    }
    else {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.PRIVILEGE_NAME.
          name());
    }
  }
}
