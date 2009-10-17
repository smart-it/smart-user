/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security.acl.impl;

import com.smartitengineering.user.parser.ParentParser;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.security.acls.Permission;
import java.util.Iterator;
import java.util.List;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.acls.Acl;
import org.springframework.security.acls.AclService;
import org.springframework.security.acls.NotFoundException;
import org.springframework.security.acls.domain.BasePermission;
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
        if (arg0.getAttribute().equals(processConfigAttribute.getAttribute())) {
            return true;
        } else {
            return false;
        }
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


            return authorize(authentication, object);

        }

        // No configuration attribute matched, so abstain
        return AccessDecisionVoter.ACCESS_ABSTAIN;
    }

    private int authorize(Authentication authentication, Object object) {

        if (getRequirePermission()[0].equals(BasePermission.CREATE)) {
            Permission[] permissions = new Permission[1];
            permissions[0] = BasePermission.WRITE;
            return authorizeByParent(authentication, permissions , object);
        }

        Object domainObject = getDomainObjectInstance(object);

        // If domain object is null, vote to abstain
        if (domainObject == null) {
            return AccessDecisionVoter.ACCESS_ABSTAIN;
        }

        // Obtain the OID applicable to the domain object
        return authorizeByAcl(authentication, getRequirePermission(), domainObject);
    }

    private int authorizeByAcl(Authentication authentication, Permission[] requirePermission, Object domainObject) {
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
            if (acl.isGranted(requirePermission, sids, false)) {
                return AccessDecisionVoter.ACCESS_GRANTED;
            } else {
                return authorizeByParent(authentication, requirePermission, domainObject);
            }
        } catch (NotFoundException nfe) {
            return AccessDecisionVoter.ACCESS_DENIED;
        }
    }

    private int authorizeByParent(Authentication authentication, Permission[] requirePermission, Object domainObject) {
        List<String> listParent = ParentParser.getParentMethodName(domainObject.getClass().getName());
        for (String parent : listParent) {
            try {
                Class objectClass = Class.forName(domainObject.getClass().getName());
                Class[] partypes = new Class[]{};
                Method method = objectClass.getMethod(parent, partypes);
                Object[] args = new Object[]{};
                Object parentObject = method.invoke(domainObject, args);
                if (parentObject != null) {
                    return authorizeByAcl(authentication, requirePermission, parentObject);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return AccessDecisionVoter.ACCESS_DENIED;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return AccessDecisionVoter.ACCESS_DENIED;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return AccessDecisionVoter.ACCESS_DENIED;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return AccessDecisionVoter.ACCESS_DENIED;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return AccessDecisionVoter.ACCESS_DENIED;
            }
        }
        return AccessDecisionVoter.ACCESS_DENIED;
    }
}
