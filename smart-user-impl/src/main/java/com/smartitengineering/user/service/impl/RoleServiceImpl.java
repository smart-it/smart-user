/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl;

import com.smartitengineering.dao.common.queryparam.FetchMode;
import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.dao.impl.hibernate.AbstractCommonDaoImpl;
import com.smartitengineering.domain.PersistentDTO;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.filter.RoleFilter;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.service.UserService;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author modhu7
 */
public class RoleServiceImpl extends AbstractCommonDaoImpl<Role> implements RoleService {

    public RoleServiceImpl() {
        setEntityClass(Role.class);
    }

    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }



    @Override
    public void create(Role role) {
        validateRole(role);
        try {
            super.save(role);
        } catch (ConstraintViolationException e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-" + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        } catch (StaleStateException e) {
            String message =
                    ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-"
                    + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public void delete(Role role) {
        try {
            super.delete(role);
        } catch (Exception e) {
        }
    }

    @Override
    public Collection<Role> getRolesByOrganizationAndUser(String organizationName, String userName) {
        User user = userService.getUserByOrganizationAndUserName(organizationName, userName);
        return user.getRoles();
    }

    @Override
    public Collection<Role> getRolesByOrganization(String organization) {
        Collection<Role> users = new HashSet<Role>();
        QueryParameter qp = QueryParameterFactory.getNestedParametersParam("organization", FetchMode.DEFAULT,QueryParameterFactory.getEqualPropertyParam("uniqueShortName", organization));
        return super.getList(qp);
    }

    @Override
    public Collection<Role> getAllRoles() {
        return super.getAll();
    }

    @Override
    public Role getRoleByOrganizationAndRoleName(String organizationName, String roleName) {
        return super.getSingle(QueryParameterFactory.getStringLikePropertyParam("name", roleName),
               QueryParameterFactory.getNestedParametersParam("organization", FetchMode.DEFAULT,
               QueryParameterFactory.getEqualPropertyParam("uniqueShortName", organizationName)));
    }

    @Override
    public void update(Role role) {
        validateRole(role);
        try {
            super.update(role);
        } catch (ConstraintViolationException e) {
            String message = ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.
                    name() + "-" + UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        } catch (StaleStateException e) {
            String message =
                    ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION.name() + "-" +
                    UniqueConstrainedField.OTHER;
            throw new RuntimeException(message, e);
        }
    }

    public void validateRole(Role role) {

        if (role.getId() == null) {
            Integer count = (Integer) super.getOther(
                    QueryParameterFactory.getElementCountParam("name"),
                    QueryParameterFactory.getStringLikePropertyParam("name",
                    role.getName()));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-"
                        + UniqueConstrainedField.ROLE_NAME.name());
            }
        } else {
            Integer count = (Integer) super.getOther(
                    QueryParameterFactory.getElementCountParam("name"),
                    QueryParameterFactory.getConjunctionParam(
                    QueryParameterFactory.getNotEqualPropertyParam("id",
                    role.getId()), QueryParameterFactory.getStringLikePropertyParam(
                    "name", role.getName())));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-"
                        + UniqueConstrainedField.ROLE_NAME.name());
            }

        }
    }

    @Override
    public Role getRoleByName(String roleName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Role getRoleByOrganizationAndUserAndRole(String organizationShortName, String username, String roleName) {
        Collection<Role> userRoles = getRolesByOrganizationAndUser(organizationShortName, username);
        for(Role role: userRoles){
            if(role.getName().equals(roleName))
                return role;
        }
        return null;
    }

    @Override
    public Collection<Role> search(RoleFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void populateRole(User user) throws Exception{
        List<Integer> roleIDs = user.getRoleIDs();

        if(roleIDs != null && !roleIDs.isEmpty()){
            Set<Role> roles = getByIds(roleIDs);

            if(roles == null || roleIDs.size() != roles.size()){
                throw new Exception("Role not found");
            }
            user.setRoles(roles);
        }
    }

    public void populateRole(Role role) throws Exception {
        List<Integer> roleIDs = role.getRoleIDs();

        if(roleIDs != null && !roleIDs.isEmpty()){
            Set<Role> roles = getByIds(roleIDs);

            if(roles == null || roleIDs.size() != roles.size()){
                throw new Exception("Role not found");
            }
            role.setRoles(roles);
        }
    }
}
