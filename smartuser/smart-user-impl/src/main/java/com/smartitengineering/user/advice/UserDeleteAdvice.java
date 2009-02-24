/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.advice;

import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.service.UserPersonService;
import java.lang.reflect.Method;
import org.springframework.aop.MethodBeforeAdvice;

/**
 *
 * @author modhu7
 */
public class UserDeleteAdvice implements MethodBeforeAdvice {

    private UserPersonService userPersonService;

    public void before(Method method, Object[] parameters, Object instance)
            throws Throwable {
        if (parameters != null && parameters.length > 0 &&
                parameters[0] instanceof User && method.getName().contains(
                "delete")) {
            User user = (User) parameters[0];
            userPersonService.deleteByUser(user);
        }
    }

    public UserPersonService getUserPersonService() {
        return userPersonService;
    }

    public void setUserPersonService(UserPersonService userPersonService) {
        this.userPersonService = userPersonService;
    }
}
