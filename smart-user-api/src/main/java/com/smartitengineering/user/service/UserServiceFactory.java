/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service;

import com.smartitengineering.util.bean.BeanFactoryRegistrar;
import com.smartitengineering.util.bean.annotations.Aggregator;
import com.smartitengineering.util.bean.annotations.InjectableField;


/**
 *
 * @author imyousuf
 */
@Aggregator(contextName = "com.smartitengineering.user.service")
public final class UserServiceFactory {

    private static UserServiceFactory userServiceFactory;

    public static UserServiceFactory getInstance() {
        if (userServiceFactory == null) {
            userServiceFactory = new UserServiceFactory();
            BeanFactoryRegistrar.aggregate(userServiceFactory);
        }
        return userServiceFactory;
    }

    private UserServiceFactory() {
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
