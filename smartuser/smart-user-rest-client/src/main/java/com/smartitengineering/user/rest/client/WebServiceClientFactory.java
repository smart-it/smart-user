/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.UserPersonService;
import com.smartitengineering.user.service.UserService;

/**
 *
 * @author imyousuf
 */
public final class WebServiceClientFactory {
    
    private static PersonService personService; 
    private static UserServiceClientImpl userService;
    
    private WebServiceClientFactory() {
        throw new AssertionError();
    }

    public static UserService getUserService() {
        if(userService == null) {
            userService = new UserServiceClientImpl();
        }
        return userService;
    }

    public static UserPersonService getUserPersonService() {
        if(userService == null) {
            userService = new UserServiceClientImpl();
        }
        return userService;
    }
    
    public static PersonService getPersonService() {
        if(personService == null) {
            personService = new PersonServiceClientImpl();
        }
        return personService;
    }
}
