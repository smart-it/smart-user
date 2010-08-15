package com.smartitengineering.user.initialization;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.service.SecuredObjectService;
import com.smartitengineering.user.service.UserService;
import java.util.HashSet;
import java.util.Set;

public class InitialUserFactory {

  private UserService userService;
  private SecuredObjectService securedObjectService;
  private PrivilegeService privilegeService;
  private OrganizationService organizationService;
  private RoleService roleService;

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

    Organization organization = new Organization("Smart User", "smart-user");
    organizationService.save(organization);
    organization = organizationService.getOrganizationByUniqueShortName("smart-user");

    Role role = new Role();
    role.setName("ROLE_SUPER_ADMIN");
    role.setDisplayName("Super admin role");    
    role.setShortDescription("The global role for super admin. It will be applied to every url coming to smart user system");
    roleService.create(role);    
    role = roleService.getRoleByName(role.getName());

    Set<Role> roles = new HashSet<Role>();
    roles.add(role);

    SecuredObject securedObject = new SecuredObject();
    securedObject.setName("Smart User System");
    securedObject.setObjectID(organization.getUniqueShortName());
    securedObject.setOrganization(organization);
    securedObject.setParentObjectID(null);
    securedObjectService.save(securedObject);

    securedObject = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObject.
        getObjectID());

    SecuredObject securedObjectOrganizations = new SecuredObject();
    securedObjectOrganizations.setName("Smart User Organizations");
    securedObjectOrganizations.setObjectID("/orgs"); //This objectId is actually the http url of organizations list
    securedObjectOrganizations.setOrganization(organization);
    securedObjectOrganizations.setParentObjectID(securedObject.getObjectID());
    securedObjectService.save(securedObjectOrganizations);

    securedObjectOrganizations = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObjectOrganizations.
        getObjectID());

    SecuredObject securedObjectUsers = new SecuredObject();
    securedObjectUsers.setName("Smart User Users");
    securedObjectUsers.setObjectID("/orgs/smart-user/users"); //This objectId is actually the http url of users list of smart-user organizations
    securedObjectUsers.setOrganization(organization);
    securedObjectUsers.setParentObjectID(securedObject.getObjectID());
    securedObjectService.save(securedObjectUsers);

    securedObjectUsers = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObjectUsers.
        getObjectID());

    SecuredObject securedObjectSecuredObjects = new SecuredObject();
    securedObjectSecuredObjects.setName("Smart User Secured Objects");
    securedObjectSecuredObjects.setObjectID("/orgs/smart-user/securedObjects"); //This objectId is actually the http url of secured objcets list of smart-user organizations
    securedObjectSecuredObjects.setOrganization(organization);
    securedObjectSecuredObjects.setParentObjectID(securedObject.getObjectID());
    securedObjectService.save(securedObjectSecuredObjects);

    securedObjectSecuredObjects = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObjectSecuredObjects.
        getObjectID());

    SecuredObject securedObjectPrivileges = new SecuredObject();
    securedObjectPrivileges.setName("Smart User Secured Objects");
    securedObjectPrivileges.setObjectID("/orgs/smart-user/privileges"); //This objectId is actually the http url of secured objcets list of smart-user organizations
    securedObjectPrivileges.setOrganization(organization);
    securedObjectPrivileges.setParentObjectID(securedObject.getObjectID());
    securedObjectService.save(securedObjectPrivileges);

    securedObjectPrivileges = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObjectPrivileges.
        getObjectID());



    Privilege privilege = new Privilege();
    privilege.setDisplayName("Smart User Adminstration");
    privilege.setName("smart-user-admin");
    privilege.setParentOrganization(organization);
    privilege.setPermissionMask(31); //permission mask 31 means all privileges are there 11111
    privilege.setSecuredObject(securedObject);
    privilege.setShortDescription("This admin privilege contains the authority to do any of the CRUD options");
    privilegeService.create(privilege);

    privilege = privilegeService.getPrivilegeByOrganizationAndPrivilegeName(organization.getUniqueShortName(), privilege.
        getName());
    Set<Privilege> privileges = new HashSet();
    privileges.add(privilege);



    User user = new User();
    user.setOrganization(organization);
    user.setUsername("smartadmin");
    user.setPassword("02040250204039");
    user.setPrivileges(privileges);
    user.setRoles(roles);
    userService.save(user);

  }
}
