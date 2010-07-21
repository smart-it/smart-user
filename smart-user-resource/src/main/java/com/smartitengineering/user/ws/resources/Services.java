/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;



import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.service.SecuredObjectService;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.util.bean.BeanFactoryRegistrar;
import com.smartitengineering.util.bean.annotations.Aggregator;
import com.smartitengineering.util.bean.annotations.InjectableField;

/**
 *
 * @author russel
 */
@Aggregator(contextName = "com.smartitengineering.user.service")
public class Services {

    @InjectableField
    private OrganizationService organizationService;
    @InjectableField
    private UserService userService;
    @InjectableField
    private PrivilegeService privilegeService;
    @InjectableField
    private RoleService roleService;
    @InjectableField
    private SecuredObjectService securedObjectService;

    private Services(){
        
    }

    public OrganizationService getOrganizationService(){
        return organizationService;
    }
    public UserService getUserService(){
        return userService;
    }
    public PrivilegeService getPrivilegeService(){
        return privilegeService;
    }
    public RoleService getRoleService(){
        return roleService;
    }

    public SecuredObjectService getSecuredObjectService() {
        return securedObjectService;
    }

    public void setSecuredObjectService(SecuredObjectService securedObjectService) {
        this.securedObjectService = securedObjectService;
    }
    

    private static Services services;
    public static Services getInstance(){
        if(services == null)
        {
            services = new Services();
            BeanFactoryRegistrar.aggregate(services);
        }
        return services;
    }

}
