/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractPersistentDTO;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author modhu7
 */
public class Privilege extends AbstractPersistentDTO<Privilege> {

    private Organization parentOrganization;

    private String name;
    private String displayName;
    private String shortDescription;
    private SecuredObject securedObject;
    private Integer permissionMask;

    private Integer parentOrganizationID;
    private Integer securedObjectID;

    private Date lastModifiedDate;

    @JsonIgnore
    public SecuredObject getSecuredObject() {
        return securedObject;
    }

    @JsonIgnore
    public void setSecuredObject(SecuredObject securedObject) {
        this.securedObject = securedObject;
    }

    @JsonIgnore
    public Integer getSecuredObjectID() {
        return securedObjectID;
    }

    public void setSecuredObjectID(Integer securedObjectID) {
        this.securedObjectID = securedObjectID;
    }

    @JsonIgnore
    public Organization getParentOrganization() {
        return parentOrganization;
    }

    @JsonIgnore
    public void setParentOrganization(Organization parentOrganization) {
        this.parentOrganization = parentOrganization;
    }

    @JsonIgnore
    public Integer getParentOrganizationID() {
        return parentOrganizationID;
    }

    public void setParentOrganizationID(Integer parentOrganizationID) {
        this.parentOrganizationID = parentOrganizationID;
    }



    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }    

    public Integer getPermissionMask() {
        return permissionMask;
    }

    public void setPermissionMask(Integer permissionMask) {
        this.permissionMask = permissionMask;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public boolean isValid() {
        if (StringUtils.isEmpty(name) || !(permissionMask<0)) {
            return false;
        }
        return true;
    }
}
