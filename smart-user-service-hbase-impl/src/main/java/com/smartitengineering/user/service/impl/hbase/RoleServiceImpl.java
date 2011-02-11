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
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.filter.RoleFilter;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.observer.ObserverNotification;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.service.impl.hbase.domain.AutoId;
import com.smartitengineering.user.service.impl.hbase.domain.KeyableObject;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKey;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKeyIndex;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class RoleServiceImpl implements RoleService {

  @Inject
  private CommonWriteDao<Role> writeDao;
  @Inject
  private CommonReadDao<Role, Long> readDao;
  @Inject
  private CommonWriteDao<UniqueKeyIndex> uniqueKeyIndexWriteDao;
  @Inject
  private CommonReadDao<UniqueKeyIndex, UniqueKey> uniqueKeyIndexReadDao;
  @Inject
  private CommonWriteDao<AutoId> autoIdWriteDao;
  @Inject
  private CommonReadDao<AutoId, String> autoIdReadDao;
  @Inject
  protected CommonFreeTextSearchDao<Role> freeTextSearchDao;
  @Inject
  private CRUDObservable observable;
  @Inject
  private RowCellIncrementor<Role, AutoId, String> idIncrementor;
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
        logger.error("Could not initialize role auto id!", ex);
        throw ex;
      }
    }
    else {
      return true;
    }
  }

  protected void checkAndInitializeAutoId() {
    if (!autoIdInitialized) {
      autoIdInitialized = checkAndInitializeAutoId(KeyableObject.ROLE.name());
    }
  }

  protected UniqueKey getUniqueKeyOfIndexForRole(Role role) {
    final String name = role.getName();
    return getUniqueKeyOfIndexForRoleName(name);
  }

  protected UniqueKey getUniqueKeyOfIndexForRoleName(final String name) {
    UniqueKey key = new UniqueKey();
    key.setKey(name);
    key.setObject(KeyableObject.ROLE);
    return key;
  }

  @Override
  public void create(Role role) {
    checkAndInitializeAutoId();
    validateRole(role);
    final Date date = new Date();
    role.setCreationDate(date);
    role.setLastModifiedDate(date);
    try {
      long nextId = idIncrementor.incrementAndGet(KeyableObject.ROLE.name(), -1l);
      UniqueKey key = getUniqueKeyOfIndexForRole(role);
      UniqueKeyIndex index = new UniqueKeyIndex();
      index.setObjId(String.valueOf(nextId));
      index.setId(key);
      role.setId(nextId);
      uniqueKeyIndexWriteDao.save(index);
      writeDao.save(role);
      observable.notifyObserver(ObserverNotification.CREATE_ROLE, role);
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.ROLE_NAME;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void update(Role role) {
    if (role.getId() == null) {
      throw new IllegalArgumentException("ID of role not set to be updated!");
    }
    final Date date = new Date();
    role.setLastModifiedDate(date);
    validateRole(role);
    Role oldRole = readDao.getById(role.getId());
    if (oldRole == null) {
      throw new IllegalArgumentException("Trying to update non-existent role!");
    }
    role.setCreationDate(oldRole.getCreationDate());
    try {
      if (!role.getName().equals(oldRole.getName())) {
        final UniqueKey oldIndexKey = getUniqueKeyOfIndexForRole(oldRole);
        UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(oldIndexKey);
        if (index == null) {
          index = new UniqueKeyIndex();
          index.setId(oldIndexKey);
          index.setObjId(String.valueOf(role.getId()));
        }
        uniqueKeyIndexWriteDao.delete(index);
        index.setId(getUniqueKeyOfIndexForRole(role));
        uniqueKeyIndexWriteDao.save(index);
      }
      writeDao.update(role);
      observable.notifyObserver(ObserverNotification.UPDATE_ROLE, role);
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.ROLE_NAME;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void delete(Role role) {
    try {
      observable.notifyObserver(ObserverNotification.DELETE_ROLE, role);
      writeDao.delete(role);
      final UniqueKey indexKey = getUniqueKeyOfIndexForRole(role);
      UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(indexKey);
      if (index != null) {
        uniqueKeyIndexWriteDao.delete(index);
      }
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public Set<Role> getRolesByIds(Long... ids) {
    return getRolesByIds(Arrays.asList(ids));
  }

  @Override
  public Set<Role> getRolesByIds(List<Long> ids) {
    return readDao.getByIds(ids);
  }

  @Override
  public Role getRoleByName(String roleName) {
    UniqueKey uniqueKey = getUniqueKeyOfIndexForRoleName(roleName);
    UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(uniqueKey);
    if (index != null) {
      long roleId = NumberUtils.toLong(index.getObjId(), Long.MIN_VALUE);
      if (roleId > Long.MIN_VALUE) {
        return readDao.getById(roleId);
      }
    }
    return null;
  }

  @Override
  public Collection<Role> getAllRoles() {
    return readDao.getAll();
  }

  @Override
  public Collection<Role> search(RoleFilter filter) {
    StringBuilder q = new StringBuilder();
    q.append("id: ").append("role\\:").append("*");
    final String name = filter.getRoleName();
    if (StringUtils.isNotBlank(name)) {
      q.append(" AND ").append(" name: ").append(ClientUtils.escapeQueryChars(name)).append('*');
    }
    return freeTextSearchDao.search(QueryParameterFactory.getStringLikePropertyParam("q", q.toString()), QueryParameterFactory.
        getOrderByParam("name", com.smartitengineering.dao.common.queryparam.Order.valueOf("ASC")));
  }

  @Override
  public void validateRole(Role role) {
    if (StringUtils.isEmpty(role.getName())) {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.ROLE_NAME.
          name());
    }
    UniqueKey key = getUniqueKeyOfIndexForRole(role);
    UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(key);
    if (index == null) {
      return;
    }
    if (role.getId() != null) {
      if (!String.valueOf(role.getId()).equals(index.getObjId())) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.ROLE_NAME.
            name());
      }
    }
    else {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.ROLE_NAME.
          name());
    }
  }
}
