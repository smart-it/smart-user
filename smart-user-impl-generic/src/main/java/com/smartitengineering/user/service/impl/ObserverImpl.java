/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl;

import com.smartitengineering.domain.PersistentDTO;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserGroup;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.observer.CRUDObserver;
import com.smartitengineering.user.observer.ObserverNotification;
import com.smartitengineering.user.parser.SmartUserStrings;
import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.SecuredObjectService;
import com.smartitengineering.user.service.Services;
import com.smartitengineering.user.service.UserGroupService;
import com.smartitengineering.user.service.UserPersonService;
import com.smartitengineering.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author modhu7
 */
public class ObserverImpl implements CRUDObserver {

  public static final Logger logger = LoggerFactory.getLogger(ObserverImpl.class);
  public final String ORGS_OID = SmartUserStrings.ORGANIZATIONS_URL;
  public final String USERS_OID = SmartUserStrings.USERS_URL;
  public final String USERS_OID_NAME = "Users";
  public final String SECURED_OBJECTS_OID = SmartUserStrings.SECURED_OBJECTS_URL;
  public final String SECURED_OBJECTS_NAME = "Secured Objects";
  public final String PRIVILEGES_OID = SmartUserStrings.PRIVILEGES_URL;
  public final String PRIVILEGES_OID_NAME = "Privileges";
  public final String ORG_UNIQUE_FRAG = SmartUserStrings.ORGANIZATION_UNIQUE_URL_FRAGMENT;
  public final String ADAM_ORG_SHORT_NAME = SmartUserStrings.FIRST_ORGANIZATION_SHORT_NAME;
  public final String ADMIN_USERNAME = SmartUserStrings.ADMIN_USERNAME;
  public final String ADMIN_PASSWORD = SmartUserStrings.ADMIN_PASSWORD;
  public final String URI_FRAG_CONTENT = SmartUserStrings.CONTENT_URI_FRAGMENT;
  public final String URI_FRAG_UPDATE = SmartUserStrings.UPDATE_URI_FRAGMENT;
  public final String URIFRAG_DELETE = SmartUserStrings.DELETE_URI_FRAGMENT;
  public final Integer PRIVILEGE_PERMISSION_MASK = 31;
  public final String EMAIL_DOMAIN = SmartUserStrings.EMAIL_DOMAIN;
  public final String USER_UNIQUE_FRAG = SmartUserStrings.USER_UNIQUE_URL_FRAGMENT;

  public UserGroupService getUserGroupService() {
    return Services.getInstance().getUserGroupService();
  }

  public OrganizationService getOrganizationService() {
    return Services.getInstance().getOrganizationService();
  }

  public PrivilegeService getPrivilegeService() {
    return Services.getInstance().getPrivilegeService();
  }

  public SecuredObjectService getSecuredObjectService() {
    return Services.getInstance().getSecuredObjectService();
  }

  public UserPersonService getUserPersonService() {
    return Services.getInstance().getUserPersonService();
  }

  public PersonService getPersonService() {
    return Services.getInstance().getPersonService();
  }

  public UserService getUserService() {
    return Services.getInstance().getUserService();
  }

  @Override
  public void update(ObserverNotification notification, PersistentDTO object) {
    if (notification.equals(ObserverNotification.CREATE_ORGANIZATION) && object instanceof Organization) {
      Organization organization = (Organization) object;
      initializeOrganization(organization);
    }
    else if (notification.equals(ObserverNotification.DELETE_ORGNIZATION) && object instanceof Organization) {
      Organization organization = (Organization) object;
      removeOrganization(organization);
    }
    else if (notification.equals(ObserverNotification.CREATE_USER_PERSON) && object instanceof UserPerson) {
      UserPerson userPerson = (UserPerson) object;
      initializeUserPerson(userPerson);
    }
    else if (notification.equals(ObserverNotification.DELETE_USER_PERSON) && object instanceof UserPerson) {
      UserPerson userPerson = (UserPerson) object;
      removeUserPerson(userPerson);
    }
    else if (notification.equals(ObserverNotification.DELETE_PRIVILEGE) && object instanceof Privilege) {
      Privilege privilege = (Privilege) object;
      removePrivilege(privilege);
    }
    else if (notification.equals(ObserverNotification.DELETE_ROLE) && object instanceof Role) {
      Role role = (Role) object;
      removeRole(role);
    }
  }

  private void initializeOrganization(Organization organization) {

    String uniqueShortName = organization.getUniqueShortName();
    organization = getOrganizationService().getOrganizationByUniqueShortName(uniqueShortName);

    User user = new User();
    user.setUsername(ADMIN_USERNAME);
    user.setPassword(ADMIN_PASSWORD);
    user.setOrganization(organization);
    Person person = new Person();
    person.setAddress(organization.getAddress());
    person.setPrimaryEmail(ADMIN_USERNAME + "_" + organization.getUniqueShortName() + "@" + EMAIL_DOMAIN);
    UserPerson userPerson = new UserPerson();
    userPerson.setUser(user);
    userPerson.setPerson(person);
    getUserPersonService().create(userPerson);


    String orgUri = ORGS_OID + ORG_UNIQUE_FRAG + "/" + organization.getUniqueShortName();
    SecuredObject securedObjectOrganization = new SecuredObject();
    securedObjectOrganization.setName(organization.getUniqueShortName());
    securedObjectOrganization.setObjectID(orgUri);
    securedObjectOrganization.setOrganization(organization);
    securedObjectOrganization.setParentObjectID(ORGS_OID);
    getSecuredObjectService().save(securedObjectOrganization);
    securedObjectOrganization = getSecuredObjectService().getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObjectOrganization.
        getObjectID());

