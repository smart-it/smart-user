/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.impl;

import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.filter.PersonFilter;
import com.smartitengineering.user.service.PersonService;
import java.util.Collection;

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
