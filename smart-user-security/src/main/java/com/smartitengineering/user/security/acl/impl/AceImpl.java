/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security.acl.impl;

import com.smartitengineering.user.security.domain.SmartAce;
import java.io.Serializable;
import org.springframework.security.acls.AccessControlEntry;
import org.springframework.security.acls.Acl;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.sid.Sid;

/**
 *
 * @author modhu7
 */
public class AceImpl implements AccessControlEntry{

    private SmartAce ace;

    public SmartAce getAce() {
        return ace;
    }

    public void setAce(SmartAce ace) {
        this.ace = ace;
    }

    public Acl getAcl() {
        return new AclImpl(getAce().getAcl());
    }

    public Permission getPermission() {
        return BasePermission.buildFromMask(getAce().getPermissionMask());
    }

    public Sid getSid() {
        return new SidImpl(getAce().getSid().getUsername());
    }

    public boolean isGranting() {
        return getAce().isGranting();
    }

    public Serializable getId() {
        return getAce().getId();
    }

}
