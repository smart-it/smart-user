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
import com.smartitengineering.user.filter.PersonFilter;
import com.smartitengineering.user.service.PersonService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author modhu7
 */
public class PersonServiceImpl implements PersonService {

    private CommonReadDao<Person> personReadDao;
    private CommonWriteDao<Person> personWriteDao;

    public CommonReadDao<Person> getPersonReadDao() {
        return personReadDao;
    }

    public void setPersonReadDao(CommonReadDao<Person> personReadDao) {
        this.personReadDao = personReadDao;
    }

    public CommonWriteDao<Person> getPersonWriteDao() {
        return personWriteDao;
    }

    public void setPersonWriteDao(CommonWriteDao<Person> personWriteDao) {
        this.personWriteDao = personWriteDao;
    }

    public void create(Person person) {
        try {
            getPersonWriteDao().save(person);
        } catch (Exception e) {
        }
    }

    public void update(Person person) {
        try {
            getPersonWriteDao().update(person);
        } catch (Exception e) {
        }
    }

    public void delete(Person person) {
        try {
            getPersonWriteDao().delete(person);
        } catch (Exception e) {
        }
    }

    public Collection<Person> search(PersonFilter filter) {
        QueryParameter qp = null;
        List<QueryParameter> queryParameters = new ArrayList<QueryParameter>();
        if (!StringUtils.isEmpty(filter.getEmail())) {
            System.out.println("Mail");
            qp = QueryParameterFactory.getEqualPropertyParam("primaryEmail", filter.getEmail());
            queryParameters.add(qp);
        }
        if (!(StringUtils.isEmpty(filter.getName().getFirstName()) &&
                StringUtils.isEmpty(filter.getName().getLastName()) &&
                StringUtils.isEmpty(filter.getName().getMiddleInitial()))) {
            System.out.println("Name");
            QueryParameter qpConjunction = null;

            if (!StringUtils.isEmpty(filter.getName().getFirstName())) {
                System.out.println("First Name");
                QueryParameter qpFirstName;
                qpFirstName = QueryParameterFactory.getNestedParametersParam("self", FetchMode.DEFAULT,
                        QueryParameterFactory.getStringLikePropertyParam(
                        "name.firstName", filter.getName().getFirstName(), MatchMode.ANYWHERE));
                qpConjunction = qpFirstName;
                System.out.println(qpConjunction.toString());
            }
            if (!StringUtils.isEmpty(filter.getName().getLastName())) {
                System.out.println("Last Name");
                QueryParameter qpLastName;
                qpLastName = QueryParameterFactory.getNestedParametersParam("self", FetchMode.DEFAULT,
                        QueryParameterFactory.getStringLikePropertyParam(
                        "name.lastName", filter.getName().getLastName()));
                if (qpConjunction != null) {
                    qpConjunction = QueryParameterFactory.getConjunctionParam(qpConjunction, qpLastName);
                } else {
                    qpConjunction = qpLastName;
                }

            }
            if (!StringUtils.isEmpty(filter.getName().getMiddleInitial())) {
                System.out.println("Middle");
                QueryParameter qpMiddleInitial;
                qpMiddleInitial = QueryParameterFactory.getNestedParametersParam("self", FetchMode.DEFAULT,
                        QueryParameterFactory.getStringLikePropertyParam(
                        "name.middleInitial", filter.getName().getLastName()));
                if (qpConjunction != null) {
                    qpConjunction = QueryParameterFactory.getConjunctionParam(qpConjunction, qpMiddleInitial);
                } else {
                    qpConjunction = qpMiddleInitial;
                }
            }
            queryParameters.add(qpConjunction);
        }
        /*
        QueryParameter qpConjunction = null;
        qpConjunction = QueryParameterFactory.getNestedParametersParam("father", FetchMode.DEFAULT,
                QueryParameterFactory.getStringLikePropertyParam(
                "name.firstName", "FFN", MatchMode.ANYWHERE));
        queryParameters.add(qpConjunction);
        qpConjunction = 
                QueryParameterFactory.getStringLikePropertyParam(
                "address.country", "gla", MatchMode.ANYWHERE);
        queryParameters.add(qpConjunction);
         */

        Collection<Person> persons = new HashSet<Person>();
        if (queryParameters.size() == 0) {
            try {
                System.out.println("All");
                persons = getPersonReadDao().getAll();
            } catch (Exception e) {
            }
        } else {
            try {
                System.out.println("Not All");
                System.out.println(queryParameters.size());
                System.out.println(queryParameters.get(0).toString());

                persons = getPersonReadDao().getList(queryParameters);
                System.out.println("SIZE: " + persons.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return persons;
    }

    public Collection<Person> getAllPerson() {
        Collection<Person> persons = new HashSet<Person>();
        try {
            persons = getPersonReadDao().getAll();
        } catch (Exception e) {
        }
        return persons;
    }

    public Person getPersonByEmail(String email) {
        Person person = new Person();
        person = getPersonReadDao().getSingle(QueryParameterFactory.getEqualPropertyParam("primaryEmail", email));
        return person;
    }
}
