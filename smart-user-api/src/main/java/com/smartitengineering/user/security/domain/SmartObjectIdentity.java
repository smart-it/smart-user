/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security.domain;

import com.smartitengineering.domain.AbstractPersistentDTO;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author modhu7
 */
public class SmartObjectIdentity extends AbstractPersistentDTO {

    private String oid;
    private String className;
    private Integer objectIdentityId;

    public String getOid() {
        if (className != null && objectIdentityId != null) {
            return className +"_" + objectIdentityId.toString();
        }
        return "";
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getObjectIdentityId() {
        return objectIdentityId;
    }

    public void setObjectIdentityId(Integer objectIdentityId) {
        this.objectIdentityId = objectIdentityId;
    }

    public boolean isValid() {
        if (StringUtils.isEmpty(className) || objectIdentityId == null) {
            return false;
        } else {
            return true;
        }
    }
}
