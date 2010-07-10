/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractPersistentDTO;
import com.smartitengineering.domain.PersistentDTO;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author modhu7
 */
class Permission extends AbstractPersistentDTO<Permission>{

    private String oid;
    private PermissionField permissionField;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public PermissionField getPermissionField() {
        return permissionField;
    }

    public void setPermissionField(PermissionField permissionField) {
        this.permissionField = permissionField;
    }



    public boolean isValid() {
        if(StringUtils.isEmpty(oid) || permissionField==null)
            return false;
        else
            return true;
    }

}
