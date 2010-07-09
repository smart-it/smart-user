/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.organization.services;

import com.smartitengineering.user.service.UserService;
import com.smartitengineering.util.spring.BeanFactoryRegistrar;
import com.smartitengineering.util.spring.annotations.InjectableField;

/**
 *
 * @author russel
 */
public class Services {

    @InjectableField
    private OrganizationService organizationService;
    private UserService userService;

    private Services(){
        
    }

    public OrganizationService getOrganizationService(){
        return organizationService;
    }
    public UserService getUserService(){
        return userService;
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
