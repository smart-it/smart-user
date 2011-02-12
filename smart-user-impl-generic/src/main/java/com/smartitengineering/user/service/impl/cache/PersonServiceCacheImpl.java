/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.cache;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.smartitengineering.dao.common.cache.CacheServiceProvider;
import com.smartitengineering.dao.common.cache.Lock;
import com.smartitengineering.dao.common.cache.Mutex;
import com.smartitengineering.dao.common.cache.impl.CacheAPIFactory;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.filter.PersonFilter;
import com.smartitengineering.user.service.PersonService;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class PersonServiceCacheImpl implements PersonService {

  @Inject
  @Named("primaryService")
  private PersonService primaryService;
  @Inject
  private CacheServiceProvider<Long, Person> cacheProvider;
  @Inject
  private CacheServiceProvider<String, Long> nameCacheProvider;
  private transient final Logger logger = LoggerFactory.getLogger(getClass());
  private final Mutex<Long> mutex = CacheAPIFactory.<Long>getMutex();

  @Override
  public void save(Person person) {
    //Simply delegate
    primaryService.save(person);
  }

  @Override
  public void update(Person person) {
    //First update then delete if update successful!
    try {
      primaryService.update(person);
      expireFromCache(person);
    }
    catch (RuntimeException exception) {
      logger.info("Could not update thus invalidate cache!", exception);
      throw exception;
    }
  }

  @Override
  public void delete(Person person) {
    //First update then delete if update successful!
    try {
      primaryService.delete(person);
      expireFromCache(person);
    }
    catch (RuntimeException exception) {
      logger.info("Could not update thus invalidate cache!", exception);
      throw exception;
    }
  }

  @Override
  public Person getById(Long id) {
    //Check cache first
    Person person = cacheProvider.retrieveFromCache(id);
    if (person != null) {
      return person;
    }
    else {
      try {
        Lock<Long> lock = mutex.acquire(id);
        person = cacheProvider.retrieveFromCache(id);
        if (person != null) {
          return person;
        }
        person = primaryService.getById(id);
        if (person != null) {
          putToCache(person);
        }
        mutex.release(lock);
      }
      catch (Exception ex) {
        logger.warn("Could not do cache lookup!", ex);
      }
      return person;
    }
  }

  @Override
  public Person getPersonByEmail(String email) {
    Long id = nameCacheProvider.retrieveFromCache(getNameCacheKey(email));
    if (id != null) {
      Person person = getById(id);
      if (person != null) {
        return person;
      }
    }
    return primaryService.getPersonByEmail(email);
  }

  @Override
  public Collection<Person> search(PersonFilter filter) {
    return primaryService.search(filter);
  }

  @Override
  public Collection<Person> getAllPerson() {
    return primaryService.getAllPerson();
  }

  @Override
  public void validatePerson(Person person) {
    primaryService.validatePerson(person);
  }

  private String getNameCacheKey(Person person) {
    final String email = person.getPrimaryEmail();
    return getNameCacheKey(email);
  }

  protected String getNameCacheKey(final String email) {
    return new StringBuilder("person:").append(email).toString();
  }

  private void putToCache(Person person) {
    cacheProvider.putToCache(person.getId(), person);
    nameCacheProvider.putToCache(getNameCacheKey(person), person.getId());
  }

  private void expireFromCache(Person person) {
    if (cacheProvider.containsKey(person.getId())) {
      Person oldPerson = getById(person.getId());
      if (oldPerson != null) {
        nameCacheProvider.expireFromCache(getNameCacheKey(oldPerson));
      }
      cacheProvider.expireFromCache(person.getId());
    }
  }
}
