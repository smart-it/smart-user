/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.impl;

import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.dao.common.queryparam.FetchMode;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.filter.UserPersonFilter;
import com.smartitengineering.user.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author modhu7
 */
public class UserServiceImpl implements UserService {

    private CommonReadDao<User> userReadDao;
    private CommonWriteDao<User> userWriteDao;
    private CommonReadDao<UserPerson> userPersonReadDao;
    private CommonWriteDao<UserPerson> userPersonWriteDao;

    public void create(UserPerson userPerson) {
        try {
            getUserPersonWriteDao().save(userPerson);
        } catch (Exception e) {
        }
    }

    public void update(User user) {
        try {
            getUserWriteDao().update(user);
        } catch (Exception e) {
        }
    }

    public void delete(User user) {
        try {
            getUserWriteDao().delete(user);
        } catch (Exception e) {
        }
    }

    public void update(UserPerson userPerson) {
        try {
            getUserPersonWriteDao().update(userPerson);
        } catch (Exception e) {
        }
    }

    public void delete(UserPerson userPerson) {
        try {
            getUserPersonWriteDao().delete(userPerson);
        } catch (Exception e) {
        }
    }

    public Collection<User> search(UserFilter filter) {
        QueryParameter qp;
        List<QueryParameter> queryParameters = new ArrayList<QueryParameter>();
        if (!StringUtils.isEmpty(filter.getUsername())) {
            qp = QueryParameterFactory.getStringLikePropertyParam("username", filter.getUsername());
            queryParameters.add(qp);
        }
        Collection<User> users = new HashSet<User>();
        if(queryParameters.size()==0){
            try {
                users = getUserReadDao().getAll();
            } catch (Exception e) {
            }
        }else{
            users = getUserReadDao().getList(queryParameters);
        }
        return users;
    }

    public Collection<UserPerson> search(UserPersonFilter filter) {
        QueryParameter qp;
        List<QueryParameter> queryParameters = new ArrayList<QueryParameter>();
        if (!StringUtils.isEmpty(filter.getUsername())) {
            qp = QueryParameterFactory.getNestedParametersParam("User", FetchMode.DEFAULT, 
                    QueryParameterFactory.getStringLikePropertyParam("username", filter.getUsername()));
            queryParameters.add(qp);
        }
        Collection<UserPerson> userPersons = new HashSet<UserPerson>();
        if(queryParameters.size()==0){
            try {
                userPersons = getUserPersonReadDao().getAll();
            } catch (Exception e) {
            }
        }else{
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

    public Collection<User> getAllUser() {
        Collection<User> users = new HashSet<User>();
        try {
            users = getUserReadDao().getAll();
        } catch (Exception e) {
        }
        return users;
    }

    public User getUserByID(String username) {
        QueryParameter qp;
        qp = QueryParameterFactory.getStringLikePropertyParam("username", username);
        User user = getUserReadDao().getSingle(qp);
        return user;
    }

    public UserPerson getUserPersonByID(String username) {
        QueryParameter qp;
        qp = QueryParameterFactory.getNestedParametersParam("User", FetchMode.DEFAULT, 
                    QueryParameterFactory.getStringLikePropertyParam("username", username));
        UserPerson userPerson = getUserPersonReadDao().getSingle(qp);
        return userPerson;
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
