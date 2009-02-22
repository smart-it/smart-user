/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.advice;

import com.smartitengineering.dao.common.queryparam.QueryParameter;
import com.smartitengineering.dao.common.queryparam.QueryParameterFactory;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.service.UserPersonService;
import java.lang.reflect.Method;
import org.springframework.aop.MethodBeforeAdvice;

/**
 *
 * @author modhu7
 */
public class PersonDeleteAdvice implements MethodBeforeAdvice {
    
    private UserPersonService userPersonService;
    
    public void before(Method method, Object[] parameters, Object instance) 
            throws Throwable {
        if(parameters != null && parameters.length > 0 
                && parameters[0] instanceof Person 
                && method.getName().contains("delete")) {
            Person person = (Person) parameters[0];
            userPersonService.deleteByPerson(person);
        }
    }

    public UserPersonService getUserPersonService() {
        return userPersonService;
    }

    public void setUserPersonService(UserPersonService userPersonService) {
        this.userPersonService = userPersonService;
    }
}
