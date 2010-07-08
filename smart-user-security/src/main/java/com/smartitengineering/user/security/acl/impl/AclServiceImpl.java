/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security.acl.impl;

import com.smartitengineering.user.filter.SmartAclFilter;
import com.smartitengineering.user.security.domain.SmartAcl;
import com.smartitengineering.user.security.domain.SmartObjectIdentity;
import com.smartitengineering.user.security.service.SmartAceService;
import com.smartitengineering.user.security.service.SmartAclService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.acls.Acl;
import org.springframework.security.acls.AclService;
import org.springframework.security.acls.NotFoundException;
import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.springframework.security.acls.sid.Sid;

/**
 *
 * @author modhu7
 */
public class AclServiceImpl implements AclService {

    private SmartAclService smartAclService;
    private SmartAceService smartAceService;

    public SmartAceService getSmartAceService() {
        return smartAceService;
    }

    public void setSmartAceService(SmartAceService smartAceService) {
        this.smartAceService = smartAceService;
    }

    public SmartAclService getSmartAclService() {
        return smartAclService;
    }

    public void setSmartAclService(SmartAclService smartAclService) {
        this.smartAclService = smartAclService;
    }

    public ObjectIdentity[] findChildren(ObjectIdentity arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Acl readAclById(ObjectIdentity objectIdentity) throws NotFoundException {
        return readAclById(objectIdentity, null);

    }

    public Acl readAclById(ObjectIdentity objectIdentity, Sid[] sids) throws NotFoundException {
        System.out.println("From Acl service impl: read Acl by ID");
        System.out.println(objectIdentity.getJavaType().getName());
        System.out.println(sids[0].toString());
        Map map = readAclsById(new ObjectIdentity[]{objectIdentity}, sids);
        return (Acl) map.get(objectIdentity);
    }

    public Map readAclsById(ObjectIdentity[] objectIdentities) throws NotFoundException {
        return readAclsById(objectIdentities, null);
    }

    //for now i m not using the sids
    public Map readAclsById(ObjectIdentity[] objects, Sid[] sids) throws NotFoundException {
        Map<ObjectIdentity, Acl> result = new HashMap<ObjectIdentity, Acl>();
        for (int i = 0; i < objects.length; i++) {
            SmartAclFilter filter = new SmartAclFilter();
            SmartObjectIdentity objectIdentity = new SmartObjectIdentity();
            objectIdentity.setClassType(objects[i].getJavaType());
            objectIdentity.setObjectIdentityId((Integer) objects[i].getIdentifier());
            filter.setOid(objectIdentity.getOid());            
            List<SmartAcl> aclList = new ArrayList<SmartAcl>(getSmartAclService().search(filter));            
            if(aclList!=null && aclList.size() != 0){
                System.out.println("Result of acl list reading "+ aclList.size());
                System.out.println(aclList.get(0).getObjectIdentity().getOid());
                
                Acl acl = new AclImpl(aclList.get(0), getSmartAclService(), getSmartAceService());
                
                result.put(objects[i], acl);
            }
        }
        return result;
    }
}
