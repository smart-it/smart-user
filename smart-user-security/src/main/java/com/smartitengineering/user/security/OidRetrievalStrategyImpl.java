/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security;

import org.springframework.security.intercept.web.FilterInvocation;

/**
 *
 * @author modhu7
 */
public class OidRetrievalStrategyImpl implements OidRetrievalStrategy{

    @Override
    public String getOid(Object object) {
        if(object instanceof FilterInvocation){
            return ((FilterInvocation)object).getRequestUrl();
        }
        return "";
    }

}
