package com.smartitengineering.user.initialization;

import com.smartitengineering.user.domain.BasicIdentity;
import com.smartitengineering.user.domain.GlobalRole;
import com.smartitengineering.user.domain.Name;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.parser.SmartUserStrings;
import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.service.SecuredObjectService;
import com.smartitengineering.user.service.UserPersonService;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.ws.resources.Services;
import java.util.HashSet;
import java.util.Set;

public class InitialUserFactory {

  private final String ORG_NAME = SmartUserStrings.FIRST_ORGANIZATION_NAME;
  private final String ORG_SHORTNAME = SmartUserStrings.FIRST_ORGANIZATION_SHORT_NAME;
  private final String ORG_SECURED_OBJECT_NAME = "Smart User Organization";
  private final String SUPER_ADMIN_USERNAME = SmartUserStrings.SUPER_ADMIN_USERNAME;
  private final String SUPER_ADMIN_PASSWORD = SmartUserStrings.SUPER_ADMIN_PASSWORD;
  private final String ORGS_OID = SmartUserStrings.ORGANIZATIONS_URL;
  private final String ORGS_OID_NAME = "smart-user-organizations";
  private final String USERS_OID_NAME = "smart-user-users";
  private final String USERS_OID = SmartUserStrings.USERS_URL;
  private final String SECURED_OBJECTS_OID = SmartUserStrings.SECURED_OBJECTS_URL;
  private final String SECURED_OBJECTS_OID_NAME = "smart-user-secured-objects";
  private final String PRIVILEGES_OID = SmartUserStrings.PRIVILEGES_URL;
  private final String PRIVILEGES_OID_NAME = "smart-user-privileges";
  private final Integer PRIVILEGE_PERMISSION_MASK = 31;
  private final String USER_OID_NAME = "super-admin-user-secured-object";
  private final String USER_UNIQUE_FRAG = SmartUserStrings.USER_UNIQUE_URL_FRAGMENT;
  private final String ORG_UNIQUE_FRAG = SmartUserStrings.ORGANIZATION_UNIQUE_URL_FRAGMENT;

  public UserPersonService getUserPersonService() {
    return Services.getInstance().getUserPersonService();
  }

  public RoleService getRoleService() {
    return Services.getInstance().getRoleService();
  }

  public PrivilegeService getPrivilegeService() {
    return Services.getInstance().getPrivilegeService();
  }

  public SecuredObjectService getSecuredObjectService() {
    return Services.getInstance().getSecuredObjectService();
  }

  public UserService getUserService() {
    return Services.getInstance().getUserService();
  }

  public OrganizationService getOrganizationService() {
    return Services.getInstance().getOrganizationService();
  }

  public void initializeInformation() {
    if (getOrganizationService().getAllOrganization().isEmpty()) {
      initialize();
    }
  }

