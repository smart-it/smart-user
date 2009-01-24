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
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
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
    private CommonReadDao<Role> roleReadDao;
    private CommonWriteDao<Role> roleWriteDao;
    private CommonReadDao<Privilege> privilegeReadDao;
    private CommonWriteDao<Privilege> privilegeWriteDao;

    public void create(UserPerson userPerson) {
        try {
            getUserPersonWriteDao().save(userPerson);
        } catch (RuntimeException e) {
            throw e;
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
        if (queryParameters.size() == 0) {
            try {
                users = getUserReadDao().getAll();
            } catch (Exception e) {
            }
        } else {
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

    public Collection<User> getAllUser() {
        Collection<User> users = new HashSet<User>();
        try {
            users = getUserReadDao().getAll();
        } catch (Exception e) {
        }
        return users;
    }

    public User getUserByUsername(String username) {
        QueryParameter qp;
        qp = QueryParameterFactory.getEqualPropertyParam("username", username);
        User user = getUserReadDao().getSingle(qp);
        return user;
    }

    public UserPerson getUserPersonByUsername(String username) {
        QueryParameter qp;
        qp = QueryParameterFactory.getNestedParametersParam("user", FetchMode.DEFAULT,
                QueryParameterFactory.getEqualPropertyParam("username", username));
        UserPerson userPerson = getUserPersonReadDao().getSingle(qp);
        return userPerson;
    }
    
    
    //Role Services
    
    public void create(Role role) {
        try {
            getRoleWriteDao().save(role);
        } catch (Exception e) {
        }
    }

    public void update(Role role) {
        try {
            getRoleWriteDao().update(role);
        } catch (Exception e) {
        }
    }

    public void delete(Role role) {
        try {
            getRoleWriteDao().delete(role);
        } catch (Exception e) {
        }
    }

    public Role getRoleByName(String name) {
        QueryParameter qp;
        qp = QueryParameterFactory.getEqualPropertyParam("name", name);
        Role role = new Role();
        try {
            role = getRoleReadDao().getSingle(qp);
        } catch (Exception e) {
        }
        return role;
    }

    //Privilege services
    
    public void create(Privilege privilege) {
        try {
            System.out.println(privilege.getDisplayName());
            getPrivilegeWriteDao().save(privilege);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Privilege privilege) {
        try {
            getPrivilegeWriteDao().update(privilege);
        } catch (Exception e) {
        }
    }

    public void delete(Privilege privilege) {
        try {
            getPrivilegeWriteDao().delete(privilege);
        } catch (Exception e) {
        }
    }

    public Privilege getPrivilegeByName(String name) {
        QueryParameter qp;
        System.out.println("Server " + name);        
        Privilege privilege = new Privilege();
        try {
            privilege = getPrivilegeReadDao().getSingle(
                    QueryParameterFactory.getEqualPropertyParam("name", name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privilege;
    }
    
    
    //Getter and Setter Methods

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

    public CommonReadDao<Privilege> getPrivilegeReadDao() {
        return privilegeReadDao;
    }

    public void setPrivilegeReadDao(CommonReadDao<Privilege> privilegeReadDao) {
        this.privilegeReadDao = privilegeReadDao;
    }

    public CommonWriteDao<Privilege> getPrivilegeWriteDao() {
        return privilegeWriteDao;
    }

    public void setPrivilegeWriteDao(CommonWriteDao<Privilege> privilegeWriteDao) {
        this.privilegeWriteDao = privilegeWriteDao;
    }

    public CommonReadDao<Role> getRoleReadDao() {
        return roleReadDao;
    }

    public void setRoleReadDao(CommonReadDao<Role> roleReadDao) {
        this.roleReadDao = roleReadDao;
    }

    public CommonWriteDao<Role> getRoleWriteDao() {
        return roleWriteDao;
    }

    public void setRoleWriteDao(CommonWriteDao<Role> roleWriteDao) {
        this.roleWriteDao = roleWriteDao;
    }
}
