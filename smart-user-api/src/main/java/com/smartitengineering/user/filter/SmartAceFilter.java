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
public class SmartAceFilter {
    private SmartObjectIdentity objectIdentity;
    private String sidUsername;

    public SmartObjectIdentity getObjectIdentity() {
        if(objectIdentity==null){
            return new SmartObjectIdentity();
        }
        return objectIdentity;
    }

    public void setObjectIdentity(SmartObjectIdentity oid) {
        if(objectIdentity==null){
            return;
        }
        this.objectIdentity = oid;
    }

    public String getSidUsername() {
        if(sidUsername==null){
            return "";
        }
        return sidUsername;
    }

    public void setSidUsername(String sidUsername) {
        if(sidUsername==null){
            return;
        }
        this.sidUsername = sidUsername;
    }

}
