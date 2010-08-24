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
import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.service.SecuredObjectService;
import com.smartitengineering.user.service.UserPersonService;
import com.smartitengineering.user.service.UserService;
import java.util.HashSet;
import java.util.Set;

public class InitialUserFactory {

  private UserService userService;
  private SecuredObjectService securedObjectService;
  private PrivilegeService privilegeService;
  private OrganizationService organizationService;
  private RoleService roleService;
  private UserPersonService userPersonService;

  public UserPersonService getUserPersonService() {
    return userPersonService;
  }

  public void setUserPersonService(UserPersonService userPersonService) {
    this.userPersonService = userPersonService;
  }



  public RoleService getRoleService() {
    return roleService;
  }

  public void setRoleService(RoleService roleService) {
    this.roleService = roleService;
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

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public OrganizationService getOrganizationService() {
    return organizationService;
  }

  public void setOrganizationService(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  public void initializeInformation() {

    intializeRoles();

    Organization organization = new Organization("Smart User", "smart-user");
    organizationService.save(organization);
    organization = organizationService.getOrganizationByUniqueShortName("smart-user");

    Role role = new Role();
    role = roleService.getRoleByName(GlobalRole.ROLE_ADMIN.name());

    Set<Role> roles = new HashSet<Role>();
    roles.add(role);

//    SecuredObject securedObject = new SecuredObject();
//    securedObject.setName("Smart User System");
//    securedObject.setObjectID(organization.getUniqueShortName());
//    securedObject.setOrganization(organization);
//    securedObject.setParentObjectID(null);
//    securedObjectService.save(securedObject);
//
//    securedObject = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObject.
//        getObjectID());
//
//    SecuredObject securedObjectOrganizations = new SecuredObject();
//    securedObjectOrganizations.setName("Smart User Organizations");
//    securedObjectOrganizations.setObjectID("/orgs"); //This objectId is actually the http url of organizations list
//    securedObjectOrganizations.setOrganization(organization);
//    securedObjectOrganizations.setParentObjectID(securedObject.getObjectID());
//    securedObjectService.save(securedObjectOrganizations);
//
//    securedObjectOrganizations = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObjectOrganizations.
//        getObjectID());
//
//    SecuredObject securedObjectUsers = new SecuredObject();
//    securedObjectUsers.setName("Smart User Users");
//    securedObjectUsers.setObjectID("/orgs/smart-user/users"); //This objectId is actually the http url of users list of smart-user organizations
//    securedObjectUsers.setOrganization(organization);
//    securedObjectUsers.setParentObjectID(securedObject.getObjectID());
//    securedObjectService.save(securedObjectUsers);
//
//    securedObjectUsers = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObjectUsers.
//        getObjectID());
//
//    SecuredObject securedObjectSecuredObjects = new SecuredObject();
//    securedObjectSecuredObjects.setName("Smart User Secured Objects");
//    securedObjectSecuredObjects.setObjectID("/orgs/smart-user/securedObjects"); //This objectId is actually the http url of secured objcets list of smart-user organizations
//    securedObjectSecuredObjects.setOrganization(organization);
//    securedObjectSecuredObjects.setParentObjectID(securedObject.getObjectID());
//    securedObjectService.save(securedObjectSecuredObjects);
//
//    securedObjectSecuredObjects = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObjectSecuredObjects.
//        getObjectID());
//
//    SecuredObject securedObjectPrivileges = new SecuredObject();
//    securedObjectPrivileges.setName("Smart User Secured Objects");
//    securedObjectPrivileges.setObjectID("/orgs/smart-user/privileges"); //This objectId is actually the http url of secured objcets list of smart-user organizations
//    securedObjectPrivileges.setOrganization(organization);
//    securedObjectPrivileges.setParentObjectID(securedObject.getObjectID());
//    securedObjectService.save(securedObjectPrivileges);
//
//    securedObjectPrivileges = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObjectPrivileges.
//        getObjectID());
//
//
//
//    Privilege privilege = new Privilege();
//    privilege.setDisplayName("Smart User Adminstration");
//    privilege.setName("smart-user-admin");
//    privilege.setParentOrganization(organization);
//    privilege.setPermissionMask(31); //permission mask 31 means all privileges are there 11111
//    privilege.setSecuredObject(securedObject);
//    privilege.setShortDescription("This admin privilege contains the authority to do any of the CRUD options");
//    privilegeService.create(privilege);
//
//    privilege = privilegeService.getPrivilegeByOrganizationAndPrivilegeName(organization.getUniqueShortName(), privilege.
//        getName());
//    Set<Privilege> privileges = new HashSet();
//    privileges.add(privilege);



    User user = new User();
    user.setOrganization(organization);
    user.setUsername("smartadmin");
    user.setPassword("02040250204039");
//    user.setPrivileges(privileges);
    user.setRoles(roles);
    
    Person person = new Person();
    Name name = new Name();
    name.setFirstName("Super");
    name.setLastName("Admin");
    BasicIdentity self= new BasicIdentity();
    self.setName(name);
    self.setNationalID("1234567890");

    person.setSelf(self);
    person.setPrimaryEmail("info@smart-user.com");

    UserPerson userPerson = new UserPerson();
    userPerson.setPerson(person);
    userPerson.setUser(user);

    userPersonService.create(userPerson);

    //userService.save(user);

  }

  private void intializeRoles() {
    Role role = new Role();
    role.setName(GlobalRole.ROLE_ADMIN.name());
    role.setDisplayName("Global Role for Adminstration");
    role.setShortDescription("This is the global role with all the privileges. The user with this role can do anything to the system");
    roleService.create(role);

    role.setName(GlobalRole.ROLE_READ.name());
    role.setDisplayName("Global Role to Read");
    role.setShortDescription("This is the global role with all reading privileges. The user with this role can read anything from the system");
    roleService.create(role);

    role.setName(GlobalRole.ROLE_CREATE.name());
    role.setDisplayName("Global Role to Create");
    role.setShortDescription("This is the global role with all creation privileges. The user with this role can create any object in the system");
    roleService.create(role);

    role.setName(GlobalRole.ROLE_UPDATE.name());
    role.setDisplayName("Global Role for editing");
    role.setShortDescription("This is the global role with all editing privileges. The user with this role can edit any object in the system");
    roleService.create(role);

    role.setName(GlobalRole.ROLE_DELETE.name());
    role.setDisplayName("Global Role for Deletion");
    role.setShortDescription("This is the global role with all deletion privileges. The user with this role can delete anything from the system");
    roleService.create(role);
  }
}
