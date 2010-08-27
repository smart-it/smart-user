/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl;

import com.smartitengineering.domain.PersistentDTO;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.observer.CRUDObserver;
import com.smartitengineering.user.observer.ObserverNotification;
import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.SecuredObjectService;
import com.smartitengineering.user.service.UserPersonService;
import com.smartitengineering.user.service.UserService;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author modhu7
 */
public class ObserverImpl implements CRUDObserver {

  final static String ORGS_OID = "/orgs";
  final static String USERS_OID = "/users";
  final static String USERS_OID_NAME = "Users";
  final static String SECURED_OBJECTS_OID = "/securedObjects";
  final static String SECURED_OBJECTS_NAME = "Secured Objects";
  final static String PRIVILEGES_OID = "/privileges";
  final static String PRIVILEGES_OID_NAME = "Privileges";
  final static String ORG_UNIQUE_FRAG = "/shortName";
  final static String ADAM_ORG_SHORT_NAME = "smart-user";
  final static String ADMIN_USERNAME = "admin";
  final static String ADMIN_PASSWORD = "adminadmin";
  final static String URI_FRAG_CONTENT = "/content";
  final static String URI_FRAG_UPDATE = "/update";
  final static String URIFRAG_DELETE = "/delete";
  final static Integer PRIVILEGE_PERMISSION_MASK = 31;

  private UserPersonService userPersonService;
  private PersonService personService;
  private UserService userService;
  private SecuredObjectService securedObjectService;
  private OrganizationService organizationService;
  private PrivilegeService privilegeService;

  public OrganizationService getOrganizationService() {
    return organizationService;
  }

  public void setOrganizationService(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  public PrivilegeService getPrivilegeService() {
    return privilegeService;
  }

  public void setPrivilegeService(PrivilegeService privilegeService) {
    this.privilegeService = privilegeService;
  }

  public SecuredObjectService getSecuredObjectService() {
    return securedObjectService;
  }

  public void setSecuredObjectService(SecuredObjectService securedObjectService) {
    this.securedObjectService = securedObjectService;
  }


  public UserPersonService getUserPersonService() {
    return userPersonService;
  }

  public void setUserPersonService(UserPersonService userPersonService) {
    this.userPersonService = userPersonService;
  }

  public PersonService getPersonService() {
    return personService;
  }

  public void setPersonService(PersonService personService) {
    this.personService = personService;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void update(ObserverNotification notification, PersistentDTO object) {
    if (notification.equals(ObserverNotification.CREATE_ORGANIZATION) && object instanceof Organization) {
      Organization organization = (Organization) object;
      initializeOrganization(organization);
    }
  }

  private void initializeOrganization(Organization organization) {

    String uniqueShortName = organization.getUniqueShortName();
    organization = organizationService.getOrganizationByUniqueShortName(uniqueShortName);

    User user = new User();
    user.setUsername(ADMIN_USERNAME);
    user.setPassword(ADMIN_PASSWORD);
    user.setOrganization(organization);
    userService.save(user);

    String orgUri = ORGS_OID + ORG_UNIQUE_FRAG + "/" + organization.getUniqueShortName();
    SecuredObject securedObjectOrganization = new SecuredObject();
    securedObjectOrganization.setName(organization.getName());
    securedObjectOrganization.setObjectID(orgUri);
    securedObjectOrganization.setOrganization(organization);
    securedObjectOrganization.setParentObjectID(ORGS_OID);
    securedObjectOrganization = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObjectOrganization.
        getObjectID());

    SecuredObject securedObjectUsers = new SecuredObject();
    securedObjectUsers.setName(organization.getName() + USERS_OID_NAME);
    securedObjectUsers.setObjectID(orgUri + USERS_OID);
    securedObjectUsers.setOrganization(organization);
    securedObjectUsers.setParentObjectID(securedObjectOrganization.getObjectID());

    SecuredObject securedObjectSOs = new SecuredObject();
    securedObjectSOs.setName(organization.getName() + SECURED_OBJECTS_NAME);
    securedObjectSOs.setObjectID(orgUri + SECURED_OBJECTS_OID); //This objectId is actually the http url of secured objcets list of smart-user organizations
    securedObjectSOs.setOrganization(organization);
    securedObjectSOs.setParentObjectID(securedObjectOrganization.getObjectID());
    securedObjectService.save(securedObjectSOs);

    SecuredObject securedObjectPrivileges = new SecuredObject();
    securedObjectPrivileges.setName(organization.getName() + PRIVILEGES_OID_NAME);
    securedObjectPrivileges.setObjectID(orgUri + PRIVILEGES_OID); //This objectId is actually the http url of secured objcets list of smart-user organizations
    securedObjectPrivileges.setOrganization(organization);
    securedObjectPrivileges.setParentObjectID(securedObjectOrganization.getObjectID());
    securedObjectService.save(securedObjectPrivileges);

    Privilege privilege = new Privilege();
    privilege.setDisplayName("Smart User Adminstration");
    privilege.setName("smart-user-admin");
    privilege.setParentOrganization(organization);
    privilege.setPermissionMask(PRIVILEGE_PERMISSION_MASK); //permission mask 31 means all privileges are there 11111
    privilege.setSecuredObject(securedObjectOrganization);
    privilege.setShortDescription("This admin privilege contains the authority to do any of the CRUD options");
    privilegeService.create(privilege);
    privilege = privilegeService.getPrivilegeByOrganizationAndPrivilegeName(organization.getUniqueShortName(), privilege.
        getName());

    Set<Privilege> privileges = new HashSet();
    privileges.add(privilege);

    user = userService.getUserByOrganizationAndUserName(ADMIN_USERNAME, organization.getUniqueShortName());
    user.setPrivileges(privileges);
    userService.update(user);

  }
}
