/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security.acl.impl;

import com.smartitengineering.user.filter.SmartAceFilter;
import com.smartitengineering.user.security.acl.UserSid;
import com.smartitengineering.user.security.domain.SmartAce;
import com.smartitengineering.user.security.domain.SmartAcl;
import com.smartitengineering.user.security.domain.SmartObjectIdentity;
import com.smartitengineering.user.security.service.SmartAceService;
import com.smartitengineering.user.security.service.SmartAclService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public class AclImpl implements Acl {

    private SmartAcl acl;
    private SmartAclService smartAclService;
    private SmartAceService smartAceService;
    private ObjectIdentity objectIdentity;

    public void setObjectIdentity(ObjectIdentity objectIdentity) {
        this.objectIdentity = objectIdentity;
    }

    public SmartAceService getSmartAceService() {
        return smartAceService;
    }

    public void setSmartAceService(SmartAceService smartAceService) {
        this.smartAceService = smartAceService;
    }

    public AclImpl(SmartAcl acl) {
        this.acl = acl;
        this.objectIdentity= new ObjectIdentityImpl(acl.getObjectIdentity());
    }

    public AclImpl(SmartAcl acl, SmartAclService smartAclService) {
        this.acl = acl;
        this.smartAclService = smartAclService;
        this.objectIdentity= new ObjectIdentityImpl(acl.getObjectIdentity());
    }

    public AclImpl(SmartAcl acl, SmartAclService smartAclService, SmartAceService smartAceService) {
        this.acl = acl;
        this.smartAclService = smartAclService;
        this.smartAceService = smartAceService;
        this.objectIdentity= new ObjectIdentityImpl(acl.getObjectIdentity());
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
        for (SmartAce ace : smartAces) {
            AceImpl smartAceAdapter = new AceImpl(this);
            smartAceAdapter.setAce(ace);
            aces.add(smartAceAdapter);
        }
        return (AccessControlEntry[]) aces.toArray();
    }

    public ObjectIdentity getObjectIdentity() {
        return objectIdentity;
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
        
        for (int i = 0; i < permissions.length; i++) {
            for (int x = 0; x < sids.length; x++) {
                if(getAcl()==null)
                    System.out.println("acl is null");
                else
                    System.out.println(getAcl().getObjectIdentity().getOid());
                if(getObjectIdentity()==null)
                    System.out.println("object identity is null");
                if(getObjectIdentity().getJavaType()==null)
                    System.out.println("class type is null");
                System.out.println("This is from is granted of acl: " + getObjectIdentity().getJavaType().getName());
                System.out.println("This is from is granted of acl: " + getObjectIdentity().getIdentifier());
                SmartAceFilter filter = new SmartAceFilter();
                SmartObjectIdentity objectIdentity = new SmartObjectIdentity();
                objectIdentity.setClassType(getObjectIdentity().getJavaType());
                objectIdentity.setObjectIdentityId((Integer) getObjectIdentity().getIdentifier());
                filter.setOid(objectIdentity.getOid());                
                filter.setSidUsername(((UserSid) sids[x]).getUsername());
                System.out.println("from acl is granted: filter oid " + filter.getOid() + " filter user " + filter.getSidUsername());
                List<SmartAce> aceList = new ArrayList<SmartAce>(getSmartAceService().search(filter));
                if(aceList==null){
                    System.out.println("ace List is null");
                }
                if (aceList != null && aceList.size()!=0) {
                    SmartAce ace = aceList.get(0);
                    if ((ace.getPermissionMask() | permissions[i].getMask()) == ace.getPermissionMask()) {
                        if (ace.isGranting()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isSidLoaded(Sid[] arg0) {
        //Don't know the need of this
        return true;
    }
}
