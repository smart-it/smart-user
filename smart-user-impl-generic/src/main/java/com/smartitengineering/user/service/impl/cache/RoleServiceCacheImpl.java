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
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.filter.RoleFilter;
import com.smartitengineering.user.service.RoleService;
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
public class RoleServiceCacheImpl implements RoleService {

  @Inject
  @Named("primaryService")
  private RoleService primaryService;
  @Inject
  private CacheServiceProvider<Long, Role> cacheProvider;
  private CacheServiceProvider<String, Long> nameCacheProvider;
  private transient final Logger logger = LoggerFactory.getLogger(getClass());
  private final Mutex<Long> mutex = CacheAPIFactory.<Long>getMutex();

  @Override
  public void create(Role role) {
    //Simply delegate
    primaryService.create(role);
  }

  @Override
  public void update(Role role) {
    //First update then delete if update successful!
    try {
      primaryService.update(role);
      expireFromCache(role);
    }
    catch (RuntimeException exception) {
      logger.info("Could not update thus invalidate cache!", exception);
      throw exception;
    }
  }

  @Override
  public void delete(Role role) {
    try {
      primaryService.delete(role);
      expireFromCache(role);
    }
    catch (RuntimeException exception) {
      logger.info("Could not update thus invalidate cache!", exception);
      throw exception;
    }
  }

  @Override
  public Set<Role> getRolesByIds(Long... ids) {
    return getRolesByIds(Arrays.asList(ids));
  }

  @Override
  public Set<Role> getRolesByIds(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return Collections.emptySet();
    }
    Map<Long, Role> results = new HashMap<Long, Role>(ids.size());
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
          logger.warn("Could not acquire lock for role!");
        }
      }
    }
    results.putAll(cacheProvider.retrieveFromCache(missedIds));
    for (Long id : results.keySet()) {
      if (missedIds.remove(id)) {
        mutex.release(locks.get(id));
      }
    }
    Set<Role> fromSource = primaryService.getRolesByIds(missedIds);
    for (Role role : fromSource) {
      putToCache(role);
      results.put(role.getId(), role);
    }
    for (Long id : missedIds) {
      mutex.release(locks.get(id));
    }
    LinkedHashSet<Role> resultSet = new LinkedHashSet<Role>(results.size());
    for (Long id : ids) {
      Role role = results.get(id);
      if (role != null) {
        resultSet.add(role);
      }
    }
    return resultSet;
  }

  @Override
  public Role getRoleByName(String roleName) {
    Long id = nameCacheProvider.retrieveFromCache(getNameCacheKey(roleName));
    if (id != null) {
      Set<Role> set = getRolesByIds(id);
      if (set != null && !set.isEmpty()) {
        return set.iterator().next();
      }
    }
    return primaryService.getRoleByName(roleName);
  }

  @Override
  public Collection<Role> getAllRoles() {
    return primaryService.getAllRoles();
  }

  @Override
  public Collection<Role> search(RoleFilter filter) {
    return primaryService.search(filter);
  }

  @Override
  public void validateRole(Role role) {
    primaryService.validateRole(role);
  }

  private String getNameCacheKey(Role role) {
    final String name = role.getName();
    return getNameCacheKey(name);
  }

  protected String getNameCacheKey(final String name) {
    return new StringBuilder("role:").append(name).toString();
  }

  private void putToCache(Role role) {
    cacheProvider.putToCache(role.getId(), role);
    nameCacheProvider.putToCache(getNameCacheKey(role), role.getId());
  }

  private void expireFromCache(Role role) {
    if (cacheProvider.containsKey(role.getId())) {
      Set<Role> set = getRolesByIds(role.getId());
      if (set != null && !set.isEmpty()) {
        nameCacheProvider.expireFromCache(getNameCacheKey(set.iterator().next()));
      }
      cacheProvider.expireFromCache(role.getId());
    }
  }
}
