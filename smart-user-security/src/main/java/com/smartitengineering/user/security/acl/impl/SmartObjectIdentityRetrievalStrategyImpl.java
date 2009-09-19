/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security.acl.impl;

import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.springframework.security.acls.objectidentity.ObjectIdentityRetrievalStrategy;

/**
 *
 * @author modhu7
 */
public class SmartObjectIdentityRetrievalStrategyImpl implements ObjectIdentityRetrievalStrategy{

    public ObjectIdentity getObjectIdentity(Object domainObject) {
        return new ObjectIdentityImpl(domainObject);
    }

}