  public void initialize() {

    intializeRoles();

    Organization organization = new Organization(ORG_NAME, ORG_SHORTNAME);
    getOrganizationService().save(organization);
    organization = getOrganizationService().getOrganizationByUniqueShortName(ORG_SHORTNAME);

    Role role = new Role();
    role = getRoleService().getRoleByName(GlobalRole.ROLE_ADMIN.name());

    Set<Role> roles = new HashSet<Role>();
    roles.add(role);



    SecuredObject securedObjectOrganizations = new SecuredObject();
    securedObjectOrganizations.setName(ORGS_OID_NAME);
    securedObjectOrganizations.setObjectID(ORGS_OID); //This objectId is actually the http url of organizations list
    securedObjectOrganizations.setOrganization(organization);
    securedObjectOrganizations.setParentObjectID(null);
    getSecuredObjectService().save(securedObjectOrganizations);
    securedObjectOrganizations = getSecuredObjectService().getByOrganizationAndObjectID(
        organization.getUniqueShortName(), securedObjectOrganizations.getObjectID());

    String orgUri = ORGS_OID + ORG_UNIQUE_FRAG + "/" + organization.getUniqueShortName();
    SecuredObject securedObject;
    securedObject = getSecuredObjectService().getByOrganizationAndObjectID(organization.getUniqueShortName(), orgUri);
    securedObject.setParentObjectID(securedObjectOrganizations.getObjectID());
    getSecuredObjectService().update(securedObject);

    Privilege privilege = getPrivilegeService().getPrivilegeByOrganizationAndPrivilegeName(organization.getUniqueShortName(), "smart-user-admin");
    privilege.setSecuredObject(securedObjectOrganizations);
    getPrivilegeService().update(privilege);
    privilege = getPrivilegeService().getPrivilegeByOrganizationAndPrivilegeName(organization.getUniqueShortName(), "smart-user-admin");
    Set<Privilege> privileges = new HashSet();
    privileges.add(privilege);

    User user = new User();
    user.setOrganization(organization);
    user.setUsername(SUPER_ADMIN_USERNAME);
    user.setPassword(SUPER_ADMIN_PASSWORD);
    user.setPrivileges(privileges);
    user.setRoles(roles);

    Person person = new Person();
    Name name = new Name();
    name.setFirstName("Super");
    name.setLastName("Admin");
    BasicIdentity self = new BasicIdentity();
    self.setName(name);
    self.setNationalID("");

    person.setSelf(self);
    person.setPrimaryEmail("info@smart-user.com");

    UserPerson userPerson = new UserPerson();
    userPerson.setPerson(person);
    userPerson.setUser(user);

    getUserPersonService().create(userPerson);

    //userService.save(user);

    user = getUserService().getUserByOrganizationAndUserName(user.getOrganization().getUniqueShortName(), user.
        getUsername());

    SecuredObject securedObjectUser;
    securedObjectUser = getSecuredObjectService().getByOrganizationAndObjectID(organization.getUniqueShortName(), orgUri +
        USER_UNIQUE_FRAG + "/" + user.getUsername());

    Privilege privilegeUser = new Privilege();
    privilegeUser.setDisplayName("Admin User Profile Privilege");
    privilegeUser.setName("super-admin-user-privilege");
    privilegeUser.setParentOrganization(organization);
    privilegeUser.setPermissionMask(PRIVILEGE_PERMISSION_MASK); //permission mask 31 means all privileges are there 11111
    privilegeUser.setSecuredObject(securedObjectUser);
    privilegeUser.setShortDescription(
        "This privilege contains the authority to change the password and profile of the super admin.");
    getPrivilegeService().create(privilegeUser);

    privilegeUser = getPrivilegeService().getPrivilegeByOrganizationAndPrivilegeName(organization.getUniqueShortName(), privilegeUser.
        getName());
    privileges.add(privilegeUser);

    user.setPrivileges(privileges);
    getUserService().update(user);

  }

  private void intializeRoles() {
    Role role = new Role();
    role.setName(GlobalRole.ROLE_ADMIN.name());
    role.setDisplayName("Global Role for Adminstration");
    role.setShortDescription(
        "This is the global role with all the privileges. The user with this role can do anything to the system");
    getRoleService().create(role);

    role.setName(GlobalRole.ROLE_READ.name());
    role.setDisplayName("Global Role to Read");
    role.setShortDescription(
        "This is the global role with all reading privileges. The user with this role can read anything from the system");
    getRoleService().create(role);

    role.setName(GlobalRole.ROLE_CREATE.name());
    role.setDisplayName("Global Role to Create");
    role.setShortDescription(
        "This is the global role with all creation privileges. The user with this role can create any object in the system");
    getRoleService().create(role);

    role.setName(GlobalRole.ROLE_UPDATE.name());
    role.setDisplayName("Global Role for editing");
    role.setShortDescription(
        "This is the global role with all editing privileges. The user with this role can edit any object in the system");
    getRoleService().create(role);

    role.setName(GlobalRole.ROLE_DELETE.name());
    role.setDisplayName("Global Role for Deletion");
    role.setShortDescription(
        "This is the global role with all deletion privileges. The user with this role can delete anything from the system");
    getRoleService().create(role);
  }
}
