/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl;

import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.service.AuthorizationService;
import com.smartitengineering.user.service.SecuredObjectService;
import com.smartitengineering.user.service.UserService;
import org.springframework.security.vote.AccessDecisionVoter;

/**
 *
 * @author modhu7
 */
public class AuthorizationServiceImpl implements AuthorizationService {

  private UserService UserService;
  private SecuredObjectService securedObjectService;

  public SecuredObjectService getSecuredObjectService() {
    return securedObjectService;
  }

  public void setSecuredObjectService(SecuredObjectService securedObjectService) {
    this.securedObjectService = securedObjectService;
  }

  public UserService getUserService() {
    return UserService;
  }

  public void setUserService(UserService UserService) {
    this.UserService = UserService;
  }

  @Override
  public Integer authorize(String username, String organizationName, String oid, Integer permission) {

    User user = UserService.getUserByOrganizationAndUserName(organizationName, username);
    if (user == null) {
      return AccessDecisionVoter.ACCESS_DENIED;
    }
    SecuredObject securedObject = securedObjectService.getByOrganizationAndObjectID(organizationName, oid);
    return authorize(user, securedObject, permission);    

  }

  private Integer authorize(User user, SecuredObject securedObject, Integer permission) {
    
    if (user == null || user.getPrivileges() == null || permission == null || securedObject == null) {      
      return AccessDecisionVoter.ACCESS_DENIED;
    }
    for (Privilege privilege : user.getPrivileges()) {
      if (privilege.getSecuredObject().getObjectID().equals(securedObject.getObjectID())
          && (permission.intValue() & privilege.getPermissionMask().intValue()) == permission.intValue()) {
        return AccessDecisionVoter.ACCESS_GRANTED;
      }      
    }
    if (securedObject.getParentObject() != null) {
      return authorize(user, securedObject.getParentObject(), permission);
    }
    else {
      return AccessDecisionVoter.ACCESS_DENIED;
    }
  }
}
