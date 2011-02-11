/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl;

import com.google.inject.Inject;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserGroup;
import com.smartitengineering.user.parser.SmartUserStrings;
import com.smartitengineering.user.service.AuthorizationService;
import com.smartitengineering.user.service.SecuredObjectService;
import com.smartitengineering.user.service.UserGroupService;
import com.smartitengineering.user.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.vote.AccessDecisionVoter;

/**
 *
 * @author modhu7
 */
public class AuthorizationServiceImpl implements AuthorizationService {

  @Inject
  private UserService userService;
  @Inject
  private SecuredObjectService securedObjectService;
  @Inject
  private UserGroupService userGroupService;
  private static Logger logger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);

  public SecuredObjectService getSecuredObjectService() {
    return securedObjectService;
  }

  public void setSecuredObjectService(SecuredObjectService securedObjectService) {
    this.securedObjectService = securedObjectService;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public UserGroupService getUserGroupService() {
    return userGroupService;
  }

  public void setUserGroupService(UserGroupService userGroupService) {
    this.userGroupService = userGroupService;
  }

  @Override
  public Integer authorize(String username, String organizationName, String oid, Integer permission) {

    User user = userService.getUserByOrganizationAndUserName(organizationName, username);
    if (user == null) {
      logger.info("User is null: Access denied");
      return AccessDecisionVoter.ACCESS_DENIED;
    }
    if (user != null && oid == null) {
      logger.info("Oid is null: Access abstain");
      return AccessDecisionVoter.ACCESS_ABSTAIN;
    }
    if (oid.contains(SmartUserStrings.PRIVILEGES_URL) || oid.contains(SmartUserStrings.ROLES_URL)) {
      return AuthorizeForPrivilegeAndRoleOperations(oid, username, organizationName, permission);
    }
    for (Privilege privilege : user.getPrivileges()) {
      if (oid.startsWith(privilege.getSecuredObject().getObjectID()) && (permission.intValue() & privilege.
                                                                         getPermissionMask().intValue()) == permission.
          intValue()) {
        return AccessDecisionVoter.ACCESS_GRANTED;
      }
    }
    SecuredObject securedObject = securedObjectService.getByOrganizationAndObjectID(organizationName, oid);
    if (user != null && securedObject == null) {
      logger.info("Secured object is null with oid:" + oid + ": Access Abstain");
      return AccessDecisionVoter.ACCESS_ABSTAIN;
    }
    else if (authorize(user.getPrivileges(), securedObject, permission) == AccessDecisionVoter.ACCESS_GRANTED) {
      logger.info("Wow! Access is granted");
      return AccessDecisionVoter.ACCESS_GRANTED;
    }
    else {
      List<UserGroup> userGroups = new ArrayList<UserGroup>(userGroupService.getUserGroupsByUser(user));
      for (UserGroup userGroup : userGroups) {
        if (authorize(userGroup.getPrivileges(), securedObject, permission) == AccessDecisionVoter.ACCESS_GRANTED) {
          logger.info("Access is granted from user group ");
          return AccessDecisionVoter.ACCESS_GRANTED;
        }
      }
      logger.info("Alas! no one says anyting, so access abstain");
      return AccessDecisionVoter.ACCESS_ABSTAIN;
    }
  }

  private Integer authorize(Collection<Privilege> privileges, SecuredObject securedObject, Integer permission) {
    logger.info("Start authorizing by privilege with secured object :" + securedObject.getObjectID() + ", permission: " +
        permission);
    for (Privilege privilege : privileges) {
      logger.info("Checking with privilege " + privilege.getName() + ", oid" +
          privilege.getSecuredObject().getObjectID());
      if (privilege.getSecuredObject().getObjectID().equals(securedObject.getObjectID()) && (permission.intValue() & privilege.
                                                                                             getPermissionMask().
                                                                                             intValue()) == permission.
          intValue()) {
        logger.info("Wow! Access is granted from authorize method, either direct privilege or parent privilege");
        return AccessDecisionVoter.ACCESS_GRANTED;
      }
    }
    logger.debug("user dont have direct privilege for the operation, Check for parent privilege will be done next");
    if (StringUtils.isNotBlank(securedObject.getParentObjectID())) {

      SecuredObject parentSecuredObject = securedObjectService.getByOrganizationAndObjectID(securedObject.
          getOrganization().
          getUniqueShortName(), securedObject.getParentObjectID());
      if (parentSecuredObject == null) {
        logger.info("Access abstaing since there is no parent");
        return AccessDecisionVoter.ACCESS_ABSTAIN;
      }
      logger.debug("authorize by parent is about to start");
      return authorize(privileges, securedObjectService.getByOrganizationAndObjectID(securedObject.getOrganization().
          getUniqueShortName(), securedObject.getParentObjectID()), permission);
    }
    else {
      logger.info("Ultimately access is denied");
      return AccessDecisionVoter.ACCESS_DENIED;
    }
  }

  @Override
  public Boolean login(String username, String password) {
    User user = userService.getUserByUsername(username);
    if (user != null && user.getPassword().equals(password)) {
      return true;
    }
    else {
      return false;
    }
  }

  public Integer AuthorizeForPrivilegeAndRoleOperations(String oid, String username, String organizationName,
                                                        int permission) {
    logger.info("AuthorizeForPrivilegeAndRoleOperations is called because oid is related to role or privilege.  oid:" +
        oid + ", username: " + username + ", organization name: " + organizationName + ", permission: " + permission);
    if (permission == BasePermission.READ.getMask() && oid.contains(SmartUserStrings.USERS_URL +
        SmartUserStrings.USER_UNIQUE_URL_FRAGMENT + "/" + username)) {
      logger.debug(
          "permission is read permission, oid contains user url, so authorize method for user object is being called");
      return authorize(username, organizationName, SmartUserStrings.ORGANIZATIONS_URL +
          SmartUserStrings.ORGANIZATION_UNIQUE_URL_FRAGMENT + "/" + organizationName + SmartUserStrings.USERS_URL +
          SmartUserStrings.USER_UNIQUE_URL_FRAGMENT + "/" + username, permission);
    }
    else {
      logger.debug(
          "permission is read permission, oid contains user url, so authorize method for user object is being called");
      return authorize(username, organizationName, SmartUserStrings.ORGANIZATIONS_URL +
          SmartUserStrings.ORGANIZATION_UNIQUE_URL_FRAGMENT + "/" + organizationName, permission);
    }
  }
}