    SecuredObject securedObjectUsers = new SecuredObject();
    securedObjectUsers.setName(organization.getUniqueShortName() + "-" + USERS_OID_NAME);
    securedObjectUsers.setObjectID(orgUri + USERS_OID);
    securedObjectUsers.setOrganization(organization);
    securedObjectUsers.setParentObjectID(securedObjectOrganization.getObjectID());
    getSecuredObjectService().save(securedObjectUsers);

    SecuredObject securedObjectSOs = new SecuredObject();
    securedObjectSOs.setName(organization.getUniqueShortName() + "-" + SECURED_OBJECTS_NAME);
    securedObjectSOs.setObjectID(orgUri + SECURED_OBJECTS_OID); //This objectId is actually the http url of secured objcets list of smart-user organizations
    securedObjectSOs.setOrganization(organization);
    securedObjectSOs.setParentObjectID(securedObjectOrganization.getObjectID());
    getSecuredObjectService().save(securedObjectSOs);

    SecuredObject securedObjectPrivileges = new SecuredObject();
    securedObjectPrivileges.setName(organization.getName() + "-" + PRIVILEGES_OID_NAME);
    securedObjectPrivileges.setObjectID(orgUri + PRIVILEGES_OID); //This objectId is actually the http url of secured objcets list of smart-user organizations
    securedObjectPrivileges.setOrganization(organization);
    securedObjectPrivileges.setParentObjectID(securedObjectOrganization.getObjectID());
    getSecuredObjectService().save(securedObjectPrivileges);

