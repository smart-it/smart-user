/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl;

import com.smartitengineering.dao.common.queryparam.FetchMode;
import com.smartitengineering.dao.common.queryparam.MatchMode;
import com.smartitengineering.dao.common.queryparam.Order;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.dao.impl.hibernate.AbstractCommonDaoImpl;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.filter.UserPersonFilter;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.observer.ObserverNotification;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.UserPersonService;
import com.smartitengineering.user.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
  private CRUDObservable observable;

  public CRUDObservable getObservable() {
    return observable;
  }

  public void setObservable(CRUDObservable observable) {
    this.observable = observable;
  }

  public UserPersonServiceImpl() {
    setEntityClass(UserPerson.class);
  }

  public PersonService getPersonService() {
    return personService;
  }

  public void setPersonService(PersonService personService) {
    this.personService = personService;
  }

  @Override
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
      observable.notifyObserver(ObserverNotification.CREATE_USER_PERSON, userPerson);
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

  @Override
  public void delete(UserPerson userPerson) {
    try {
      observable.notifyObserver(ObserverNotification.DELETE_USER_PERSON, userPerson);
      super.delete(userPerson);
    }
    catch (Exception e) {
    }
  }

  @Override
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

  @Override
  public Collection<UserPerson> getAllUserPerson() {
    Collection<UserPerson> userPersons = new HashSet<UserPerson>();
    try {
      userPersons = super.getAll();
    }
    catch (Exception e) {
    }
    return userPersons;
  }

  @Override
  public Collection<UserPerson> getByOrganization(String organizationUniqueShortName, String userName,
                                                  boolean isSmallerThan, int count) {

    List<QueryParameter> params = new ArrayList<QueryParameter>();
    List<QueryParameter> nestedParams = new ArrayList<QueryParameter>();


    if (StringUtils.isNotBlank(organizationUniqueShortName)) {
      final QueryParameter orgNameParam = QueryParameterFactory.getNestedParametersParam("organization",
                                                                                         FetchMode.LAZY,
                                                                                         QueryParameterFactory.
          getEqualPropertyParam("uniqueShortName", organizationUniqueShortName));
      params.add(orgNameParam);
    }
    else {
      return Collections.emptyList();
    }

    //params.add(QueryParameterFactory.getMaxResultsParam(count));
    //params.add(QueryParameterFactory.getOrderByParam("username", isSmallerThan ? Order.DESC : Order.ASC));
    //params.add(QueryParameterFactory.getDistinctPropProjectionParam("id"));




    if (StringUtils.isNotBlank(userName)) {
      if (isSmallerThan) {
        params.add(QueryParameterFactory.getLesserThanPropertyParam("username", userName));
      }
      else {
        params.add(QueryParameterFactory.getGreaterThanPropertyParam("username", userName));
      }
    }


    params.add(QueryParameterFactory.getMaxResultsParam(count));
    params.add(QueryParameterFactory.getOrderByParam("username", isSmallerThan ? Order.DESC : Order.ASC));
    //params.add(QueryParameterFactory.getDistinctPropProjectionParam("username"));
    List<UserPerson> userPersons = getList(QueryParameterFactory.getNestedParametersParam("user", FetchMode.DEFAULT, params.
        toArray(new QueryParameter[0])));
    if (userPersons != null && !userPersons.isEmpty()) {
      Collections.sort(userPersons, new Comparator<UserPerson>() {

        @Override
        public int compare(UserPerson o1, UserPerson o2) {
          //return o1.getId().compareTo(o2.getId()) * -1;
          return o1.getUser().getUsername().toUpperCase().compareTo(o2.getUser().getUsername().toUpperCase());
        }
      });
      return userPersons;
    }
    else {
      return Collections.emptySet();
    }
  }

  public List<UserPerson> getByUserNames(List<String> userNames) {

    QueryParameter<String> param = QueryParameterFactory.<String>getIsInPropertyParam("username", userNames.toArray(
        new String[0]));

    Collection<UserPerson> result;
    try {
      result = getList(param);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      result = Collections.<UserPerson>emptyList();
    }
    return new ArrayList<UserPerson>(result);

  }

  @Override
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

  @Override
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
    qp = QueryParameterFactory.getNestedParametersParam("user", FetchMode.DEFAULT, QueryParameterFactory.
        getEqualPropertyParam("username", username));
    Collection<UserPerson> userPersons = super.getList(qp);
    if (userPersons != null) {
      for (UserPerson userPerson : userPersons) {
        if (userPerson.getUser().getOrganization().getUniqueShortName().equals(orgName)) {
          return userPerson;
        }
      }
    }
    return null;
  }

  @Override
  public Collection<UserPerson> getAllByOrganization(String organizationUniqueShortName) {
    return super.getList(QueryParameterFactory.getNestedParametersParam("user", FetchMode.DEFAULT, QueryParameterFactory.
        getNestedParametersParam("organization", FetchMode.DEFAULT, QueryParameterFactory.getEqualPropertyParam(
        "uniqueShortName", organizationUniqueShortName))));
  }
}
