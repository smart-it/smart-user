/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security;

import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.InsufficientAuthenticationException;
import org.springframework.security.intercept.web.FilterInvocation;
import org.springframework.security.vote.AbstractAccessDecisionManager;

/**
 *
 * @author modhu7
 */
public class SmartAccessDecisionManager extends AbstractAccessDecisionManager{

    @Override
    public void decide(Authentication a, Object o, ConfigAttributeDefinition cad) throws AccessDeniedException, InsufficientAuthenticationException {
        System.out.println(o.getClass().getCanonicalName());
        System.out.println(((FilterInvocation)o).getFullRequestUrl());
        System.out.println(((FilterInvocation)o).getRequestUrl());
        return;
//        throw new AccessDeniedException(messages.getMessage("AbstractAccessDecisionManager.accessDenied",
//                            "Access is denied"));
    }

}
