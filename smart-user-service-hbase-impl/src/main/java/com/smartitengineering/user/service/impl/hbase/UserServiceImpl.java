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
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.filter.AbstractFilter.Order;
import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.observer.ObserverNotification;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.service.impl.hbase.domain.AutoId;
import com.smartitengineering.user.service.impl.hbase.domain.KeyableObject;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKey;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKeyIndex;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class UserServiceImpl implements UserService {

  @Inject
  private CommonWriteDao<User> writeDao;
  @Inject
  private CommonReadDao<User, Long> readDao;
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
  private RowCellIncrementor<User, AutoId, String> idIncrementor;
  @Inject
  protected CommonFreeTextSearchDao<User> freeTextSearchDao;
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
        logger.error("Could not initialize user auto id!", ex);
        throw ex;
      }
    }
    else {
      return true;
    }
  }

  protected void checkAndInitializeAutoId() {
    if (!autoIdInitialized) {
      autoIdInitialized = checkAndInitializeAutoId(KeyableObject.USER.name());
    }
  }

  protected UniqueKey getUniqueKeyOfIndexForUser(User user) {
    final String username = user.getUsername();
    return getUniqueKeyOfIndexForUserName(username, user.getOrganization().getUniqueShortName());
  }

  protected UniqueKey getUniqueKeyOfIndexForUserName(final String name, final String orgShortName) {
    UniqueKey key = new UniqueKey();
    key.setKey(name);
    key.setObject(KeyableObject.USER);
    key.setOrgId(orgShortName);
    return key;
  }

  @Override
  public void save(User user) {
    checkAndInitializeAutoId();
    validateUser(user);
    final Date date = new Date();
    user.setCreationDate(date);
    user.setLastModifiedDate(date);
    try {
      long nextId = idIncrementor.incrementAndGet(KeyableObject.USER.name(), -1l);
      UniqueKey key = getUniqueKeyOfIndexForUser(user);
      UniqueKeyIndex index = new UniqueKeyIndex();
      index.setObjId(String.valueOf(nextId));
      index.setId(key);
      user.setId(nextId);
      uniqueKeyIndexWriteDao.save(index);
      writeDao.save(user);
      observable.notifyObserver(ObserverNotification.CREATE_USER, user);
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.USER_USERNAME;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void update(User user) {
    if(logger.isInfoEnabled()){
      logger.info("Start updating user: " + user.getUsername());
    }
    if (user.getId() == null) {
      throw new IllegalArgumentException("ID of user not set to be updated!");
    }
//    user.setRoles(getById(user.getId()).getRoles());
//    user.setPrivileges(getById(user.getId()).getPrivileges());
    final Date date = new Date();
    user.setLastModifiedDate(date);
    validateUser(user);
    User oldUser = readDao.getById(user.getId());
    if (oldUser == null) {
      throw new IllegalArgumentException("Trying to update non-existent user!");
    }
    user.setCreationDate(oldUser.getCreationDate());
    try {
      if (!user.getUsername().equals(oldUser.getUsername())) {
        final UniqueKey oldIndexKey = getUniqueKeyOfIndexForUser(oldUser);
        UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(oldIndexKey);
        if (index == null) {
          index = new UniqueKeyIndex();
          index.setId(oldIndexKey);
          index.setObjId(String.valueOf(user.getId()));
        }
        uniqueKeyIndexWriteDao.delete(index);
        index.setId(getUniqueKeyOfIndexForUser(user));
        uniqueKeyIndexWriteDao.save(index);
      }
      writeDao.update(user);
      observable.notifyObserver(ObserverNotification.UPDATE_USER, user);
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.USER_USERNAME;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void delete(User user) {
    try {
      observable.notifyObserver(ObserverNotification.DELETE_USER, user);
      writeDao.delete(user);
      final UniqueKey indexKey = getUniqueKeyOfIndexForUser(user);
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
  public User getById(Long userId) {
    return readDao.getById(userId);
  }

  @Override
  public Set<User> getUsersByIds(Long... userId) {
    return getUsersByIds(Arrays.asList(userId));
  }

  @Override
  public Set<User> getUsersByIds(List<Long> userId) {
    return readDao.getByIds(userId);
  }

  @Override
  public Collection<User> getAllUser() {
    return readDao.getAll();
  }

  @Override
  public User getUserByUsername(String usernameWithOrganizationName) {
    String username;
    String organizationName;
    StringTokenizer tokenizer = new StringTokenizer(usernameWithOrganizationName, "@");
    if (tokenizer.hasMoreTokens()) {
      username = tokenizer.nextToken();
    }
    else {
      username = "";
    }
    if (tokenizer.hasMoreTokens()) {
      organizationName = tokenizer.nextToken();
    }
    else {
      organizationName = "";
    }
    User user = getUserByOrganizationAndUserName(organizationName, username);
    return user;
  }

  @Override
  public User getUserByOrganizationAndUserName(String organizationShortName, String userName) {
    UniqueKey uniqueKey = getUniqueKeyOfIndexForUserName(userName, organizationShortName);
    UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(uniqueKey);
    if (index != null) {
      long userId = NumberUtils.toLong(index.getObjId(), Long.MIN_VALUE);
      if (userId > Long.MIN_VALUE) {
        return readDao.getById(userId);
      }
    }
    return null;

  }

  @Override
  public Collection<User> getUsers(String userNameLike, String userName, boolean isSmallerThan, int count) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Collection<User> search(UserFilter filter) {
    StringBuilder q = new StringBuilder();
    final String id = filter.getId();
    if (StringUtils.isNotBlank(id)) {
      q.append("id: ").append(ClientUtils.escapeQueryChars(id)).append('*');
    }
    if (StringUtils.isBlank(id)) {
      q.append("id: ").append("user\\:").append('*');
    }
    final String username = filter.getUserName();
    if (StringUtils.isNotBlank(username)) {
      q.append(" AND ").append(" userName: ").append(username).append('*');
    }
    final String orgName = filter.getOrganizationName();
    if (StringUtils.isNotBlank(orgName)) {
      q.append(" AND ").append(" organization: ").append(orgName);
    }
    if (filter.getSortBy() == null) {
      filter.setSortBy("id");
    }
    if (filter.getSortOrder() == null) {
      filter.setSortOrder(Order.ASC);
    }
    if (filter.getCount() == null) {
      logger.info("count is null");
    }
    else {
      logger.info("count is " + filter.getCount());
    }
    logger.info(">>>>>>>>>>>QUERY>>>>>>>>>>"+q.toString());
    if (filter.getCount() != null && filter.getIndex() != null) {

      return freeTextSearchDao.search(QueryParameterFactory.getStringLikePropertyParam("q", q.toString()), QueryParameterFactory.
          getMaxResultsParam(filter.getCount()), QueryParameterFactory.getFirstResultParam(filter.getIndex() * filter.
          getCount()), QueryParameterFactory.getOrderByParam(filter.getSortBy(), com.smartitengineering.dao.common.queryparam.Order.
          valueOf(filter.getSortOrder().name())));
    }
    else {
      return freeTextSearchDao.search(QueryParameterFactory.getStringLikePropertyParam("q", q.toString()), QueryParameterFactory.
          getOrderByParam(filter.getSortBy(), com.smartitengineering.dao.common.queryparam.Order.valueOf(filter.
          getSortOrder().name())));
    }
  }

  @Override
  public Collection<User> getUserByOrganization(String organizationName) {
    UserFilter userFilter = new UserFilter();
    userFilter.setOrganizationName(organizationName);
    return search(userFilter);
  }

  @Override
  public Collection<User> getUserByOrganization(String organizationName, String userName, boolean isSmallerThan,
                                                int count) {
    UserFilter userFilter = new UserFilter();
    userFilter.setOrganizationName(organizationName);
    userFilter.setUserName(userName);
    userFilter.setCount(count);
    return search(userFilter);
  }

  @Override
  public void validateUser(User user) {
    if (StringUtils.isEmpty(user.getUsername())) {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.USER_USERNAME.
          name());
    }
    UniqueKey key = getUniqueKeyOfIndexForUser(user);
    UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(key);
    if (index == null) {
      return;
    }
    if (user.getId() != null) {
      if (!String.valueOf(user.getId()).equals(index.getObjId())) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.USER_USERNAME.
            name());
      }
    }
    else {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.USER_USERNAME.
          name());
    }
  }
}
