/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl;

import com.smartitengineering.dao.common.queryparam.FetchMode;
import com.smartitengineering.dao.common.queryparam.Order;
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
import com.smartitengineering.user.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
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

    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
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

    @Override
    public Privilege getPrivilegeByName(String privilegeName){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Privilege getPrivilegeByOrganizationAndPrivilegeName(String organizationName, String privilegename){
        return super.getSingle(QueryParameterFactory.getStringLikePropertyParam("name", privilegename),
               QueryParameterFactory.getNestedParametersParam("parentOrganization", FetchMode.DEFAULT,
               QueryParameterFactory.getEqualPropertyParam("uniqueShortName", organizationName)));
    }

    @Override
    public Collection<Privilege> getPrivilegesByOrganizationAndUser(String organizationName, String userName){
        User user = userService.getUserByOrganizationAndUserName(organizationName, userName);
        return user.getPrivileges();
    }

    @Override
    public Collection<Privilege> getPrivilegesByOrganization(String organization){
        Collection<Privilege> users = new HashSet<Privilege>();
        QueryParameter qp = QueryParameterFactory.getNestedParametersParam("parentOrganization", FetchMode.DEFAULT,QueryParameterFactory.getEqualPropertyParam("uniqueShortName", organization));
        return super.getList(qp);
    }

    public Collection<Privilege> getPrivilegess(String nameLike, String name, boolean isSmallerThan, int count){

      List<QueryParameter> params = new ArrayList<QueryParameter>();

      if(StringUtils.isNotBlank(nameLike)){
        final QueryParameter orgNameLikeParam =   QueryParameterFactory.getNestedParametersParam("name",
                                                                                               FetchMode.EAGER,
                                                                                               QueryParameterFactory.getStringLikePropertyParam("name", nameLike));
        params.add(orgNameLikeParam);
      }
      else{
        params.add(QueryParameterFactory.getNestedParametersParam("username", FetchMode.EAGER));
      }

      if(StringUtils.isNotBlank(name)){
        if(isSmallerThan){
          params.add(QueryParameterFactory.getLesserThanPropertyParam("username", name));
        }else{
          params.add(QueryParameterFactory.getGreaterThanPropertyParam("username", name));
        }
      }

      params.add(QueryParameterFactory.getMaxResultsParam(count));
      params.add(QueryParameterFactory.getOrderByParam("id", Order.DESC));
      params.add(QueryParameterFactory.getDistinctPropProjectionParam("id"));

      List<Integer> userIDs = getOtherList(params);

      if (userIDs != null && !userIDs.isEmpty()) {
      List<Privilege> privileges = new ArrayList<Privilege>(super.getByIds(userIDs));
      Collections.sort(privileges, new Comparator<Privilege>(){

        @Override
        public int compare(Privilege o1, Privilege o2) {
          return o1.getId().compareTo(o2.getId()) * -1;
        }
      });
      return privileges;
    }
    else {
      return Collections.emptySet();
    }
    }

    public void validatePrivilege(Privilege privilege){
        if (privilege.getId() == null) {
            Integer count = (Integer) super.getOther(
                    QueryParameterFactory.getElementCountParam("name"), QueryParameterFactory.getConjunctionParam(
                    QueryParameterFactory.getEqualPropertyParam("parentOrganization.id",
                    privilege.getParentOrganization().getId()), QueryParameterFactory.getStringLikePropertyParam(
                    "name", privilege.getName())));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-"
                        + UniqueConstrainedField.SECURED_OBJECT_OBJECT_ID.name());
            }
        } else {
            Integer count = (Integer) super.getOther(
                    QueryParameterFactory.getElementCountParam("name"),
                    QueryParameterFactory.getConjunctionParam(
                    QueryParameterFactory.getNotEqualPropertyParam("id",
                    privilege.getId()), QueryParameterFactory.getEqualPropertyParam("parentOrganization.id",
                    privilege.getParentOrganization().getId()), QueryParameterFactory.getStringLikePropertyParam(
                    "name", privilege.getName())));
            if (count.intValue() > 0) {
                throw new RuntimeException(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION.name() + "-"
                        + UniqueConstrainedField.SECURED_OBJECT_OBJECT_ID.name());
            }

        }
    }

    @Override
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

    @Override
    public void populatePrivilege(Role role)throws Exception {
       throw new UnsupportedOperationException("Not supported yet.");
    }
}
