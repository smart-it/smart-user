/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security.acl.impl;

import com.smartitengineering.user.security.domain.SmartAce;
import com.smartitengineering.user.security.domain.SmartAcl;
import com.smartitengineering.user.security.service.SmartAclService;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.acls.AccessControlEntry;
import org.springframework.security.acls.Acl;
import org.springframework.security.acls.NotFoundException;
import org.springframework.security.acls.Permission;
import org.springframework.security.acls.UnloadedSidException;
import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.springframework.security.acls.sid.Sid;

/**
 *
 * @author modhu7
 */
public class AclImpl implements Acl{
    
    private SmartAcl acl;
    private SmartAclService smartAclService;

    public AclImpl(SmartAcl acl) {
        this.acl = acl;
    }

    public AclImpl(SmartAcl acl, SmartAclService smartAclService) {
        this.acl = acl;
        this.smartAclService = smartAclService;
    }

    public SmartAclService getSmartAclService() {
        return smartAclService;
    }

    public void setSmartAclService(SmartAclService smartAclService) {
        this.smartAclService = smartAclService;
    }

    public SmartAcl getAcl() {
        return acl;
    }

    public void setAcl(SmartAcl acl) {
        this.acl = acl;
    }
    
    
    public AccessControlEntry[] getEntries() {
        Set<SmartAce> smartAces = new HashSet(getSmartAclService().getAceEntries(acl));
        Set<AccessControlEntry> aces = new HashSet<AccessControlEntry>();
        for(SmartAce ace : smartAces){
            AceImpl smartAceAdapter = new AceImpl();
            smartAceAdapter.setAce(ace);
            aces.add(smartAceAdapter);
        }
        return (AccessControlEntry[]) aces.toArray();
    }

    public ObjectIdentity getObjectIdentity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Sid getOwner() {
        return new SidImpl(getAcl().getOwner().getUsername());
    }

    public Acl getParentAcl() {
        return new AclImpl(getAcl().getParentAcl());
    }

    public boolean isEntriesInheriting() {
        return getAcl().isEntriesInheriting();
    }

    public boolean isGranted(Permission[] permissions, Sid[] sids, boolean administrativMode) throws NotFoundException, UnloadedSidException {
        return false;
    }

    public boolean isSidLoaded(Sid[] arg0) {
        //Don't know the need of this
        return true;
    }

}
