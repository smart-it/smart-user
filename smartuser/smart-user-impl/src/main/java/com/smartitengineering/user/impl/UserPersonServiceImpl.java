/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.impl;

import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.dao.common.queryparam.FetchMode;
import com.smartitengineering.dao.common.queryparam.MatchMode;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
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
public class UserPersonServiceImpl implements UserPersonService {

    private CommonReadDao<UserPerson> userPersonReadDao;
    private CommonWriteDao<UserPerson> userPersonWriteDao;
    private PersonService personService;
    private UserService userService;

    public PersonService getPersonService() {
        return personService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void create(UserPerson userPerson) {
        getUserService().validateUser(userPerson.getUser());
        if (userPerson.getPerson().getId() != null) {
            System.out.println("Checking person");
            System.out.println("Person ID : " + userPerson.getPerson().getId());
            Integer count = (Integer) getUserPersonReadDao().getOther(
                    QueryParameterFactory.getElementCountParam("person.id"),
                    QueryParameterFactory.getEqualPropertyParam(
                    "person.id", userPerson.getPerson().getId()));
            System.out.println(count);
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                        name() + "-" +
                        UniqueConstrainedField.PERSON.name());
            }
        }
        personService.validatePerson(userPerson.getPerson());
        try {
            getUserPersonWriteDao().save(userPerson);
        } catch (ConstraintViolationException e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                    name() + "-" + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        } catch (StaleStateException e) {
            String message =
                    ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name();
            throw new RuntimeException(message, e);
        }
    }

    public void update(UserPerson userPerson) {
        getUserService().validateUser(userPerson.getUser());
        personService.validatePerson(userPerson.getPerson());
        try {
            getUserPersonWriteDao().update(userPerson);
        } catch (ConstraintViolationException e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                    name() + "-" + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        } catch (StaleStateException e) {
            String message =
                    ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name();
            throw new RuntimeException(message, e);
        }
    }

    public void delete(UserPerson userPerson) {
        try {
            getUserPersonWriteDao().delete(userPerson);
        } catch (Exception e) {
        }
    }

    public Collection<UserPerson> search(UserPersonFilter filter) {
        QueryParameter qp;
        List<QueryParameter> queryParameters = new ArrayList<QueryParameter>();
        if (!StringUtils.isEmpty(filter.getUsername())) {
            qp = QueryParameterFactory.getNestedParametersParam("user",
                    FetchMode.DEFAULT,
                    QueryParameterFactory.getStringLikePropertyParam("username",
                    filter.getUsername(), MatchMode.ANYWHERE));
            queryParameters.add(qp);
        }
        Collection<UserPerson> userPersons = new HashSet<UserPerson>();
        if (queryParameters.size() == 0) {
            try {
                userPersons = getUserPersonReadDao().getAll();
            } catch (Exception e) {
            }
        } else {
            try {
                userPersons = getUserPersonReadDao().getList(queryParameters);
            } catch (Exception e) {
            }
        }
        return userPersons;
    }

    public Collection<UserPerson> getAllUserPerson() {
        Collection<UserPerson> userPersons = new HashSet<UserPerson>();
        try {
            userPersons = getUserPersonReadDao().getAll();
        } catch (Exception e) {
        }
        return userPersons;
    }

    public UserPerson getUserPersonByUsername(String username) {
        QueryParameter qp;
        qp =
                QueryParameterFactory.getNestedParametersParam("user",
                FetchMode.DEFAULT,
                QueryParameterFactory.getEqualPropertyParam("username", username));
        UserPerson userPerson = getUserPersonReadDao().getSingle(qp);
        return userPerson;
    }

    public void deleteByPerson(Person person) {
        if(person == null) {
            return;
        }
        QueryParameter qp = QueryParameterFactory.getEqualPropertyParam(
                "person.id", person.getId());
        UserPerson userPerson = getUserPersonReadDao().getSingle(qp);
        if (userPerson != null) {
            delete(userPerson);
        }
    }

    public void deleteByUser(User user) {
        if(user == null) {
            return;
        }
        QueryParameter qp = QueryParameterFactory.getEqualPropertyParam(
                "user.id", user.getId());
        UserPerson userPerson = getUserPersonReadDao().getSingle(qp);
        if (userPerson != null) {
            delete(userPerson);
        }
    }

    public CommonReadDao<UserPerson> getUserPersonReadDao() {
        return userPersonReadDao;
    }

    public void setUserPersonReadDao(CommonReadDao<UserPerson> userPersonReadDao) {
        this.userPersonReadDao = userPersonReadDao;
    }

    public CommonWriteDao<UserPerson> getUserPersonWriteDao() {
        return userPersonWriteDao;
    }

    public void setUserPersonWriteDao(
            CommonWriteDao<UserPerson> userPersonWriteDao) {
        this.userPersonWriteDao = userPersonWriteDao;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}