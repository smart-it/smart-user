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
        return objectIdentity;
    }

    public void setObjectidentity(SmartObjectIdentity oid) {
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
