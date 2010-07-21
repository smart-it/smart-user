/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl;

import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.dao.impl.hibernate.AbstractCommonDaoImpl;
import com.smartitengineering.domain.PersistentDTO;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.PrivilegeService;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author russel
 */
public class PrivilegeServiceImpl extends AbstractCommonDaoImpl<Privilege> implements PrivilegeService{

    public PrivilegeServiceImpl(){
        setEntityClass(Privilege.class);
    }
    @Override
    public void create(Privilege privilege){

        validatePrivilege(privilege);
        try{
            super.save(privilege);
        }catch (ConstraintViolationException e) {
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
    public void delete(Privilege privilege) {
        try {
            super.delete(privilege);
        } catch (Exception e) {
        }
    }

    @Override
    public void update(Privilege privilege) {
        validatePrivilege(privilege);
        try {
            super.update(privilege);
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
//    public Privilege getPrivilegesByObjectID(String objectID){
//
//    }

    public Privilege getPrivilegeByName(String privilegeName){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Privilege getPrivilegeByOrganizationAndPrivilegeName(String organizationName, String privilegename){
        throw new UnsupportedOperationException("Not supported yet.");        
    }

    public Collection<Privilege> getPrivilegesByOrganizationAndUser(String organizationName, String userName){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<Privilege> getPrivilegesByOrganization(String organization){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void validatePrivilege(Privilege privilege){
        if(privilege.getId() == null){

            Integer count = (Integer)super.getOther(QueryParameterFactory.getElementCountParam("name"),
                    QueryParameterFactory.getStringLikePropertyParam("name",
                    privilege.getName()));
            if(count.intValue() > 0){
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-"
                        + UniqueConstrainedField.PRIVILEGE_NAME.name());
            }
        }else{
            Integer count = (Integer) super.getOther(
                    QueryParameterFactory.getElementCountParam("name"),
                    QueryParameterFactory.getConjunctionParam(
                    QueryParameterFactory.getNotEqualPropertyParam("id",
                    privilege.getId()), QueryParameterFactory.getStringLikePropertyParam(
                    "name", privilege.getName())));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-"
                        + UniqueConstrainedField.PRIVILEGE_NAME.name());
            }
        }
    }

    public void populatePrivilege(User user) throws Exception{
        List<Integer> privilegeIDs = user.getPrivilegeIDs();
        if(privilegeIDs != null && ! privilegeIDs.isEmpty()){
            Set<Privilege> privileges = getByIds(privilegeIDs);

            if(privileges == null || privilegeIDs.size() != privileges.size()){
                throw new Exception("Privilege not found");
            }

            user.setPrivileges(privileges);
        }
    }

    public void populatePrivilege(Role role)throws Exception {
        List<Integer> privilegeIDs = role.getPrivilegeIDs();
        if(privilegeIDs != null && ! privilegeIDs.isEmpty()){
            Set<Privilege> privileges = getByIds(privilegeIDs);

            if(privileges == null || privilegeIDs.size() != privileges.size()){
                throw new Exception("Privilege not found");
            }

            role.setPrivileges(privileges);
        }
    }
}
