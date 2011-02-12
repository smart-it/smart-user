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
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.filter.AbstractFilter.Order;
import com.smartitengineering.user.filter.PersonFilter;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.observer.ObserverNotification;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.impl.hbase.domain.AutoId;
import com.smartitengineering.user.service.impl.hbase.domain.KeyableObject;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKey;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKeyIndex;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class PersonServiceImpl implements PersonService {

  @Inject
  private CommonWriteDao<Person> writeDao;
  @Inject
  private CommonReadDao<Person, Long> readDao;
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
  protected CommonFreeTextSearchDao<Person> freeTextSearchDao;
  @Inject
  private RowCellIncrementor<Person, AutoId, String> idIncrementor;
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
        logger.error("Could not initialize person auto id!", ex);
        throw ex;
      }
    }
    else {
      return true;
    }
  }

  protected void checkAndInitializeAutoId() {
    if (!autoIdInitialized) {
      autoIdInitialized = checkAndInitializeAutoId(KeyableObject.PERSON.name());
    }
  }

  protected UniqueKey getUniqueKeyOfIndexForPersonPrimaryEmail(Person person) {
    final String primaryEmail = person.getPrimaryEmail();
    return getUniqueKeyOfIndexForPrimaryEmail(primaryEmail);
  }

  protected UniqueKey getUniqueKeyOfIndexForPrimaryEmail(final String primaryEmail) {
    UniqueKey key = new UniqueKey();
    key.setKey(primaryEmail);
    key.setObject(KeyableObject.PERSON);
    return key;
  }

  @Override
  public void save(Person person) {
    checkAndInitializeAutoId();
    validatePerson(person);
    try {
      long nextId = idIncrementor.incrementAndGet(KeyableObject.PERSON.name(), -1l);
      UniqueKey key = getUniqueKeyOfIndexForPersonPrimaryEmail(person);
      UniqueKeyIndex index = new UniqueKeyIndex();
      index.setObjId(String.valueOf(nextId));
      index.setId(key);
      person.setId(nextId);
      uniqueKeyIndexWriteDao.save(index);
      writeDao.save(person);
      observable.notifyObserver(ObserverNotification.CREATE_PERSON, person);
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.PERSON_EMAIL;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void update(Person person) {
    if(logger.isInfoEnabled()){
      logger.info("Start updating person: " + person.getPrimaryEmail() + " address: " + person.getAddress().toString());
    }
    if (person.getId() == null) {
      throw new IllegalArgumentException("ID of person not set to be updated!");
    }
    validatePerson(person);
    Person oldPerson = readDao.getById(person.getId());
    if (oldPerson == null) {
      throw new IllegalArgumentException("Trying to update non-existent person!");
    }
    try {
      if (!person.getPrimaryEmail().equals(oldPerson.getPrimaryEmail())) {
        final UniqueKey oldIndexKey = getUniqueKeyOfIndexForPersonPrimaryEmail(oldPerson);
        UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(oldIndexKey);
        if (index == null) {
          index = new UniqueKeyIndex();
          index.setId(oldIndexKey);
          index.setObjId(String.valueOf(person.getId()));
        }
        uniqueKeyIndexWriteDao.delete(index);
        index.setId(getUniqueKeyOfIndexForPersonPrimaryEmail(person));
        uniqueKeyIndexWriteDao.save(index);
      }
      writeDao.update(person);
      observable.notifyObserver(ObserverNotification.UPDATE_PERSON, person);
    }
    catch (IllegalArgumentException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.PERSON_EMAIL;
      throw new RuntimeException(message, e);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void delete(Person person) {
    try {
      observable.notifyObserver(ObserverNotification.DELETE_PERSON, person);
      writeDao.delete(person);
      final UniqueKey indexKey = getUniqueKeyOfIndexForPersonPrimaryEmail(person);
      UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(indexKey);
      if (index != null) {
        uniqueKeyIndexWriteDao.delete(index);
      }
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.PERSON;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public Person getById(Long personId) {
    return readDao.getById(personId);
  }

  @Override
  public Collection<Person> search(PersonFilter filter) {
    StringBuilder q = new StringBuilder();
    final String id = filter.getId();
    if (StringUtils.isNotBlank(id)) {
      q.append("id: ").append(" person\\: ").append(ClientUtils.escapeQueryChars(id)).append('*');
    }
    if (StringUtils.isBlank(id)) {
      q.append("id: ").append(" person\\:").append('*');
    }
    final String username = filter.getName().toString();
    if (StringUtils.isNotBlank(username)) {
      q.append(" AND ").append(" name: ").append(username).append('*');
    }
    final String nationalId = filter.getNationalId();
    if (StringUtils.isNotBlank(nationalId)) {
      q.append(" AND ").append(" nationalId: ").append(nationalId).append('*');
    }
    final String sposeName = filter.getSposeNmae();
    if (StringUtils.isNotBlank(sposeName)) {
      q.append(" AND ").append(" sposeName: ").append(sposeName).append('*');
    }
    final String email = filter.getEmail();
    if (StringUtils.isNotBlank(email)) {
      q.append(" AND ").append(" email: ").append(email).append('*');
    }
    final String cellNo = filter.getCellNo();
    if (StringUtils.isNotBlank(cellNo)) {
      q.append(" AND ").append(" cellNo: ").append(cellNo).append('*');
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
  public Collection<Person> getAllPerson() {
    return readDao.getAll();
  }

  @Override
  public Person getPersonByEmail(String email) {
    UniqueKey uniqueKey = getUniqueKeyOfIndexForPrimaryEmail(email);
    UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(uniqueKey);
    if (index != null) {
      long personId = NumberUtils.toLong(index.getObjId(), Long.MIN_VALUE);
      if (personId > Long.MIN_VALUE) {
        return getById(personId);
      }
    }
    return null;
  }

  @Override
  public void validatePerson(Person person) {
    if (StringUtils.isEmpty(person.getPrimaryEmail())) {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.PERSON_EMAIL.
          name());
    }
    UniqueKey key = getUniqueKeyOfIndexForPersonPrimaryEmail(person);
    UniqueKeyIndex index = uniqueKeyIndexReadDao.getById(key);
    if (index == null) {
      return;
    }
    if (person.getId() != null) {
      if (!String.valueOf(person.getId()).equals(index.getObjId())) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.PERSON_EMAIL.
            name());
      }
    }
    else {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.PERSON_EMAIL.
          name());
    }
  }
}
