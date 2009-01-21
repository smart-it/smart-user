/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service;

import com.smartitengineering.util.spring.BeanFactoryRegistrar;
import com.smartitengineering.util.spring.annotations.Aggregator;
import com.smartitengineering.util.spring.annotations.InjectableField;

/**
 *
 * @author imyousuf
 */
@Aggregator(contextName="userServiceContext")
public final class UserServiceFactory {

    private static UserServiceFactory userServiceFactory;
    
    public static UserServiceFactory getInstance() {
        if(userServiceFactory == null) {
            userServiceFactory = new UserServiceFactory();
        }
        return userServiceFactory;
    }

    private UserServiceFactory() {
        BeanFactoryRegistrar.aggregate(this);
    }
    
    @InjectableField
    private UserService userService;
    @InjectableField
    private PersonService personService;
    
    public PersonService getPersonService() {
        return personService;
    }    

    public UserService getUserService() {
        return userService;
    }
    
}
