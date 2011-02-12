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
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.filter.AbstractFilter.Order;
import com.smartitengineering.user.filter.UserPersonFilter;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.observer.ObserverNotification;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.Services;
import com.smartitengineering.user.service.UserPersonService;
import com.smartitengineering.user.service.UserService;
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
public class UserPersonServiceImpl implements UserPersonService {

  @Inject
  private CommonWriteDao<UserPerson> writeDao;
  @Inject
  private CommonReadDao<UserPerson, Long> readDao;
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
  private RowCellIncrementor<UserPerson, AutoId, String> idIncrementor;
  @Inject
  protected CommonFreeTextSearchDao<UserPerson> freeTextSearchDao;
  @Inject
  private UserService userService;
  @Inject
  private PersonService personService;
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
        logger.error("Could not initialize UserPerson auto id!", ex);
        throw ex;
      }
    }
    else {
      return true;
    }
  }

  protected void checkAndInitializeAutoId() {
    if (!autoIdInitialized) {
      autoIdInitialized = checkAndInitializeAutoId(KeyableObject.USER_PERSON.name());
    }
  }

  protected UniqueKey getUniqueKeyOfIndexForUser(UserPerson userPerson) {
    return getUniqueKeyOfIndexForUser(userPerson.getUser().getId());
  }

  protected UniqueKey getUniqueKeyOfIndexForUser(final Long userId) {
    if (userId == null) {
      return null;
    }
    UniqueKey key = new UniqueKey();
    key.setKey(new StringBuilder("user_").append(userId).toString());
    key.setObject(KeyableObject.USER_PERSON);
    return key;
  }

  protected UniqueKey getUniqueKeyOfIndexForPerson(UserPerson userPerson) {
    return getUniqueKeyOfIndexForPerson(userPerson.getPerson().getId());
  }

  protected UniqueKey getUniqueKeyOfIndexForPerson(final Long personId) {
    if (personId == null) {
      return null;
    }
    UniqueKey key = new UniqueKey();
    key.setKey(new StringBuilder("person_").append(personId).toString());
    key.setObject(KeyableObject.USER_PERSON);
    return key;
  }

  @Override
  public void create(UserPerson userPerson) {
    checkAndInitializeAutoId();
    validateUserPerson(userPerson);
    cascadeSave(userPerson);
    final Date date = new Date();
    userPerson.setCreationDate(date);
    userPerson.setLastModifiedDate(date);
    try {
      long nextId = idIncrementor.incrementAndGet(KeyableObject.USER_PERSON.name(), -1l);
      UniqueKey key = getUniqueKeyOfIndexForUser(userPerson);
      UniqueKeyIndex index = new UniqueKeyIndex();
      index.setObjId(String.valueOf(nextId));
      index.setId(key);
      uniqueKeyIndexWriteDao.save(index);
      key = getUniqueKeyOfIndexForPerson(userPerson);
      index = new UniqueKeyIndex();
      index.setObjId(String.valueOf(nextId));
      index.setId(key);
      uniqueKeyIndexWriteDao.save(index);
      userPerson.setId(nextId);
      writeDao.save(userPerson);
      observable.notifyObserver(ObserverNotification.CREATE_USER_PERSON, userPerson);
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }

  }

  @Override
  public void update(UserPerson userPerson) {
    if (userPerson.getId() == null) {
      throw new IllegalArgumentException("ID of UserPerson not set to be updated!");
    }
    final Date date = new Date();
    userPerson.setLastModifiedDate(date);
    validateUserPerson(userPerson);
    UserPerson oldUserPerson = readDao.getById(userPerson.getId());
    if (oldUserPerson == null) {
      throw new IllegalArgumentException("Trying to update non-existent user person!");
    }
    userPerson.setCreationDate(oldUserPerson.getCreationDate());
    cascadeUpdate(userPerson);
    try {
      if (!userPerson.getUser().getId().equals(oldUserPerson.getUser().getId())) {
        final UniqueKey oldIndexKey = getUniqueKeyOfIndexForUser(oldUserPerson);
        UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(oldIndexKey);
        if (index == null) {
          index = new UniqueKeyIndex();
          index.setId(oldIndexKey);
          index.setObjId(String.valueOf(userPerson.getId()));
        }
        uniqueKeyIndexWriteDao.delete(index);
        index.setId(getUniqueKeyOfIndexForUser(userPerson));
        uniqueKeyIndexWriteDao.save(index);
      }
      if (!userPerson.getPerson().getId().equals(oldUserPerson.getPerson().getId())) {
        final UniqueKey oldIndexKey = getUniqueKeyOfIndexForPerson(oldUserPerson);
        UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(oldIndexKey);
        if (index == null) {
          index = new UniqueKeyIndex();
          index.setId(oldIndexKey);
          index.setObjId(String.valueOf(userPerson.getId()));
        }
        uniqueKeyIndexWriteDao.delete(index);
        index.setId(getUniqueKeyOfIndexForPerson(userPerson));
        uniqueKeyIndexWriteDao.save(index);
      }
      writeDao.update(userPerson);
      observable.notifyObserver(ObserverNotification.UPDATE_USER_PERSON, userPerson);
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }

  }

  @Override
  public void delete(UserPerson userPerson) {
    try {
      observable.notifyObserver(ObserverNotification.DELETE_USER_PERSON, userPerson);
      writeDao.delete(userPerson);
      cascadeDelete(userPerson);
      UniqueKey indexKey = getUniqueKeyOfIndexForUser(userPerson);
      UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(indexKey);
      if (index != null) {
        uniqueKeyIndexWriteDao.delete(index);
      }
      indexKey = getUniqueKeyOfIndexForPerson(userPerson);
      index = uniqueKeyIndexReadDao.getById(indexKey);
      if (index != null) {
        uniqueKeyIndexWriteDao.delete(index);
      }
    }
    catch (Exception e) {
      logger.info(e.getMessage(), e);
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" +
          UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void deleteByPerson(Person person) {
    UniqueKey key = getUniqueKeyOfIndexForPerson(person.getId());
    if (key != null) {
      UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(key);
      if (index != null) {
        long userPersonId = NumberUtils.toLong(index.getObjId(), Long.MIN_VALUE);
        if (userPersonId > Long.MIN_VALUE) {
          delete(readDao.getById(userPersonId));
        }
      }
    }
  }

  @Override
  public void deleteByUser(User user) {
    UserPerson userPerson = getUserPersonForUser(user);
    if (userPerson != null) {
      delete(userPerson);
    }
  }

  protected UserPerson getUserPersonForUser(User user) {
    UserPerson userPerson = null;
    UniqueKey key = getUniqueKeyOfIndexForUser(user.getId());
    if (key != null) {
      UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(key);
      if (index != null) {
        long userPersonId = NumberUtils.toLong(index.getObjId(), Long.MIN_VALUE);
        if (userPersonId > Long.MIN_VALUE) {
          userPerson = readDao.getById(userPersonId);
        }
      }
    }
    return userPerson;
  }

  @Override
  public Collection<UserPerson> getAllUserPerson() {
    return readDao.getAll();
  }

  @Override
  public UserPerson getUserPersonByUsernameAndOrgName(String username, String orgName) {
    User user = userService.getUserByOrganizationAndUserName(orgName, username);
    if (user != null) {
      return getUserPersonForUser(user);
    }
    return null;
  }

  @Override
  public Collection<UserPerson> search(UserPersonFilter filter) {
    StringBuilder q = new StringBuilder();
    final String id = filter.getId();
    if (StringUtils.isNotBlank(id)) {
      q.append("id: ").append("userPerson\\: ").append(ClientUtils.escapeQueryChars(id)).append('*');
    }
    if (StringUtils.isBlank(id)) {
      q.append("id: ").append("userPerson\\:").append('*');
    }
    final String username = filter.getUsername();
    if (StringUtils.isNotBlank(username)) {
      q.append(" AND ").append(" userName: ").append(username).append('*');
    }
    final String orgName = filter.getOrganizationShortName();
    if (StringUtils.isNotBlank(orgName)) {
      q.append(" AND ").append(" organizationUniqueShortName: ").append(orgName).append('*');
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
    logger.info(">>>>>>>>>>>QUERY>>>>>>>>>>" + q.toString());
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
  public Collection<UserPerson> getAllByOrganization(String organizationUniqueShortName) {
    UserPersonFilter userPersonFilter = new UserPersonFilter();
    if (organizationUniqueShortName != null) {
      userPersonFilter.setOrganizationShortName(organizationUniqueShortName);
    }
    return search(userPersonFilter);
  }

  @Override
  public Collection<UserPerson> getByOrganization(String organizationUniqueShortName, String userName,
                                                  boolean isSmallerThan, int count) {
    UserPersonFilter userPersonFilter = new UserPersonFilter();
    logger.info(">>>>>>>>>>>OrgShorName>>>>>>>>>>" + organizationUniqueShortName);
    if (organizationUniqueShortName != null) {
      Organization organization = Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(
          organizationUniqueShortName);
      if (organization != null) {
        userPersonFilter.setOrganizationShortName(organization.getUniqueShortName());
      }
      else {
        logger.info("Organization is null");
      }
    }
    if (count != 0) {
      userPersonFilter.setCount(count);
    }
    userPersonFilter.setCount(count);
    if (userName == null) {
      logger.info("Username is null");
    }
    else {
      logger.info(">>>>>>>>>>>username>>>>>>>>>>" + userName);
      userPersonFilter.setUsername(userName);
    }
    return search(userPersonFilter);
  }

  @Override
  public void validateUserPerson(UserPerson userPerson) {
    userService.validateUser(userPerson.getUser());
    personService.validatePerson(userPerson.getPerson());
    UniqueKey key = getUniqueKeyOfIndexForUser(userPerson);
    if (key != null) {
      UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(key);
      if (index == null) {
        return;
      }
      if (userPerson.getId() != null) {
        if (!String.valueOf(userPerson.getId()).equals(index.getObjId())) {
          throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER.
              name());
        }
      }
      else {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER.
            name());
      }
    }
    key = getUniqueKeyOfIndexForPerson(userPerson);
    if (key != null) {
      UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(key);
      if (index == null) {
        return;
      }
      if (userPerson.getId() != null) {
        if (!String.valueOf(userPerson.getId()).equals(index.getObjId())) {
          throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER.
              name());
        }
      }
      else {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER.
            name());
      }
    }
  }

  private void cascadeSave(UserPerson userPerson) {
    if (userPerson.getUser().getId() == null) {
      userService.save(userPerson.getUser());
    }
    if (userPerson.getPerson().getId() == null) {
      personService.save(userPerson.getPerson());
    }
  }

  private void cascadeDelete(UserPerson userPerson) {
    if (userPerson.getUser().getId() != null) {
      userService.delete(userPerson.getUser());
    }
    if (userPerson.getPerson().getId() != null) {
      personService.delete(userPerson.getPerson());
    }
  }

  private void cascadeUpdate(UserPerson userPerson) {
    userService.update(userPerson.getUser());
    logger.info("Calling person service update");
    personService.update(userPerson.getPerson());
  }
}
