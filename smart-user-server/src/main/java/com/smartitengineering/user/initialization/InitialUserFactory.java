package com.smartitengineering.user.initialization;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.SecuredObjectService;
import com.smartitengineering.user.service.UserService;
import java.util.HashSet;
import java.util.Set;

public class InitialUserFactory {

    private UserService userService;
    private SecuredObjectService securedObjectService;
    private PrivilegeService privilegeService;
    private OrganizationService organizationService;

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

        SecuredObject securedObject = new SecuredObject();
        securedObject.setName("Smart User System");
        securedObject.setObjectID(organization.getUniqueShortName());
        securedObject.setOrganization(organization);
        securedObject.setParentObject(null);
        securedObjectService.save(securedObject);

        securedObject = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObject.getObjectID());

        SecuredObject securedObjectOrganizations = new SecuredObject();
        securedObjectOrganizations.setName("Smart User Organizations");
        securedObjectOrganizations.setObjectID("/orgs"); //This objectId is actually the http url of organizations list
        securedObjectOrganizations.setOrganization(organization);
        securedObjectOrganizations.setParentObject(securedObject);
        securedObjectService.save(securedObjectOrganizations);

        securedObjectOrganizations = securedObjectService.getByOrganizationAndObjectID(organization.getUniqueShortName(), securedObjectOrganizations.getObjectID());

        Privilege privilege = new Privilege();
        privilege.setDisplayName("Smart User Adminstration");
        privilege.setName("smart-user-admin");
        privilege.setParentOrganization(organization);
        privilege.setPermissionMask(31); //permission mask 31 means all privileges are there 11111
        privilege.setSecuredObject(securedObject);
        privilege.setShortDescription("This admin privilege contains the authority to do any of the CRUD options");
        privilegeService.create(privilege);

        privilege = privilegeService.getPrivilegeByOrganizationAndPrivilegeName(organization.getUniqueShortName(), privilege.getName());
        Set<Privilege> privileges = new HashSet();
        privileges.add(privilege);

        Privilege privilegeOrganizations = new Privilege();
        privilegeOrganizations.setDisplayName("Adminstration of Organizations");
        privilegeOrganizations.setName("organizations-admin");
        privilegeOrganizations.setParentOrganization(organization);
        privilegeOrganizations.setPermissionMask(31); //permission mask 31 means all privileges are there 11111
        privilegeOrganizations.setSecuredObject(securedObjectOrganizations);
        privilegeOrganizations.setShortDescription("This admin privilege contains the authority to create organization and read list of organizations");
        privilegeService.create(privilegeOrganizations);

        privilegeOrganizations = privilegeService.getPrivilegeByOrganizationAndPrivilegeName(organization.getUniqueShortName(), privilegeOrganizations.getName());
        privileges.add(privilegeOrganizations);


        User user = new User();
        user.setOrganization(organization);
        user.setUsername("smartadmin");
        user.setPassword("02040250204039");
        user.setPrivileges(privileges);
        userService.save(user);
        
    }
}