    Privilege privilege = new Privilege();
    privilege.setDisplayName(organization.getName() + " " + "admin user profile privilege");
    privilege.setName(organization.getUniqueShortName() + "-" + "admin");
    privilege.setParentOrganization(organization);
    privilege.setPermissionMask(PRIVILEGE_PERMISSION_MASK); //permission mask 31 means all privileges are there 11111
    privilege.setSecuredObject(securedObjectOrganization);
    privilege.setShortDescription("This admin privilege contains the authority to do any of the CRUD options");
    getPrivilegeService().create(privilege);
    privilege = getPrivilegeService().getPrivilegeByOrganizationAndPrivilegeName(organization.getUniqueShortName(), privilege.
        getName());
    user = getUserService().getUserByOrganizationAndUserName(organization.getUniqueShortName(), ADMIN_USERNAME);
    user.getPrivileges().add(privilege);
    getUserService().update(user);
  }

  private void removeOrganization(Organization organization) {
    List<UserGroup> userGroups = new ArrayList<UserGroup>(getUserGroupService().getByOrganizationName(organization.
        getUniqueShortName()));
    for (UserGroup userGroup : userGroups) {
      getUserGroupService().delete(userGroup);
    }
    List<UserPerson> userPersons = new ArrayList<UserPerson>(getUserPersonService().getAllByOrganization(organization.
        getUniqueShortName()));
    for (UserPerson userPerson : userPersons) {
      getUserPersonService().delete(userPerson);
    }
    List<Privilege> privileges = new ArrayList<Privilege>(getPrivilegeService().getPrivilegesByOrganization(organization.
        getUniqueShortName()));
    for (Privilege privilege : privileges) {
      if (privilege != null) {
        logger.info("privilege name: " + privilege.getName());
        logger.info("privilege permission mask : " + privilege.getPermissionMask());
        getPrivilegeService().delete(privilege);
      }
      else {
        logger.info("privilege null");
      }
    }
    List<SecuredObject> securedObjects = new ArrayList<SecuredObject>(getSecuredObjectService().getByOrganization(organization.
        getUniqueShortName()));
    for (SecuredObject securedObject : securedObjects) {
      if (securedObject != null) {
        getSecuredObjectService().delete(securedObject);
      }
    }
  }

  private void initializeUserPerson(UserPerson userPerson) {
    String username = userPerson.getUser().getUsername();
    String organizationShortName = userPerson.getUser().getOrganization().getUniqueShortName();
    UserPerson persistentUserPerson = getUserPersonService().getUserPersonByUsernameAndOrgName(username,
                                                                                               organizationShortName);
    SecuredObject securedObjectUser = new SecuredObject();
    securedObjectUser.setName(username + "-profile");
    String orgUri = ORGS_OID + ORG_UNIQUE_FRAG + "/" + organizationShortName;
    securedObjectUser.setObjectID(orgUri + USERS_OID + USER_UNIQUE_FRAG + "/" + username);
    securedObjectUser.setOrganization(userPerson.getUser().getOrganization());
    securedObjectUser.setParentObjectID(orgUri + USERS_OID);
    getSecuredObjectService().save(securedObjectUser);
    securedObjectUser = getSecuredObjectService().getByOrganizationAndObjectID(userPerson.getUser().getOrganization().
        getUniqueShortName(), securedObjectUser.getObjectID());

    Privilege privilegeUser = new Privilege();
    privilegeUser.setDisplayName(username + "'s Profile Privilege");
    privilegeUser.setName(username + "-" + organizationShortName + "-user-privilege");
    privilegeUser.setParentOrganization(userPerson.getUser().getOrganization());
    privilegeUser.setPermissionMask(PRIVILEGE_PERMISSION_MASK); //permission mask 31 means all privileges are there 11111
    privilegeUser.setSecuredObject(securedObjectUser);
    privilegeUser.setShortDescription(
        "This privilege contains the authority to change the password and profile of the user with username " + userPerson.
        getUser().getUsername());
    getPrivilegeService().create(privilegeUser);

    privilegeUser = getPrivilegeService().getPrivilegeByOrganizationAndPrivilegeName(userPerson.getUser().
        getOrganization().
        getUniqueShortName(), privilegeUser.getName());
    Set<Privilege> privileges = userPerson.getUser().getPrivileges();
    privileges.add(privilegeUser);
    User user = persistentUserPerson.getUser();
    user.setPrivileges(privileges);
    getUserService().update(user);
  }

  private void removeUserPerson(UserPerson userPerson) {
    User user = userPerson.getUser();
    List<UserGroup> userGroups = new ArrayList<UserGroup>(getUserGroupService().getByOrganizationName(userPerson.getUser().
        getOrganization().getUniqueShortName()));
    logger.info("user group size : " + userGroups.size());
    for (UserGroup userGroup : userGroups) {
      if (userGroup != null) {
        List<User> users = new ArrayList<User>(userGroup.getUsers());
        if (users.contains(userPerson.getUser())) {
          userGroup.getUsers().remove(userPerson.getUser());
          getUserGroupService().update(userGroup);
        }
      }
    }
    String organizationShortName = userPerson.getUser().getOrganization().getUniqueShortName();
    String username = userPerson.getUser().getUsername();
    String orgUri = ORGS_OID + ORG_UNIQUE_FRAG + "/" + organizationShortName;
    String privilegeName = username + "-" + organizationShortName + "-user-privilege";

    SecuredObject securedObject = getSecuredObjectService().getByOrganizationAndObjectID(organizationShortName, orgUri +
        USERS_OID + USER_UNIQUE_FRAG + "/" + username);

    List<Privilege> privileges = new ArrayList<Privilege>(getPrivilegeService().
        getPrivilegesByOrganizationNameAndObjectID(
        organizationShortName, securedObject.getObjectID()));

    for (Privilege privilege : privileges) {
//      List<Privilege> privilegeUsers = new ArrayList<Privilege>(user.getPrivileges());
//      for (Privilege privilegeUser : privilegeUsers) {
//        if (privilegeUser.getName().equals(privilege.getName())) {
//          user.getPrivileges().remove(privilegeUser);
//        }
//      }
      user.getPrivileges().remove(privilege);
      getUserService().update(user);
      getPrivilegeService().delete(privilege);
    }
    getSecuredObjectService().delete(securedObject);
  }

  private void removePrivilege(Privilege privilege) {
    List<User> users = new ArrayList<User>(getUserService().getUserByOrganization(privilege.getParentOrganization().
        getUniqueShortName()));
    for (User user : users) {
      List<Privilege> privileges = new ArrayList<Privilege>(user.getPrivileges());
      if (privileges.contains(privilege)) {
        user.getPrivileges().remove(privilege);
        getUserService().update(user);
      }
    }
    List<UserGroup> userGroups = new ArrayList<UserGroup>(getUserGroupService().getByOrganizationName(privilege.
        getParentOrganization().getUniqueShortName()));
    for (UserGroup userGroup : userGroups) {
      if (userGroup != null) {
        List<Privilege> privileges = new ArrayList<Privilege>(userGroup.getPrivileges());
        if (privileges.contains(privilege)) {
          userGroup.getPrivileges().remove(privilege);
          getUserGroupService().update(userGroup);
        }
      }
    }
  }

  private void removeRole(Role role) {
    List<User> users = new ArrayList<User>(getUserService().getAllUser());
    for (User user : users) {
      List<Role> roles = new ArrayList<Role>(user.getRoles());
      if (roles.contains(role)) {
        user.getRoles().remove(role);
        getUserService().update(user);
      }
    }

    List<UserGroup> userGroups = new ArrayList<UserGroup>(getUserGroupService().getAllUserGroup());
    for (UserGroup userGroup : userGroups) {
      if (userGroup != null) {
        List<Role> roles = new ArrayList<Role>(userGroup.getRoles());
        if (roles.contains(role)) {
          userGroup.getRoles().remove(role);
          getUserGroupService().update(userGroup);
        }
      }
    }
  }
}
