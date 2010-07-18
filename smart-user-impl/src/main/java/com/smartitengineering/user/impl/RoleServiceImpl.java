/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.impl;

import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.dao.impl.hibernate.AbstractCommonDaoImpl;
import com.smartitengineering.domain.PersistentDTO;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.filter.RoleFilter;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.RoleService;
import java.util.Collection;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author modhu7
 */
public class RoleServiceImpl extends AbstractCommonDaoImpl<Role> implements RoleService {

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
    public Collection<Role> getRolesByOrganizationAndUser(String organization, String user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Role> getRolesByOrganization(String organization) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Role> getAllRoles() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Role getRoleByOrganizationAndRoleName(String organization, String roleName) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        QueryParameter qp;
        qp = QueryParameterFactory.getEqualPropertyParam("name", roleName);
        Role role = new Role();
        try {
            role = super.getSingle(qp);
        } catch (Exception e) {
        }
        return role;
    }

    @Override
    public Role getRoleByOrganizationAndUserAndRole(String organizationShortName, String username, String roleName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Role> search(RoleFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
