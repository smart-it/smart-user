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
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.service.SecuredObjectService;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class SecuredObjectServiceCacheImpl implements SecuredObjectService {

  @Inject
  @Named("primaryService")
  private SecuredObjectService primaryService;
  @Inject
  private CacheServiceProvider<Long, SecuredObject> cacheProvider;
  @Inject
  private CacheServiceProvider<String, Long> nameCacheProvider;
  private transient final Logger logger = LoggerFactory.getLogger(getClass());
  private final Mutex<Long> mutex = CacheAPIFactory.<Long>getMutex();

  @Override
  public void save(SecuredObject securedObject) {
    primaryService.save(securedObject);
  }

  @Override
  public void update(SecuredObject securedObject) {
    //First update then delete if update successful!
    try {
      primaryService.update(securedObject);
      expireFromCache(securedObject);
    }
    catch (RuntimeException exception) {
      logger.info("Could not update thus invalidate cache!", exception);
      throw exception;
    }
  }

  @Override
  public void delete(SecuredObject securedObject) {
    //First update then delete if update successful!
    try {
      primaryService.delete(securedObject);
      expireFromCache(securedObject);
    }
    catch (RuntimeException exception) {
      logger.warn("Could not delete thus invalidate cache!", exception);
      throw exception;
    }
  }

  @Override
  public SecuredObject getById(Long id) {
    //Check cache first
    SecuredObject securedObject = cacheProvider.retrieveFromCache(id);
    if (securedObject != null) {
      return securedObject;
    }
    else {
      try {
        Lock<Long> lock = mutex.acquire(id);
        securedObject = cacheProvider.retrieveFromCache(id);
        if (securedObject != null) {
          return securedObject;
        }
        securedObject = primaryService.getById(id);
        if (securedObject != null) {
          putToCache(securedObject);
        }
        mutex.release(lock);
      }
      catch (Exception ex) {
        logger.warn("Could not do cache lookup!", ex);
      }
      return securedObject;
    }
  }

  @Override
  public SecuredObject getByOrganizationAndObjectID(String organizationName, String objectID) {
    Long id = nameCacheProvider.retrieveFromCache(getObjectIDCacheKey(objectID, organizationName));
    if (id != null) {
      SecuredObject set = getById(id);
      if (set != null) {
        return set;
      }
    }
    return primaryService.getByOrganizationAndObjectID(organizationName, objectID);
  }

  @Override
  public SecuredObject getByOrganizationAndName(String organizationName, String name) {
    Long id = nameCacheProvider.retrieveFromCache(getNameCacheKey(name, organizationName));
    if (id != null) {
      SecuredObject set = getById(id);
      if (set != null) {
        return set;
      }
    }
    return primaryService.getByOrganizationAndName(organizationName, name);
  }

  @Override
  public Collection<SecuredObject> getByOrganization(String organizationName) {
    return primaryService.getByOrganization(organizationName);
  }

  @Override
  public void validateSecuredObject(SecuredObject securedObject) {
    primaryService.validateSecuredObject(securedObject);
  }

  private String getNameCacheKey(SecuredObject securedObject) {
    final String name = securedObject.getName();
    return getNameCacheKey(name, securedObject.getOrganization().getUniqueShortName());
  }

  protected String getNameCacheKey(final String name, final String organizationName) {
    return new StringBuilder("secObjName:").append(name).append(':').append(organizationName).toString();
  }

  private String getObjectIDCacheKey(SecuredObject securedObject) {
    final String objectId = securedObject.getObjectID();
    return getObjectIDCacheKey(objectId, securedObject.getOrganization().getUniqueShortName());
  }

  protected String getObjectIDCacheKey(final String objId, final String organizationName) {
    return new StringBuilder("secObjObjId:").append(objId).append(':').append(organizationName).toString();
  }

  private void putToCache(SecuredObject securedObject) {
    cacheProvider.putToCache(securedObject.getId(), securedObject);
    nameCacheProvider.putToCache(getNameCacheKey(securedObject), securedObject.getId());
    nameCacheProvider.putToCache(getObjectIDCacheKey(securedObject), securedObject.getId());
  }

  private void expireFromCache(SecuredObject secObj) {
    if (cacheProvider.containsKey(secObj.getId())) {
      SecuredObject securedObject = getById(secObj.getId());
      if (securedObject != null) {
        nameCacheProvider.expireFromCache(getNameCacheKey(securedObject));
        nameCacheProvider.expireFromCache(getObjectIDCacheKey(securedObject));
      }
      cacheProvider.expireFromCache(secObj.getId());
    }
  }
}
