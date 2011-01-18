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
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.service.PrivilegeService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class PrivilegeServiceCacheImpl implements PrivilegeService {

  @Inject
  @Named("primaryService")
  private PrivilegeService primaryService;
  @Inject
  private CacheServiceProvider<Long, Privilege> cacheProvider;
  @Inject
  private CacheServiceProvider<String, Long> nameCacheProvider;
  private transient final Logger logger = LoggerFactory.getLogger(getClass());
  private final Mutex<Long> mutex = CacheAPIFactory.<Long>getMutex();

  @Override
  public void create(Privilege privilege) {
    //Simply delegate
    primaryService.create(privilege);
  }

  @Override
  public void delete(Privilege privilege) {
    //First update then delete if update successful!
    try {
      primaryService.delete(privilege);
      expireFromCache(privilege);
    }
    catch (RuntimeException exception) {
      logger.info("Could not update thus invalidate cache!", exception);
      throw exception;
    }
  }

  @Override
  public void update(Privilege privilege) {
    //First update then delete if update successful!
    try {
      primaryService.update(privilege);
      expireFromCache(privilege);
    }
    catch (RuntimeException exception) {
      logger.info("Could not update thus invalidate cache!", exception);
      throw exception;
    }
  }

  @Override
  public Set<Privilege> getPrivilegesByIds(Long... ids) {
    return getPrivilegesByIds(Arrays.asList(ids));
  }

  @Override
  public Set<Privilege> getPrivilegesByIds(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return Collections.emptySet();
    }
    Map<Long, Privilege> results = new HashMap<Long, Privilege>(ids.size());
    List<Long> missedIds = new ArrayList<Long>(ids);
    results.putAll(cacheProvider.retrieveFromCache(missedIds));
    for (Long id : results.keySet()) {
      missedIds.remove(id);
    }
    Map<Long, Lock<Long>> locks = new HashMap<Long, Lock<Long>>(missedIds.size());
    for (Long missedId : missedIds) {
      boolean attained = false;
      while (!attained) {
        try {
          locks.put(missedId, mutex.acquire(missedId));
          attained = true;
        }
        catch (Exception ex) {
          logger.warn("Could not acquire lock for privilege!");
        }
      }
    }
    results.putAll(cacheProvider.retrieveFromCache(missedIds));
    for (Long id : results.keySet()) {
      if (missedIds.remove(id)) {
        mutex.release(locks.get(id));
      }
    }
    Set<Privilege> fromSource = primaryService.getPrivilegesByIds(missedIds);
    for (Privilege privilege : fromSource) {
      putToCache(privilege);
      results.put(privilege.getId(), privilege);
    }
    for (Long id : missedIds) {
      mutex.release(locks.get(id));
    }
    LinkedHashSet<Privilege> resultSet = new LinkedHashSet<Privilege>(results.size());
    for (Long id : ids) {
      Privilege privilege = results.get(id);
      if (privilege != null) {
        resultSet.add(privilege);
      }
    }
    return resultSet;
  }

  @Override
  public Privilege getPrivilegeByOrganizationAndPrivilegeName(String organizationName, String privilegename) {
    Long id = nameCacheProvider.retrieveFromCache(getNameCacheKey(privilegename, organizationName));
    if (id != null) {
      Set<Privilege> set = getPrivilegesByIds(id);
      if (set != null && !set.isEmpty()) {
        return set.iterator().next();
      }
    }
    return primaryService.getPrivilegeByOrganizationAndPrivilegeName(organizationName, privilegename);
  }

  @Override
  public Collection<Privilege> getPrivilegesByOrganizationNameAndObjectID(String organizationName, String objectID) {
    return primaryService.getPrivilegesByOrganizationNameAndObjectID(organizationName, objectID);
  }

  @Override
  public Collection<Privilege> getPrivilegesByOrganizationAndUser(String organizationName, String userName) {
    return primaryService.getPrivilegesByOrganizationAndUser(organizationName, userName);
  }

  @Override
  public Collection<Privilege> getPrivilegesByOrganization(String organization) {
    return primaryService.getPrivilegesByOrganization(organization);
  }

  @Override
  public void validatePrivilege(Privilege privilege) {
    primaryService.validatePrivilege(privilege);
  }

  private String getNameCacheKey(Privilege privilege) {
    final String name = privilege.getName();
    final String uniqueShortName = privilege.getParentOrganization().getUniqueShortName();
    return getNameCacheKey(name, uniqueShortName);
  }

  protected String getNameCacheKey(final String name, final String uniqueShortName) {
    return new StringBuilder("privilege:").append(name).append(':').append(uniqueShortName).toString();
  }

  private void putToCache(Privilege privilege) {
    cacheProvider.putToCache(privilege.getId(), privilege);
    nameCacheProvider.putToCache(getNameCacheKey(privilege), privilege.getId());
  }

  private void expireFromCache(Privilege privilege) {
    if (cacheProvider.containsKey(privilege.getId())) {
      Set<Privilege> set = getPrivilegesByIds(privilege.getId());
      if (set != null && !set.isEmpty()) {
        nameCacheProvider.expireFromCache(getNameCacheKey(set.iterator().next()));
      }
      cacheProvider.expireFromCache(privilege.getId());
    }
  }
}
