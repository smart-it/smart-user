/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.filter;

import com.smartitengineering.user.security.domain.SmartObjectIdentity;

/**
 *
 * @author modhu7
 */
public class SmartAclFilter {

    private SmartObjectIdentity objectIdentity;
    private String ownerUsername;

    public SmartObjectIdentity getObjectIdentity() {
        return objectIdentity;
    }

    public void setObjectIdentity(SmartObjectIdentity objectIdentity) {
        this.objectIdentity = objectIdentity;
    }


    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
}
