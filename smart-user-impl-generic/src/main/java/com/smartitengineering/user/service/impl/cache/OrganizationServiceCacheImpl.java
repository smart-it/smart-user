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
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.filter.OrganizationFilter;
import com.smartitengineering.user.service.OrganizationService;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class OrganizationServiceCacheImpl implements OrganizationService {

  @Inject
  @Named("primaryService")
  private OrganizationService primaryService;
  @Inject
  private CacheServiceProvider<String, Organization> orgCacheProvider;
  private transient final Logger logger = LoggerFactory.getLogger(getClass());
  private final Mutex<String> mutex = CacheAPIFactory.<String>getMutex();

  @Override
  public void save(Organization organization) {
    //Simply delegate
    primaryService.save(organization);
  }

  @Override
  public void update(Organization organization) {
    //First update then delete if update successful!
    try {
      primaryService.update(organization);
      orgCacheProvider.expireFromCache(organization.getId());
    }
    catch (RuntimeException exception) {
      logger.info("Could not update thus invalidate cache!", exception);
      throw exception;
    }
  }

  @Override
  public void delete(Organization organization) {
    //First update then delete if update successful!
    try {
      primaryService.delete(organization);
      orgCacheProvider.expireFromCache(organization.getId());
    }
    catch (RuntimeException exception) {
      logger.warn("Could not update thus invalidate cache!", exception);
      throw exception;
    }
  }

  @Override
  public Collection<Organization> search(OrganizationFilter organizationFilter) {
    //No caching
    return primaryService.search(organizationFilter);
  }

  @Override
  public Collection<Organization> getAllOrganization() {
    //No caching
    return primaryService.getAllOrganization();
  }

  @Override
  public Collection<Organization> getOrganizations(String organizationNameLike, String shortName, boolean isSmallerThan,
                                                   int count) {
    //No caching
    return primaryService.getOrganizations(organizationNameLike, shortName, isSmallerThan, count);
  }

  @Override
  public Organization getOrganizationByUniqueShortName(String uniqueShortName) {
    //Check cache first
    Organization organization = orgCacheProvider.retrieveFromCache(uniqueShortName);
    if (organization != null) {
      return organization;
    }
    else {
      try {
        Lock<String> lock = mutex.acquire(uniqueShortName);
        organization = orgCacheProvider.retrieveFromCache(uniqueShortName);
        if (organization != null) {
          return organization;
        }
        organization = primaryService.getOrganizationByUniqueShortName(uniqueShortName);
        if (organization != null) {
          orgCacheProvider.putToCache(uniqueShortName, organization);
        }
        mutex.release(lock);
      }
      catch (Exception ex) {
        logger.warn("Could not do cache lookup!", ex);
      }
      return organization;
    }

  }

  @Override
  public void validateOrganization(Organization organization) {
    //No caching
    primaryService.validateOrganization(organization);
  }
}
