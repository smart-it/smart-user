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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author modhu7
 */
public class ObserverImpl implements CRUDObserver {

  final static String ORGS_OID = "/orgs";
  final static String USERS_OID = "/users";
  final static String USERS_OID_NAME = "Users";
  final static String SECURED_OBJECTS_OID = "/so";
  final static String SECURED_OBJECTS_NAME = "Secured Objects";
  final static String PRIVILEGES_OID = "/privs";
  final static String PRIVILEGES_OID_NAME = "Privileges";
  final static String ORG_UNIQUE_FRAG = "/sn";
  final static String ADAM_ORG_SHORT_NAME = "smart-user";
  final static String ADMIN_USERNAME = "admin";
  final static String ADMIN_PASSWORD = "adminadmin";
  final static String URI_FRAG_CONTENT = "/content";
  final static String URI_FRAG_UPDATE = "/update";
  final static String URIFRAG_DELETE = "/delete";
  final static Integer PRIVILEGE_PERMISSION_MASK = 31;
  final static String EMAIL_DOMAIN = "smartitengineering.com";
  final static String USER_UNIQUE_FRAG = "/un";

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
    }else if(notification.equals(ObserverNotification.DELETE_ORGNIZATION) && object instanceof Organization){
      Organization organization = (Organization) object;
      removeOrganization(organization);
    }else if(notification.equals(ObserverNotification.CREATE_USER_PERSON) && object instanceof UserPerson){
      UserPerson userPerson = (UserPerson) object;
      initializeUserPerson(userPerson);
    }else if(notification.equals(ObserverNotification.DELETE_USER_PERSON) && object instanceof UserPerson){
      UserPerson userPerson = (UserPerson) object;
      removeUserPerson(userPerson);
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
    securedObjectService.save(securedObjectOrganization);
    securedObjectOrganization = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObjectOrganization.
        getObjectID());

    SecuredObject securedObjectUsers = new SecuredObject();
    securedObjectUsers.setName(organization.getName() + USERS_OID_NAME);
    securedObjectUsers.setObjectID(orgUri + USERS_OID);
    securedObjectUsers.setOrganization(organization);
    securedObjectUsers.setParentObjectID(securedObjectOrganization.getObjectID());
    securedObjectService.save(securedObjectUsers);

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
    privilege.setDisplayName(organization.getName() + " " + "admin user profile privilege");
    privilege.setName(organization.getName() + " " + "admin");
    privilege.setParentOrganization(organization);
    privilege.setPermissionMask(PRIVILEGE_PERMISSION_MASK); //permission mask 31 means all privileges are there 11111
    privilege.setSecuredObject(securedObjectOrganization);
    privilege.setShortDescription("This admin privilege contains the authority to do any of the CRUD options");
    privilegeService.create(privilege);
    privilege = privilegeService.getPrivilegeByOrganizationAndPrivilegeName(organization.getUniqueShortName(), privilege.
        getName());

    Set<Privilege> privileges = new HashSet();
    privileges.add(privilege);

    user = userService.getUserByOrganizationAndUserName(organization.getUniqueShortName(), ADMIN_USERNAME);
    user.setPrivileges(privileges);
    userService.update(user);

    UserPerson userPerson = new UserPerson();
    userPerson.setUser(user);
    Person person = new Person();
    person.setPrimaryEmail(ADMIN_USERNAME + "_" + organization.getUniqueShortName() + "@" +EMAIL_DOMAIN);
    userPerson.setPerson(person);
    userPersonService.create(userPerson);

  }

  private void removeOrganization(Organization organization) {
    List<UserPerson> userPersons = new ArrayList<UserPerson>(userPersonService.getAllByOrganization(organization.getUniqueShortName()));
    for(UserPerson userPerson : userPersons){
      userPersonService.delete(userPerson);
    }
    List<Privilege> privileges = new ArrayList<Privilege>(privilegeService.getPrivilegesByOrganization(organization.getUniqueShortName()));
    for(Privilege privilege : privileges){
      privilegeService.delete(privilege);
    }
    List<SecuredObject> securedObjects = new ArrayList<SecuredObject>(securedObjectService.getByOrganization(organization.getUniqueShortName()));
    for(SecuredObject securedObject : securedObjects){
      securedObjectService.delete(securedObject);
    }
  }

  private void initializeUserPerson(UserPerson userPerson) {
    String username = userPerson.getUser().getUsername();
    String organizationShortName = userPerson.getUser().getOrganization().getUniqueShortName();
    UserPerson persistentUserPerson = userPersonService.getUserPersonByUsernameAndOrgName(username, organizationShortName);
    SecuredObject securedObjectUser = new SecuredObject();
    securedObjectUser.setName(username + "'s Profile");
    String orgUri = ORGS_OID + ORG_UNIQUE_FRAG + "/" + organizationShortName;
    securedObjectUser.setObjectID(orgUri + USERS_OID + USER_UNIQUE_FRAG + "/" + username);
    securedObjectUser.setOrganization(userPerson.getUser().getOrganization());
    securedObjectUser.setParentObjectID(orgUri+USERS_OID);
    securedObjectService.save(securedObjectUser);
    securedObjectUser = securedObjectService.getByOrganizationAndObjectID(userPerson.getUser().getOrganization().getUniqueShortName(), securedObjectUser.
        getObjectID());

    Privilege privilegeUser = new Privilege();
    privilegeUser.setDisplayName(username + "'s Profile Privilege");
    privilegeUser.setName(username + "-" + organizationShortName + "-user-privilege");
    privilegeUser.setParentOrganization(userPerson.getUser().getOrganization());
    privilegeUser.setPermissionMask(PRIVILEGE_PERMISSION_MASK); //permission mask 31 means all privileges are there 11111
    privilegeUser.setSecuredObject(securedObjectUser);
    privilegeUser.setShortDescription(
        "This privilege contains the authority to change the password and profile of the user with username " + userPerson.getUser().getUsername());
    privilegeService.create(privilegeUser);

    privilegeUser = privilegeService.getPrivilegeByOrganizationAndPrivilegeName(userPerson.getUser().getOrganization().getUniqueShortName(), privilegeUser.
        getName());
    Set<Privilege> privileges = userPerson.getUser().getPrivileges();
    privileges.add(privilegeUser);
    User user = userPerson.getUser();
    user.setPrivileges(privileges);
    userService.update(user);
  }

  private void removeUserPerson(UserPerson userPerson) {
    String organizationShortName = userPerson.getUser().getOrganization().getUniqueShortName();
    String username = userPerson.getUser().getUsername();
    String orgUri = ORGS_OID + ORG_UNIQUE_FRAG + "/" + organizationShortName;
    String privilegeName = username + "-" + organizationShortName + "-user-privilege";
    SecuredObject securedObject = securedObjectService.getByOrganizationAndObjectID(organizationShortName, orgUri + USER_UNIQUE_FRAG + "/" + username);
    List<Privilege> privileges = new ArrayList<Privilege>(privilegeService.getPrivilegesByOrganizationNameAndObjectID(organizationShortName, securedObject.getObjectID()));
    for(Privilege privilege : privileges){
      privilegeService.delete(privilege);
    }
    securedObjectService.delete(securedObject);
  }

}
