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
        if(objectIdentity==null){
            return new SmartObjectIdentity();
        }
        return objectIdentity;
    }

    public void setObjectIdentity(SmartObjectIdentity objectIdentity) {
        if(objectIdentity==null){
            return;
        }
        this.objectIdentity = objectIdentity;
    }


    public String getOwnerUsername() {
        if(ownerUsername==null){
            return "";
        }
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        if(ownerUsername==null){
            return;
        }
        this.ownerUsername = ownerUsername;
    }
}
