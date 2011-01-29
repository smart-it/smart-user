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
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.filter.AbstractFilter.Order;
import com.smartitengineering.user.filter.OrganizationFilter;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.observer.ObserverNotification;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.OrganizationService;
import java.util.Collection;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class OrganizationServiceImpl implements OrganizationService {

  public static final Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);
  @Inject
  private CommonWriteDao<Organization> writeDao;
  @Inject
  private CommonReadDao<Organization, String> readDao;
  @Inject
  private CRUDObservable observable;
  @Inject
  protected CommonFreeTextSearchDao<Organization> freeTextSearchDao;

  @Override
  public void save(Organization organization) {
    validateOrganization(organization);
    final Date date = new Date();
    organization.setCreationDate(date);
    organization.setLastModifiedDate(date);
    try {
      writeDao.save(organization);
      logger.info("notify observer.................");
      observable.notifyObserver(ObserverNotification.CREATE_ORGANIZATION, organization);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void update(Organization organization) {
    final Date date = new Date();
    organization.setLastModifiedDate(date);
    validateOrganization(organization);
    try {
      writeDao.update(organization);
      observable.notifyObserver(ObserverNotification.UPDATE_ORGANIZATION, organization);
    }
    catch (Exception e) {
      String message = ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void delete(Organization organization) {
    try {
      observable.notifyObserver(ObserverNotification.DELETE_ORGNIZATION, organization);
      writeDao.delete(organization);
      logger.info("@@@@@@@@@@@@@Service Impl :: Organization Deleted" + organization.getUniqueShortName());
    }
    catch (Exception e) {
      logger.info(e.getMessage(), e);
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
          UniqueConstrainedField.ORGANIZATION;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public Collection<Organization> getAllOrganization() {
    return readDao.getAll();
  }

  @Override
  public Collection<Organization> search(OrganizationFilter organizationFilter) {
    StringBuilder q = new StringBuilder();
    final String id = organizationFilter.getOrganizationUniqueShortName();
    if (StringUtils.isNotBlank(id)) {
      q.append("id: ").append("org\\: ").append(ClientUtils.escapeQueryChars(id)).append('*');
    }
    if (StringUtils.isBlank(id)) {
      q.append("id: ").append("org\\:").append('*');
    }
    final String name = organizationFilter.getName();
    if (StringUtils.isNotBlank(name)) {
      q.append(" AND ").append(" name: ").append(ClientUtils.escapeQueryChars(id)).append('*');
    }
    if (organizationFilter.getSortBy() == null) {
      organizationFilter.setSortBy("id");
    }
    if (organizationFilter.getSortOrder() == null) {
      organizationFilter.setSortOrder(Order.ASC);
    }
    if (organizationFilter.getCount() == null) {
      logger.info("count is null");
    }
    else {
      logger.info("count is " + organizationFilter.getCount());
    }
    logger.info(">>>>>>>>>>>QUERY>>>>>>>>>>" + q.toString());
    if (organizationFilter.getCount() != null && organizationFilter.getIndex() != null) {

      return freeTextSearchDao.search(QueryParameterFactory.getStringLikePropertyParam("q", q.toString()), QueryParameterFactory.
          getMaxResultsParam(organizationFilter.getCount()), QueryParameterFactory.getFirstResultParam(organizationFilter.
          getIndex() * organizationFilter.getCount()), QueryParameterFactory.getOrderByParam(organizationFilter.
          getSortBy(), com.smartitengineering.dao.common.queryparam.Order.valueOf(
          organizationFilter.getSortOrder().name())));
    }
    else {
      return freeTextSearchDao.search(QueryParameterFactory.getStringLikePropertyParam("q", q.toString()), QueryParameterFactory.
          getOrderByParam(organizationFilter.getSortBy(), com.smartitengineering.dao.common.queryparam.Order.valueOf(organizationFilter.
          getSortOrder().name())));
    }
  }

  @Override
  public Collection<Organization> getOrganizations(String organizationNameLike, String shortName, boolean isSmallerThan,
                                                   int count) {
    OrganizationFilter organizationFilter = new OrganizationFilter();
    organizationFilter.setName(organizationNameLike);
    organizationFilter.setOrganizationUniqueShortName(shortName);
    organizationFilter.setCount(count);
    return search(organizationFilter);
  }

  @Override
  public Organization getOrganizationByUniqueShortName(String uniqueShortName) {
    OrganizationFilter organizationFilter = new OrganizationFilter();
    organizationFilter.setOrganizationUniqueShortName(uniqueShortName);
    Organization organization = readDao.getById(uniqueShortName);
    return organization;
  }

  @Override
  public void validateOrganization(Organization organization) {
    if (StringUtils.isEmpty(organization.getUniqueShortName())) {
      logger.warn("Constriant violation for empty short name! " + organization.getUniqueShortName() + " " + organization.
          getId());
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.ORGANIZATION_UNIQUE_SHORT_NAME.
          name());
    }
    if (organization.getCreationDate() == null) {
      Organization testOrg = readDao.getById(organization.getId());
      if (testOrg != null) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.ORGANIZATION_UNIQUE_SHORT_NAME.
            name());
      }
    }
  }
}
