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
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.service.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class UserServiceCacheImpl implements UserService {

  @Inject
  @Named("primaryService")
  private UserService primaryService;
  @Inject
  private CacheServiceProvider<Long, User> cacheProvider;
  @Inject
  private CacheServiceProvider<String, Long> nameCacheProvider;
  private transient final Logger logger = LoggerFactory.getLogger(getClass());
  private final Mutex<Long> mutex = CacheAPIFactory.<Long>getMutex();

  @Override
  public void save(User user) {
    //Simply delegate
    primaryService.save(user);
  }

  @Override
  public void update(User user) {
    //First update then delete if update successful!
    try {
      primaryService.delete(user);
      expireFromCache(user);
    }
    catch (RuntimeException exception) {
      logger.info("Could not update thus invalidate cache!", exception);
      throw exception;
    }
  }

  @Override
  public void delete(User user) {
    //First update then delete if update successful!
    try {
      primaryService.update(user);
      expireFromCache(user);
    }
    catch (RuntimeException exception) {
      logger.info("Could not update thus invalidate cache!", exception);
      throw exception;
    }
  }

  @Override
  public User getById(Long id) {
    //Check cache first
    User user = cacheProvider.retrieveFromCache(id);
    if (user != null) {
      return user;
    }
    else {
      try {
        Lock<Long> lock = mutex.acquire(id);
        user = cacheProvider.retrieveFromCache(id);
        if (user != null) {
          return user;
        }
        user = primaryService.getById(id);
        if (user != null) {
          putToCache(user);
        }
        mutex.release(lock);
      }
      catch (Exception ex) {
        logger.warn("Could not do cache lookup!", ex);
      }
      return user;
    }
  }

  @Override
  public Set<User> getUsersByIds(Long... userId) {
    return getUsersByIds(Arrays.asList(userId));
  }

  @Override
  public Set<User> getUsersByIds(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return Collections.emptySet();
    }
    Map<Long, User> results = new HashMap<Long, User>(ids.size());
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
          logger.warn("Could not acquire lock for user!");
        }
      }
    }
    results.putAll(cacheProvider.retrieveFromCache(missedIds));
    for (Long id : results.keySet()) {
      if (missedIds.remove(id)) {
        mutex.release(locks.get(id));
      }
    }
    Set<User> fromSource = primaryService.getUsersByIds(missedIds);
    for (User user : fromSource) {
      putToCache(user);
      results.put(user.getId(), user);
    }
    for (Long id : missedIds) {
      mutex.release(locks.get(id));
    }
    LinkedHashSet<User> resultSet = new LinkedHashSet<User>(results.size());
    for (Long id : ids) {
      User user = results.get(id);
      if (user != null) {
        resultSet.add(user);
      }
    }
    return resultSet;
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
    Long id = nameCacheProvider.retrieveFromCache(getNameCacheKey(userName, organizationShortName));
    if (id != null) {
      User user = getById(id);
      if (user != null) {
        return user;
      }
    }
    return primaryService.getUserByOrganizationAndUserName(organizationShortName, userName);
  }

  @Override
  public Collection<User> search(UserFilter filter) {
    return primaryService.search(filter);
  }

  @Override
  public Collection<User> getAllUser() {
    return primaryService.getAllUser();
  }

  @Override
  public Collection<User> getUsers(String userNameLike, String userName, boolean isSmallerThan, int count) {
    return primaryService.getUsers(userNameLike, userName, isSmallerThan, count);
  }

  @Override
  public Collection<User> getUserByOrganization(String organizationName) {
    return primaryService.getUserByOrganization(organizationName);
  }

  @Override
  public Collection<User> getUserByOrganization(String organizationName, String userName, boolean isSmallerThan,
                                                int count) {
    return primaryService.getUserByOrganization(organizationName, userName, isSmallerThan, count);
  }

  @Override
  public void validateUser(User user) {
    primaryService.validateUser(user);
  }

  private String getNameCacheKey(User user) {
    final String username = user.getUsername();
    final String uniqueShortName = user.getOrganization().getUniqueShortName();
    return getNameCacheKey(username, uniqueShortName);
  }

  protected String getNameCacheKey(final String username, final String uniqueShortName) {
    return new StringBuilder("user:").append(username).append(':').append(uniqueShortName).toString();
  }

  private void putToCache(User user) {
    cacheProvider.putToCache(user.getId(), user);
    nameCacheProvider.putToCache(getNameCacheKey(user), user.getId());
  }

  private void expireFromCache(User user) {
    if (cacheProvider.containsKey(user.getId())) {
      User oldUser = getById(user.getId());
      if (oldUser != null) {
        nameCacheProvider.expireFromCache(getNameCacheKey(oldUser));
      }
      cacheProvider.expireFromCache(oldUser.getId());
    }
  }
}
