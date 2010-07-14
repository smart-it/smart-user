/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.impl;



import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.util.bean.BeanFactoryRegistrar;
import com.smartitengineering.util.bean.annotations.InjectableField;

/**
 *
 * @author russel
 */
public class Services {

    @InjectableField
    private OrganizationService organizationService;
    private UserService userService;
    private PrivilegeService privilegeService;
    private RoleService roleService;

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
