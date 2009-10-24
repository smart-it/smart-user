/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security.acl.impl;

import org.springframework.security.ConfigAttribute;
import org.springframework.security.acls.Permission;

/**
 *
 * @author modhu7
 */
public class VotingConfig {
    private ConfigAttribute processConfigAttribute;
    private Permission[] requirePermission;
    private Class processDomainObjectClass;

    public Class getProcessDomainObjectClass() {
        return processDomainObjectClass;
    }

    public void setProcessDomainObjectClass(Class processDomainObjectClass) {
        this.processDomainObjectClass = processDomainObjectClass;
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

}
