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
public class SmartAcl extends AbstractPersistentDTO{

    private SmartAcl parentAcl;
    private User owner;
    private SmartObjectIdentity objectIdentity;
    private boolean entriesInheriting;

    public boolean isEntriesInheriting() {
        return entriesInheriting;
    }

    public void setEntriesInheriting(boolean entriesInheriting) {
        this.entriesInheriting = entriesInheriting;
    }

    public SmartObjectIdentity getObjectIdentity() {
        return objectIdentity;
    }

    public void setObjectIdentity(SmartObjectIdentity objectIdentity) {
        this.objectIdentity = objectIdentity;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public SmartAcl getParentAcl() {
        return parentAcl;
    }

    public void setParentAcl(SmartAcl parentAcl) {
        this.parentAcl = parentAcl;
    }

    public boolean isValid() {
        return true;
    }

}
