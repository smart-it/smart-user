/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.impl;


import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
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
public class PersonServiceImpl implements PersonService{

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
        try{
            getPersonWriteDao().save(person);
        }catch(Exception e){
            
        }
    }

    public void update(Person person) {
        try{
            getPersonWriteDao().update(person);
        }catch(Exception e){
            
        }
    }

    public void delete(Person person) {
        try{
            getPersonWriteDao().delete(person);
        }catch(Exception e){
            
        }
    }

    public Collection<Person> search(PersonFilter filter) {
        QueryParameter qp;
        List<QueryParameter> queryParameters = new ArrayList<QueryParameter>();
        if(!StringUtils.isEmpty(filter.getEmail())){
            qp = QueryParameterFactory.getEqualPropertyParam("email", filter.getEmail());
            queryParameters.add(qp);
        }
        if(filter.getName()!=null){
            QueryParameter qpFirstName;
            qpFirstName = QueryParameterFactory.getStringLikePropertyParam("firstName", filter.getName().getFirstName());
            QueryParameter qpLastname;
            qpLastname = QueryParameterFactory.getStringLikePropertyParam("lastName", filter.getName().getLastName());
            QueryParameter qpMiddleInitial;
            qpMiddleInitial = QueryParameterFactory.getStringLikePropertyParam("middleInitial", filter.getName().getMiddleInitial());
            queryParameters.add(QueryParameterFactory.getConjunctionParam(qpFirstName, qpLastname, qpMiddleInitial));
        }
        Collection<Person> persons = new HashSet<Person>();
        if(queryParameters.size()==0){
            try {
                persons = getPersonReadDao().getAll();
            } catch (Exception e) {
            }
        }else{
            try {
                persons = getPersonReadDao().getList(queryParameters);
            } catch (Exception e) {
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
