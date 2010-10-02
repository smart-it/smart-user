/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl;

import com.smartitengineering.dao.common.queryparam.FetchMode;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.dao.impl.hibernate.AbstractCommonDaoImpl;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.domain.UserGroup;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.UserGroupService;
import java.util.Collection;
import java.util.HashSet;
import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author russel
 */
public class UserGroupServiceImpl extends AbstractCommonDaoImpl<UserGroup> implements UserGroupService {

  public UserGroupServiceImpl() {
    setEntityClass(UserGroup.class);
  }

  @Override
  public void save(UserGroup userGroup) {
    validateUserGroup(userGroup);

    try {
      super.save(userGroup);
    }
    catch (ConstraintViolationException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
    catch (StaleStateException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void update(UserGroup userGroup) {
    validateUserGroup(userGroup);
    try {
      super.update(userGroup);
    }
    catch (ConstraintViolationException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
    catch (StaleStateException e) {
      String message =
             ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" +
          UniqueConstrainedField.OTHER;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public void delete(UserGroup userGroup) {
    try {
      super.delete(userGroup);
    }
    catch (Exception e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.PERSON;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public UserGroup getByOrganizationAndUserGroupName(String organizationShortName, String userGroupName) {

    return super.getSingle(QueryParameterFactory.getStringLikePropertyParam("name", userGroupName),
                           QueryParameterFactory.getNestedParametersParam("organization", FetchMode.DEFAULT,
                                                                          QueryParameterFactory.getEqualPropertyParam(
        "uniqueShortName", organizationShortName)));
  }

  @Override
  public Collection<UserGroup> getByOrganizationName(String organizationShortName) {
    Collection<UserGroup> userGroups = new HashSet<UserGroup>();

    QueryParameter qp = QueryParameterFactory.getNestedParametersParam("organization", FetchMode.DEFAULT, QueryParameterFactory.
        getEqualPropertyParam("uniqueShortName", organizationShortName));
    return super.getList(qp);
  }

  public void validateUserGroup(UserGroup userGroup) {
    if (StringUtils.isEmpty(userGroup.getName())) {
      throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.USER_GROUP_NAME.
          name());
    }
    if (userGroup.getId() == null) {
      Integer count = (Integer) super.getOther(
          QueryParameterFactory.getElementCountParam("name"), QueryParameterFactory.getConjunctionParam(
          QueryParameterFactory.getEqualPropertyParam("organization.id",
                                                      userGroup.getOrganization().getId()), QueryParameterFactory.
          getStringLikePropertyParam(
          "name", userGroup.getName())));
      if (count.intValue() > 0) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
            UniqueConstrainedField.USER_USERNAME.name());
      }
    }
    else {
      Integer count = (Integer) super.getOther(
          QueryParameterFactory.getElementCountParam("name"),
          QueryParameterFactory.getConjunctionParam(
          QueryParameterFactory.getNotEqualPropertyParam("id",
                                                         userGroup.getId()), QueryParameterFactory.getEqualPropertyParam(
          "organization.id",
                                                                                                                         userGroup.
          getOrganization().getId()), QueryParameterFactory.getStringLikePropertyParam(
          "name", userGroup.getName())));
      if (count.intValue() > 0) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
            UniqueConstrainedField.USER_USERNAME.name());
      }

    }
  }

  @Override
  public Collection<UserGroup> getAllUserGroup() {
    return getAll();
  }
}
