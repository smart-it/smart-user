/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase;

import com.google.inject.Inject;
import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.filter.OrganizationFilter;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.observer.ObserverNotification;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.OrganizationService;
import java.util.Collection;
import java.util.Date;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
public class OrganizationServiceImpl implements OrganizationService {

  @Inject
  private CommonWriteDao<Organization> writeDao;
  @Inject
  private CommonReadDao<Organization, String> readDao;
  @Inject
  private CRUDObservable observable;

  @Override
  public void save(Organization organization) {
    validateOrganization(organization);
    final Date date = new Date();
    organization.setCreationDate(date);
    organization.setLastModifiedDate(date);
    try {
      writeDao.save(organization);
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
    }
    catch (Exception e) {
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
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Collection<Organization> getOrganizations(String organizationNameLike, String shortName, boolean isSmallerThan,
                                                   int count) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Organization getOrganizationByUniqueShortName(String uniqueShortName) {
    return readDao.getById(uniqueShortName);
  }

  @Override
  public void validateOrganization(Organization organization) {
    if (StringUtils.isEmpty(organization.getUniqueShortName())) {
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
