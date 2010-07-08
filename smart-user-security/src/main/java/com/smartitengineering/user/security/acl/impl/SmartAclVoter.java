/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security.acl.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.security.acls.Permission;
import java.util.Iterator;
import java.util.List;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.security.Authentication;
import org.springframework.security.AuthorizationServiceException;
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
import org.springframework.security.vote.AccessDecisionVoter;

/**
 *
 * @author modhu7
 */
public class SmartAclVoter implements AccessDecisionVoter {

    private AclService aclService;   
    private ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy;
     private SidRetrievalStrategy sidRetrievalStrategy;
    private VotingConfigProvider votingConfigProvider;
    private ParentProvider parentProvider;

    public ParentProvider getParentProvider() {
        return parentProvider;
    }

    public void setParentProvider(ParentProvider parentProvider) {
        this.parentProvider = parentProvider;
    }

    public VotingConfigProvider getVotingConfigProvider() {
        return votingConfigProvider;
    }

    public void setVotingConfigProvider(VotingConfigProvider votingConfigProvider) {
        this.votingConfigProvider = votingConfigProvider;
    }
   

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

    public boolean supports(ConfigAttribute arg0) {
        for(VotingConfig votingConfig : votingConfigProvider.getVotingConfigList()){
            if(arg0.getAttribute().equals(votingConfig.getProcessConfigAttribute().getAttribute()))
                return true;
        }
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


            return authorize(authentication, object, getVotingConfig(attr));

        }

        // No configuration attribute matched, so abstain
        return AccessDecisionVoter.ACCESS_ABSTAIN;
    }

    private int authorize(Authentication authentication, Object object, VotingConfig votingConfig) {

        Object domainObject = getDomainObjectInstance(object , votingConfig);

        if (votingConfig.getRequirePermission()[0].equals(BasePermission.CREATE)) {
            Permission[] permissions = new Permission[1];
            permissions[0] = BasePermission.WRITE;
            return authorizeByParent(authentication, permissions, domainObject);
        }

        // If domain object is null, vote to abstain
        if (domainObject == null) {
            System.out.println("Object is null at authorize");
            return AccessDecisionVoter.ACCESS_ABSTAIN;
        }

        // Obtain the OID applicable to the domain object
        return authorizeByAcl(authentication, votingConfig.getRequirePermission(), domainObject);
    }

    private int authorizeByAcl(Authentication authentication, Permission[] requirePermission, Object domainObject) {
        ObjectIdentity objectIdentity = objectIdentityRetrievalStrategy.getObjectIdentity(domainObject);

        // Obtain the SIDs applicable to the principal
        Sid[] sids = sidRetrievalStrategy.getSids(authentication);

        Acl acl;

        try {
            // Lookup only ACLs for SIDs we're interested in

            if (aclService.readAclById(objectIdentity, sids) != null) {
                acl = aclService.readAclById(objectIdentity, sids);
                AclImpl aclImpl = (AclImpl) acl;
                System.out.println("this is from smartaclvoter "+aclImpl.getAcl().getObjectIdentity().getOid());
                
                try {
                    if (acl.isGranted(requirePermission, sids, false)) {
                        return AccessDecisionVoter.ACCESS_GRANTED;
                    } else {
                        System.out.println("Not Authorized directly.......... Checking with parents authorization");
                        return authorizeByParent(authentication, requirePermission, domainObject);
                    }
                } catch (NotFoundException nfe) {
                    return AccessDecisionVoter.ACCESS_DENIED;
                }
            }
        } catch (NotFoundException nfe) {
            return AccessDecisionVoter.ACCESS_DENIED;
        }
        return AccessDecisionVoter.ACCESS_DENIED;

    }

    private int authorizeByParent(Authentication authentication, Permission[] requirePermission, Object domainObject) {
        System.out.println("Parent Search is running  " + domainObject.getClass().getName() + " permission : " + requirePermission[0].getMask());
        List<String> listParent = parentProvider.getParent(domainObject.getClass().getName());
        for (String parent : listParent) {
            System.out.println("Parent is: " + parent);
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

    public boolean supports(Class clazz) {
        if (MethodInvocation.class.isAssignableFrom(clazz)) {
            return true;
        } else if (JoinPoint.class.isAssignableFrom(clazz)) {
            return true;
        } else {
            return false;
        }
    }

    private Object getDomainObjectInstance(Object secureObject, VotingConfig votingConfig) {
        Object[] args;
        Class[] params;

        if (secureObject instanceof MethodInvocation) {
            MethodInvocation invocation = (MethodInvocation) secureObject;
            params = invocation.getMethod().getParameterTypes();
            args = invocation.getArguments();
        } else {
            JoinPoint jp = (JoinPoint) secureObject;
            params = ((CodeSignature) jp.getStaticPart().getSignature()).getParameterTypes();
            args = jp.getArgs();
        }

        for (int i = 0; i < params.length; i++) {
            if (votingConfig.getProcessDomainObjectClass().isAssignableFrom(params[i])) {
                return args[i];
            }
        }

        throw new AuthorizationServiceException("Secure object: " + secureObject
            + " did not provide any argument of type: " + votingConfig.getProcessDomainObjectClass());
    }

    private VotingConfig getVotingConfig(ConfigAttribute attr) {
        for(VotingConfig votingConfig : votingConfigProvider.getVotingConfigList()){
            if(votingConfig.getProcessConfigAttribute().getAttribute().equals(attr.getAttribute()))
                return votingConfig;
        }
        return new VotingConfig();
    }
}
