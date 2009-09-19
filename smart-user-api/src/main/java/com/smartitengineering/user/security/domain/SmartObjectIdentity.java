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
    private Class classType;
    private Integer objectIdentityId;

    public String getOid() {
        if (classType != null && objectIdentityId != null) {
            return classType.getName() +"_" + objectIdentityId.toString();
        }
        return "";
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Class getClassType() {
        return classType;
    }

    public void setClassType(Class classType) {
        this.classType = classType;
    }

    public Integer getObjectIdentityId() {
        return objectIdentityId;
    }

    public void setObjectIdentityId(Integer objectIdentityId) {
        this.objectIdentityId = objectIdentityId;
    }

    public boolean isValid() {
        if (StringUtils.isEmpty(classType.getName()) || objectIdentityId == null) {
            return false;
        } else {
            return true;
        }
    }
}
