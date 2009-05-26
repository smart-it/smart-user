/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.service.UserPersonService;
import com.smartitengineering.user.service.UserService;

/**
 *
 * @author imyousuf
 */
public final class WebServiceClientFactory {

    private static PersonService personService;
    private static UserService userService;
    private static UserPersonService userPersonService;
    private static RoleService roleService;
    private static PrivilegeService privilegeService;

    private WebServiceClientFactory() {
        throw new AssertionError();
    }

    public static UserService getUserService() {
        if (userService == null) {
            userService = new UserServiceClientImpl();
        }
        return userService;
    }

    public static UserPersonService getUserPersonService() {
        if (userPersonService == null) {
            userPersonService = new UserPersonServiceClientImpl();
        }
        return userPersonService;
    }

    public static PersonService getPersonService() {
        if (personService == null) {
            personService = new PersonServiceClientImpl();
        }
        return personService;
    }

    public static RoleService getRoleService() {
        if (roleService == null) {
            roleService = new RoleServiceClientImpl();
        }
        return roleService;
    }

    public static PrivilegeService getPrivilegeService() {
        if (privilegeService == null) {
            privilegeService = new PrivilegeServiceClientImpl();
        }
        return privilegeService;
    }
}
