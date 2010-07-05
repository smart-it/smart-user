/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security.domain;

import com.smartitengineering.domain.AbstractPersistentDTO;
import com.smartitengineering.user.domain.User;
/**
 *
 * @author modhu7
 */
public class SmartAce extends AbstractPersistentDTO{

    private User sid;
    private int permissionMask;
    private SmartAcl acl;
    private boolean granting;

    public boolean isGranting() {
        return granting;
    }

    public void setGranting(boolean granting) {
        this.granting = granting;
    }

    public SmartAcl getAcl() {
        return acl;
    }

    public void setAcl(SmartAcl acl) {
        this.acl = acl;
    }
    

    public int getPermissionMask() {
        return permissionMask;
    }

    public void setPermissionMask(int permissionMask) {
        this.permissionMask = permissionMask;
    }

    public User getSid() {
        return sid;
    }

    public void setSid(User sid) {
        this.sid = sid;
    }

    public boolean isValid() {
        if(sid.isValid() && acl.isValid())
            return true;
        else
            return false;
    }
}
