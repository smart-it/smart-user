/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl;

import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.dao.common.queryparam.FetchMode;
import com.smartitengineering.dao.common.queryparam.MatchMode;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.dao.impl.hibernate.AbstractCommonDaoImpl;
import com.smartitengineering.dao.impl.hibernate.AbstractDAO;
import com.smartitengineering.domain.PersistentDTO;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.filter.UserPersonFilter;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.UserPersonService;
import com.smartitengineering.user.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author modhu7
 */
public class UserPersonServiceImpl extends AbstractCommonDaoImpl<UserPerson> implements UserPersonService {

  private PersonService personService;
  private UserService userService;

  public UserPersonServiceImpl() {
    setEntityClass(UserPerson.class);
  }

  public PersonService getPersonService() {
    return personService;
  }

  public void setPersonService(PersonService personService) {
    this.personService = personService;
  }

  public void create(UserPerson userPerson) {
    getUserService().validateUser(userPerson.getUser());
    if (userPerson.getPerson().getId() != null) {
      Integer count = (Integer) super.getOther(
          QueryParameterFactory.getElementCountParam("person.id"),
          QueryParameterFactory.getEqualPropertyParam(
          "person.id", userPerson.getPerson().getId()));
      if (count.intValue() > 0) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
            UniqueConstrainedField.PERSON.name());
      }
    }
    personService.validatePerson(userPerson.getPerson());
    try {
      super.save(userPerson);
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

  public void update(UserPerson userPerson) {
    getUserService().validateUser(userPerson.getUser());
    personService.validatePerson(userPerson.getPerson());
    try {
      super.update(userPerson);
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

  public void delete(UserPerson userPerson) {
    try {
      super.delete(userPerson);
    }
    catch (Exception e) {
    }
  }

  public Collection<UserPerson> search(UserPersonFilter filter) {
    QueryParameter qp;
    List<QueryParameter> queryParameters = new ArrayList<QueryParameter>();
    if (!StringUtils.isEmpty(filter.getUsername())) {
      qp = QueryParameterFactory.getNestedParametersParam("user",
                                                          FetchMode.DEFAULT,
                                                          QueryParameterFactory.getStringLikePropertyParam("username",
                                                                                                           filter.
          getUsername(), MatchMode.ANYWHERE));
      queryParameters.add(qp);
    }
    Collection<UserPerson> userPersons = new HashSet<UserPerson>();
    if (queryParameters.size() == 0) {
      try {
        userPersons = super.getAll();
      }
      catch (Exception e) {
      }
    }
    else {
      try {
        userPersons = super.getList(queryParameters);
      }
      catch (Exception e) {
      }
    }
    return userPersons;
  }

  public Collection<UserPerson> getAllUserPerson() {
    Collection<UserPerson> userPersons = new HashSet<UserPerson>();
    try {
      userPersons = super.getAll();
    }
    catch (Exception e) {
    }
    return userPersons;
  }

  public void deleteByPerson(Person person) {
    if (person == null) {
      return;
    }
    QueryParameter qp = QueryParameterFactory.getEqualPropertyParam(
        "person.id", person.getId());
    UserPerson userPerson = super.getSingle(qp);
    if (userPerson != null) {
      delete(userPerson);
    }
  }

  public void deleteByUser(User user) {
    if (user == null) {
      return;
    }
    QueryParameter qp = QueryParameterFactory.getEqualPropertyParam(
        "user.id", user.getId());
    UserPerson userPerson = super.getSingle(qp);
    if (userPerson != null) {
      delete(userPerson);
    }
  }
  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserPerson getUserPersonByUsernameAndOrgName(String username, String orgName) {
    QueryParameter qp;
    qp = QueryParameterFactory.getNestedParametersParam("user", FetchMode.DEFAULT, QueryParameterFactory.getEqualPropertyParam("username", username));        
    Collection<UserPerson> userPersons = super.getList(qp);    
    if (userPersons!=null) {
      for (UserPerson userPerson : userPersons) {
        if (userPerson.getUser().getOrganization().getUniqueShortName().equals(orgName)) {
          return userPerson;
        }
      }
    }    
    return null;
  }
}
