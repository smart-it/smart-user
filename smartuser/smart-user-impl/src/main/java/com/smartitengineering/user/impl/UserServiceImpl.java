/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.impl;

import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.filter.UserPersonFilter;
import com.smartitengineering.user.service.UserService;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public class UserServiceImpl implements UserService{
    
    private CommonReadDao<User> userReadDao;
    private CommonWriteDao<User> userWriteDao;
    private CommonReadDao<UserPerson> userPersonReadDao;
    private CommonWriteDao<UserPerson> userPersonWriteDao;
    

    public void create(UserPerson userPerson) {
        try{
            getUserPersonWriteDao().save(userPerson);
        }catch(Exception e){
            
        }
    }

    public void update(User user) {
        try{
            getUserWriteDao().update(user);
        }catch(Exception e){
            
        }
    }

    public void delete(User user) {
        try{
            getUserWriteDao().delete(user);
        }catch(Exception e){
            
        }
    }

    public void update(UserPerson userPerson) {
        try{
            getUserPersonWriteDao().update(userPerson);
        }catch(Exception e){
            
        }
    }

    public void delete(UserPerson userPerson) {
        try{
            getUserPersonWriteDao().delete(userPerson);
        }catch(Exception e){
            
        }
    }

    public Collection<User> search(UserFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<UserPerson> search(UserPersonFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public User getUserByID(Integer id) {
        return getUserReadDao().getById(id);
    }

    public UserPerson getUserPersonByID(Integer id) {
        return getUserPersonReadDao().getById(id);
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

    public void setUserPersonWriteDao(CommonWriteDao<UserPerson> userPersonWriteDao) {
        this.userPersonWriteDao = userPersonWriteDao;
    }

    public CommonReadDao<User> getUserReadDao() {
        return userReadDao;
    }

    public void setUserReadDao(CommonReadDao<User> userReadDao) {
        this.userReadDao = userReadDao;
    }

    public CommonWriteDao<User> getUserWriteDao() {
        return userWriteDao;
    }

    public void setUserWriteDao(CommonWriteDao<User> userWriteDao) {
        this.userWriteDao = userWriteDao;
    }
   
}
