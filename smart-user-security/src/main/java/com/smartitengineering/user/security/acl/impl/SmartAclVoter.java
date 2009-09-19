/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security.acl.impl;

import org.springframework.security.acls.Permission;
import java.util.Iterator;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.acls.Acl;
import org.springframework.security.acls.AclService;
import org.springframework.security.acls.NotFoundException;
import org.springframework.security.acls.objectidentity.ObjectIdentity;
import org.springframework.security.acls.objectidentity.ObjectIdentityRetrievalStrategy;
import org.springframework.security.acls.sid.Sid;
import org.springframework.security.acls.sid.SidRetrievalStrategy;
import org.springframework.security.vote.AbstractAclVoter;
import org.springframework.security.vote.AccessDecisionVoter;

/**
 *
 * @author modhu7
 */
public class SmartAclVoter extends AbstractAclVoter {

    private AclService aclService;
    private ConfigAttribute processConfigAttribute;
    private Permission[] requirePermission;
    private ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy;
    private SidRetrievalStrategy sidRetrievalStrategy;

    public ObjectIdentityRetrievalStrategy getObjectIdentityRetrievalStrategy() {
        return objectIdentityRetrievalStrategy;
    }

    public void setObjectIdentityRetrievalStrategy(ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy) {
        this.objectIdentityRetrievalStrategy = objectIdentityRetrievalStrategy;
    }

    public SidRetrievalStrategy getSidRetrievalStrategy() {
        return sidRetrievalStrategy;
    }

    public void setSidRetrievalStrategy(SidRetrievalStrategy sidRetrievalStrategy) {
        this.sidRetrievalStrategy = sidRetrievalStrategy;
    }

    public AclService getAclService() {
        return aclService;
    }

    public void setAclService(AclService aclService) {
        this.aclService = aclService;
    }

    public ConfigAttribute getProcessConfigAttribute() {
        return processConfigAttribute;
    }

    public void setProcessConfigAttribute(ConfigAttribute processConfigAttribute) {
        this.processConfigAttribute = processConfigAttribute;
    }

    public Permission[] getRequirePermission() {
        return requirePermission;
    }

    public void setRequirePermission(Permission[] requirePermission) {
        this.requirePermission = requirePermission;
    }

    public boolean supports(ConfigAttribute arg0) {
        if(arg0.getAttribute().equals(processConfigAttribute.getAttribute()))
            return true;
        else
            return false;
    }

    public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
        Iterator iter = config.getConfigAttributes().iterator();

        while (iter.hasNext()) {
            ConfigAttribute attr = (ConfigAttribute) iter.next();

            if (!this.supports(attr)) {
                continue;
            }
            // Need to make an access decision on this invocation
            // Attempt to locate the domain object instance to process
            Object domainObject = getDomainObjectInstance(object);

            // If domain object is null, vote to abstain
            if (domainObject == null) {
                return AccessDecisionVoter.ACCESS_ABSTAIN;
            }



            // Obtain the OID applicable to the domain object
            ObjectIdentity objectIdentity = objectIdentityRetrievalStrategy.getObjectIdentity(domainObject);

            // Obtain the SIDs applicable to the principal
            Sid[] sids = sidRetrievalStrategy.getSids(authentication);

            Acl acl;

            try {
                // Lookup only ACLs for SIDs we're interested in
                acl = aclService.readAclById(objectIdentity, sids);
            } catch (NotFoundException nfe) {
                return AccessDecisionVoter.ACCESS_DENIED;
            }

            try {
                if (acl.isGranted(requirePermission,sids, false)) {
                    return AccessDecisionVoter.ACCESS_GRANTED;
                } else {
                    return AccessDecisionVoter.ACCESS_DENIED;
                }
            } catch (NotFoundException nfe) {
                return AccessDecisionVoter.ACCESS_DENIED;
            }
        }

        // No configuration attribute matched, so abstain
        return AccessDecisionVoter.ACCESS_ABSTAIN;
    }

    

}
