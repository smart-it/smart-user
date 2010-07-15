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
 * @author russel
 */
public class SecuredObject extends AbstractPersistentDTO<SecuredObject>{

    private String objectID;
    private String parentObjectID;

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getParentObjectID() {
        return parentObjectID;
    }

    public void setParentObjectID(String parentObjectID) {
        this.parentObjectID = parentObjectID;
    }

    public boolean isValid(){
        return StringUtils.isNotBlank(objectID) && StringUtils.isNotBlank(parentObjectID);
    }

}
