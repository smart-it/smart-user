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
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserGroup;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.observer.ObserverNotification;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.UserGroupService;
import com.smartitengineering.user.service.impl.hbase.domain.AutoId;
import com.smartitengineering.user.service.impl.hbase.domain.KeyableObject;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKey;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKeyIndex;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class UserGroupServiceImpl implements UserGroupService {

  @Inject
  private CommonWriteDao<UserGroup> writeDao;
  @Inject
  private CommonReadDao<UserGroup, Long> readDao;
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
  protected CommonFreeTextSearchDao<UserGroup> freeTextSearchDao;
  @Inject
  private RowCellIncrementor<UserGroup, AutoId, String> idIncrementor;
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
        logger.error("Could not initialize usergroup auto id!", ex);
        throw ex;
      }
    }
    else {
      return true;
    }
  }

  protected void checkAndInitializeAutoId() {
    if (!autoIdInitialized) {
      autoIdInitialized = checkAndInitializeAutoId(KeyableObject.USER_GROUP.name());
    }
  }

  protected UniqueKey getUniqueKeyOfIndexForUserGroup(UserGroup userGroup) {
    final String name = userGroup.getName();
    return getUniqueKeyOfIndexForUserGroupName(name, userGroup.getOrganization().getUniqueShortName());
  }

  protected UniqueKey getUniqueKeyOfIndexForUserGroupName(final String name, final String orgShortName) {
    UniqueKey key = new UniqueKey();
    key.setKey(name);
    key.setObject(KeyableObject.USER_GROUP);
    key.setOrgId(orgShortName);
    return key;
  }

  @Override
  public void save(UserGroup userGroup) {
    checkAndInitializeAutoId();
    validateUserGroup(userGroup);
    final Date date = new Date();
    userGroup.setCreationDate(date);
    userGroup.setLastModifiedDate(date);
    try {
      long nextId = idIncrementor.incrementAndGet(KeyableObject.USER_GROUP.name(), -1l);
      UniqueKey key = getUniqueKeyOfIndexForUserGroup(userGroup);
      UniqueKeyIndex index = new UniqueKeyIndex();
      index.setObjId(String.valueOf(nextId));
      index.setId(key);
      userGroup.setId(nextId);
      uniqueKeyIndexWriteDao.save(index);
      writeDao.save(userGroup);
      observable.notifyObserver(ObserverNotification.CREATE_USER_GROUP, userGroup);
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.USER_GROUP_NAME;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void update(UserGroup userGroup) {
    if (userGroup.getId() == null) {
      throw new IllegalArgumentException("ID of user group not set to be updated!");
    }
    final Date date = new Date();
    userGroup.setLastModifiedDate(date);
    validateUserGroup(userGroup);
    UserGroup oldUserGroup = readDao.getById(userGroup.getId());
    if (oldUserGroup == null) {
      throw new IllegalArgumentException("Trying to update non-existent user group!");
    }
    userGroup.setCreationDate(oldUserGroup.getCreationDate());
    try {
      if (!userGroup.getName().equals(oldUserGroup.getName())) {
        final UniqueKey oldIndexKey = getUniqueKeyOfIndexForUserGroup(oldUserGroup);
        UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(oldIndexKey);
        if (index == null) {
          index = new UniqueKeyIndex();
          index.setId(oldIndexKey);
          index.setObjId(String.valueOf(userGroup.getId()));
        }
        uniqueKeyIndexWriteDao.delete(index);
        index.setId(getUniqueKeyOfIndexForUserGroup(userGroup));
        uniqueKeyIndexWriteDao.save(index);
      }
      if (logger.isInfoEnabled()) {
        Collection<User> users = userGroup.getUsers();
        if (users != null && !users.isEmpty()) {
          for (User user : users) {
            logger.info("$$$ BEFORE UPDATE User " + user.getUsername());
          }
        }
        final Set<Privilege> privileges = userGroup.getPrivileges();
        if (privileges != null) {
          logger.info("$$$ BEFORE UPDATE UserGroup Privileges " + privileges.size());
        }
      }

      writeDao.update(userGroup);
      observable.notifyObserver(ObserverNotification.UPDATE_USER_GROUP, userGroup);
      if (logger.isInfoEnabled()) {
        userGroup = getByOrganizationAndUserGroupName(userGroup.getOrganization().getUniqueShortName(), userGroup.
            getName());
        UserGroup userGroup1 = readDao.getById(oldUserGroup.getId());
        Collection<User> users = userGroup.getUsers();
        logger.info("ID0 " + userGroup.getId() + " ID1 " + userGroup1.getId());
        if (users != null && !users.isEmpty()) {
          for (User user : users) {
            logger.info("$$$ After UPDATE User " + user.getUsername());
          }
        }
        users = userGroup1.getUsers();
        if (users != null && !users.isEmpty()) {
          for (User user : users) {
            logger.info("$$$2 After UPDATE User " + user.getUsername());
          }
        }
        final Set<Privilege> privileges = userGroup.getPrivileges();
        if (privileges != null) {
          logger.info("$$$ AFTER UPDATE UserGroup Privileges " + privileges.size());
        }
      }
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.USER_GROUP_NAME;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void delete(UserGroup userGroup) {
    try {
      observable.notifyObserver(ObserverNotification.DELETE_USER_GROUP, userGroup);
      writeDao.delete(userGroup);
      final UniqueKey indexKey = getUniqueKeyOfIndexForUserGroup(userGroup);
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
  public UserGroup getByOrganizationAndUserGroupName(String organizationShortName, String userGroupName) {
    UniqueKey uniqueKey = getUniqueKeyOfIndexForUserGroupName(userGroupName, organizationShortName);
    UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(uniqueKey);
    if (index != null) {
      long userGroupId = NumberUtils.toLong(index.getObjId(), Long.MIN_VALUE);
      if (userGroupId > Long.MIN_VALUE) {
        return readDao.getById(userGroupId);
      }
    }
    return null;
  }

  @Override
  public Collection<UserGroup> getAllUserGroup() {
    return readDao.getAll();
  }

  @Override
  public Collection<UserGroup> getByOrganizationName(String organizationName) {
    StringBuilder q = new StringBuilder();
    q.append("id: ").append("userGroup\\:").append("*");
    q.append(" AND ").append(" organizationUniqueShortName: ").append(organizationName);
    logger.info(">>>>>QUERY>>>>>>" + q.toString());
    return freeTextSearchDao.search(QueryParameterFactory.getStringLikePropertyParam("q", q.toString()), QueryParameterFactory.
        getOrderByParam("organization", com.smartitengineering.dao.common.queryparam.Order.valueOf("ASC")));

  }

  @Override
  public Collection<UserGroup> getUserGroupsByUser(User user) {
    StringBuilder q = new StringBuilder();
    q.append("id: ").append("userGroup\\:").append("*");
    q.append(" AND ").append(" groupedUserName: ").append(user.getUsername());
    logger.info(">>>>>QUERY>>>>>>" + q.toString());
    return freeTextSearchDao.search(QueryParameterFactory.getStringLikePropertyParam("q", q.toString()), QueryParameterFactory.
        getOrderByParam("userName", com.smartitengineering.dao.common.queryparam.Order.valueOf("ASC")));
  }

  @Override
  public void validateUserGroup(UserGroup userGroup) {
    if (StringUtils.isEmpty(userGroup.getName())) {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.USER_GROUP_NAME.
          name());
    }
    UniqueKey key = getUniqueKeyOfIndexForUserGroup(userGroup);
    UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(key);
    if (index == null) {
      return;
    }
    if (userGroup.getId() != null) {
      if (!String.valueOf(userGroup.getId()).equals(index.getObjId())) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.USER_GROUP_NAME.
            name());
      }
    }
    else {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.USER_GROUP_NAME.
          name());
    }
  }
}
