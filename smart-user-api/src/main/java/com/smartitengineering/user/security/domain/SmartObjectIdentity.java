/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security.domain;

import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author modhu7
 */
public class SmartObjectIdentity{

    private String oid;
    private Class classType;
    private Integer objectIdentityId;

    public String getOid() {
        if (classType != null && objectIdentityId != null) {
            return classType.getName() +"_" + objectIdentityId.toString();
        }else if(oid!=null){
            return oid;
        }else
            return "";
    }

    public void setOid(String oid) throws ClassNotFoundException {
        this.oid = oid;
        StringTokenizer st = new StringTokenizer(oid, "_");
        setClassType(Class.forName(st.nextToken()));
        setObjectIdentityId(Integer.getInteger(st.nextToken()));
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
