/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.impl;

import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.dao.common.queryparam.MatchMode;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.RoleService;
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
public class UserServiceImpl implements UserService, RoleService, PrivilegeService {

    private CommonReadDao<User> userReadDao;
    private CommonWriteDao<User> userWriteDao;
    private CommonReadDao<Role> roleReadDao;
    private CommonWriteDao<Role> roleWriteDao;
    private CommonReadDao<Privilege> privilegeReadDao;
    private CommonWriteDao<Privilege> privilegeWriteDao;
    private PersonService personService;

    public PersonService getPersonService() {
        return personService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void update(User user) {
        validateUser(user);
        try {
            getUserWriteDao().update(user);
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

    public void delete(User user) {
        try {
            getUserWriteDao().delete(user);
        } catch (Exception e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                    name() + "-" + UniqueConstrainedField.PERSON;
            throw new RuntimeException(message, e);
        }
    }

    public Collection<User> search(UserFilter filter) {
        QueryParameter qp;
        List<QueryParameter> queryParameters = new ArrayList<QueryParameter>();
        if (!StringUtils.isEmpty(filter.getUsername())) {
            qp = QueryParameterFactory.getEqualPropertyParam("username",
                    filter.getUsername());
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

    //Role Services
    public void create(Role role) {
        validateRole(role);
        try {
            getRoleWriteDao().save(role);
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

    public void update(Role role) {
        validateRole(role);
        try {
            getRoleWriteDao().update(role);
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

    public Collection<Role> getRolesByName(String name) {
        System.out.println(name);
        QueryParameter qp;
        qp = QueryParameterFactory.getStringLikePropertyParam("name", name,
                MatchMode.ANYWHERE);
        Collection<Role> roles = new HashSet<Role>();
        try {
            roles = getRoleReadDao().getList(qp);
        } catch (Exception e) {
        }
        return roles;
    }

    //Privilege services
    public void create(Privilege privilege) {
        validatePrivilege(privilege);        
        try {          
            getPrivilegeWriteDao().save(privilege);
        }catch (ConstraintViolationException e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                    name() + "-" + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        } catch (StaleStateException e) {
            String message =
                    ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name();
            throw new RuntimeException(message, e);
        }
    }

    public void update(Privilege privilege) {
        validatePrivilege(privilege);
        try {
            getPrivilegeWriteDao().update(privilege);
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

    public void delete(Privilege privilege) {
        try {
            getPrivilegeWriteDao().delete(privilege);
        } catch (Exception e) {
        }
    }

    public Privilege getPrivilegeByName(String name) {
        QueryParameter qp;
        Privilege privilege = new Privilege();
        try {
            privilege = getPrivilegeReadDao().getSingle(
                    QueryParameterFactory.getEqualPropertyParam("name", name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privilege;
    }

    public Collection<Privilege> getPrivilegesByName(String name) {
        QueryParameter qp;
        Collection<Privilege> privileges = new HashSet<Privilege>();
        try {
            privileges = getPrivilegeReadDao().getList(
                    QueryParameterFactory.getStringLikePropertyParam("name",
                    name, MatchMode.ANYWHERE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privileges;
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

    private void validatePrivilege(Privilege privilege) {
        System.out.println("Privilege validation : " + privilege.getName());
        if (privilege.getId() == null) {
            Integer count = (Integer) getPrivilegeReadDao().getOther(
                    QueryParameterFactory.getElementCountParam("name"), 
                    QueryParameterFactory.getStringLikePropertyParam("name",
                    privilege.getName(), MatchMode.EXACT));
            System.out.println(count);
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                        name() + "-" +
                        UniqueConstrainedField.PRIVILEGE_NAME.name());
            }
        } else {
            Integer count = (Integer) getPrivilegeReadDao().getOther(
                    QueryParameterFactory.getElementCountParam("name"),
                    QueryParameterFactory.getConjunctionParam(
                    QueryParameterFactory.getNotEqualPropertyParam("id",
                    privilege.getId()), QueryParameterFactory.
                    getStringLikePropertyParam(
                    "name", privilege.getName(), MatchMode.EXACT)));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                        name() + "-" +
                        UniqueConstrainedField.PRIVILEGE_NAME.name());
            }

        }
    }

    private void validateRole(Role role) {
        if (role.getId() == null) {
            Integer count = (Integer) getRoleReadDao().getOther(
                    QueryParameterFactory.getElementCountParam("name"), 
                    QueryParameterFactory.getStringLikePropertyParam("name",
                    role.getName()));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                        name() + "-" +
                        UniqueConstrainedField.ROLE_NAME.name());
            }
        } else {
            Integer count = (Integer) getRoleReadDao().getOther(
                    QueryParameterFactory.getElementCountParam("name"),
                    QueryParameterFactory.getConjunctionParam(
                    QueryParameterFactory.getNotEqualPropertyParam("id",
                    role.getId()), QueryParameterFactory.
                    getStringLikePropertyParam(
                    "name", role.getName())));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                        name() + "-" +
                        UniqueConstrainedField.ROLE_NAME.name());
            }

        }

    }

    public void validateUser(User user) {
        if (user.getId() == null) {
            Integer count = (Integer) getUserReadDao().getOther(
                    QueryParameterFactory.getElementCountParam("username"), QueryParameterFactory.getStringLikePropertyParam(
                    "username",
                    user.getUsername()));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                        name() + "-" +
                        UniqueConstrainedField.USER_USERNAME.name());
            }
        } else {
            Integer count = (Integer) getUserReadDao().getOther(
                    QueryParameterFactory.getElementCountParam("username"),
                    QueryParameterFactory.getConjunctionParam(
                    QueryParameterFactory.getNotEqualPropertyParam("id",
                    user.getId()), QueryParameterFactory.
                    getStringLikePropertyParam(
                    "username", user.getUsername())));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                        name() + "-" +
                        UniqueConstrainedField.USER_USERNAME.name());
            }

        }
    }
}