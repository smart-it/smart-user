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

    private String oid;
    private String ownerUsername;

    public String getOid() {
        if(oid==null){
            return "";
        }
        return oid;
    }

    public void setOid(String oid) {
        if(oid==null)
            return;
        this.oid = oid;
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
