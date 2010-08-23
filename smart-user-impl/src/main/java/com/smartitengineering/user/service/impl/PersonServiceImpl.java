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
import com.smartitengineering.domain.PersistentDTO;
import com.smartitengineering.user.domain.BasicIdentity;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.filter.PersonFilter;
import com.smartitengineering.user.service.BasicIdentityService;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.PersonService;
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
public class PersonServiceImpl extends AbstractCommonDaoImpl<Person> implements PersonService {

  private BasicIdentityService basicIdentityService;

  public BasicIdentityService getBasicIdentityService() {
    return basicIdentityService;
  }

  public void setBasicIdentityService(BasicIdentityService basicIdentityService) {
    this.basicIdentityService = basicIdentityService;
  }


  @Override
  public void save(Person person) {
    validatePerson(person);
    try {
      super.save(person);
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
  public void update(Person person) {
    validatePerson(person);
    try {
      super.update(person);
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
  public void delete(Person person) {
    try {
      super.delete(person);
    }
    catch (RuntimeException e) {
      String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.PERSON;
      throw new RuntimeException(message, e);
    }
  }

  @Override
  public Collection<Person> search(PersonFilter filter) {
    QueryParameter qp = null;
    List<QueryParameter> queryParameters = new ArrayList<QueryParameter>();
    if (!StringUtils.isEmpty(filter.getEmail())) {
      qp = QueryParameterFactory.getEqualPropertyParam("primaryEmail",
                                                       filter.getEmail());
      queryParameters.add(qp);
    }
    if (!(StringUtils.isEmpty(filter.getName().getFirstName()) &&
          StringUtils.isEmpty(filter.getName().getLastName()) &&
          StringUtils.isEmpty(filter.getName().getMiddleInitial()))) {
      QueryParameter qpConjunction = null;

      if (!StringUtils.isEmpty(filter.getName().getFirstName())) {
        QueryParameter qpFirstName;
        qpFirstName = QueryParameterFactory.getNestedParametersParam(
            "self",
            FetchMode.DEFAULT,
            QueryParameterFactory.getStringLikePropertyParam(
            "name.firstName", filter.getName().getFirstName(),
            MatchMode.ANYWHERE));
        qpConjunction = qpFirstName;
      }
      if (!StringUtils.isEmpty(filter.getName().getLastName())) {
        QueryParameter qpLastName;
        qpLastName = QueryParameterFactory.getNestedParametersParam(
            "self",
            FetchMode.DEFAULT,
            QueryParameterFactory.getStringLikePropertyParam(
            "name.lastName", filter.getName().getLastName()));
        if (qpConjunction != null) {
          qpConjunction = QueryParameterFactory.getConjunctionParam(
              qpConjunction, qpLastName);
        }
        else {
          qpConjunction = qpLastName;
        }

      }
      if (!StringUtils.isEmpty(filter.getName().getMiddleInitial())) {
        QueryParameter qpMiddleInitial;
        qpMiddleInitial = QueryParameterFactory.getNestedParametersParam("self",
                                                                         FetchMode.DEFAULT,
                                                                         QueryParameterFactory.
            getStringLikePropertyParam(
            "name.middleInitial", filter.getName().getLastName()));
        if (qpConjunction != null) {
          qpConjunction = QueryParameterFactory.getConjunctionParam(
              qpConjunction, qpMiddleInitial);
        }
        else {
          qpConjunction = qpMiddleInitial;
        }
      }
      queryParameters.add(qpConjunction);
    }

    Collection<Person> persons = new HashSet<Person>();
    if (queryParameters.isEmpty()) {
      try {
        persons = super.getAll();
      }
      catch (Exception e) {
      }
    }
    else {
      try {
        persons = super.getList(queryParameters);
      }
      catch (Exception e) {
      }
    }
    return persons;
  }

  @Override
  public Collection<Person> getAllPerson() {
    Collection<Person> persons = new HashSet<Person>();
    try {
      persons = super.getAll();
    }
    catch (Exception e) {
    }
    return persons;
  }

  @Override
  public Person getPersonByEmail(String email) {
    Person person = new Person();
    person = super.getSingle(QueryParameterFactory.getEqualPropertyParam("primaryEmail", email));
    return person;
  }

  @Override
  public void validatePerson(Person person) {

    if (person.getId() != null) {
      Integer count = (Integer) super.getOther(QueryParameterFactory.getElementCountParam(
          "primaryEmail"), QueryParameterFactory.getConjunctionParam(
          QueryParameterFactory.getNotEqualPropertyParam("id",
                                                         person.getId()), QueryParameterFactory.
          getStringLikePropertyParam(
          "primaryEmail", person.getPrimaryEmail(), MatchMode.EXACT)));
      if (count.intValue() > 0) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
            UniqueConstrainedField.PERSON_EMAIL.name());
      }
      count = basicIdentityService.count(person.getSelf().getId(), person.getSelf().getNationalID());
      if (count.intValue() > 0) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
            UniqueConstrainedField.PERSON_NATIONAL_ID.name());
      }
      if (person.getSpouse() != null) {
        count = basicIdentityService.count(person.getSpouse().getId(), person.getSpouse().getNationalID());
        if (count.intValue() > 0) {
          throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
              UniqueConstrainedField.PERSON_SPOUSE_NATIONAL_ID.name());
        }
      }
      if (person.getFather() != null) {
        count = basicIdentityService.count(person.getFather().getId(), person.getFather().getNationalID());
        if (count.intValue() > 0) {
          throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
              UniqueConstrainedField.PERSON_FATHER_NATIONAL_ID.name());
        }
      }
      else {
      }
      if (person.getMother() != null) {
        count = basicIdentityService.count(person.getMother().getId(), person.getMother().getNationalID());
        if (count.intValue() > 0) {
          throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
              UniqueConstrainedField.PERSON_MOTHER_NATIONAL_ID.name());
        }
      }
    }
    else {
      Integer count = (Integer) super.getOther(QueryParameterFactory.getElementCountParam(
          "primaryEmail"), QueryParameterFactory.getStringLikePropertyParam(
          "primaryEmail", person.getPrimaryEmail(), MatchMode.EXACT));
      if (count.intValue() > 0) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
            UniqueConstrainedField.PERSON_EMAIL.name());
      }
      count = basicIdentityService.count(person.getSelf().getNationalID());
      if (count.intValue() > 0) {
        throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
            UniqueConstrainedField.PERSON_NATIONAL_ID.name());
      }
      if (person.getSpouse() != null) {
        count = basicIdentityService.count(person.getSpouse().getNationalID());
        if (count.intValue() > 0) {
          throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
              UniqueConstrainedField.PERSON_SPOUSE_NATIONAL_ID.name());
        }
      }
      if (person.getFather() != null) {
        count = basicIdentityService.count(person.getFather().getNationalID());
        if (count.intValue() > 0) {
          throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
              UniqueConstrainedField.PERSON_FATHER_NATIONAL_ID.name());
        }
      }
      if (person.getMother() != null) {
        count = basicIdentityService.count(person.getMother().getNationalID());
        if (count.intValue() > 0) {
          throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" +
              UniqueConstrainedField.PERSON_MOTHER_NATIONAL_ID.name());
        }
      }
    }
  }
}
