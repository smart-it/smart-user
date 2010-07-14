/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractPersistentDTO;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author modhu7
 */
public class Role extends AbstractPersistentDTO<Role> {

    private String name;
    private String displayName;
    private String shortDescription;
    private Set<Privilege> privileges;
    private Set<Role> roles;
    private Date lastModifiedDate;

    public Role(){

    }

    public Role(String name, String displayName, String shortDescription){
        this.name = name;
        this.displayName = displayName;
        this.shortDescription = shortDescription;
    }



    public Set<Role> getRoles() {
        if(roles == null){
            roles = new HashSet<Role>();
        }
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        if(roles==null){
            return;
        }
        this.roles = roles;
    }
    

    public Set<Privilege> getPrivileges() {
        if (privileges == null) {
            privileges = new HashSet<Privilege>();
        }
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        if (privileges == null) {
            return;
        }
        this.privileges = privileges;
    }   



    public String getDisplayName() {
        if (displayName == null) {
            displayName = "";
        }
        return displayName;
    }

    public void setDisplayName(String displayName) {
        if (displayName == null) {
            return;
        }
        this.displayName = displayName;
    }

    public String getName() {
        if (name == null) {
            name = "";
        }
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            return;
        }
        this.name = name;
    }

    public String getShortDescription() {
        if (shortDescription == null) {
            shortDescription = "";
        }
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        if (shortDescription == null) {
            return;
        }
        this.shortDescription = shortDescription;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public boolean isValid() {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(displayName)) {
            return false;
        }
        return true;
    }
}
