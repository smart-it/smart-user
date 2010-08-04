/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractPersistentDTO;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
     * @author russel
 */
public class UserGroup extends AbstractPersistentDTO<UserGroup>{

    private String name;
    private Set<User> users;
    private Set<Privilege> privileges;
    private Organization organization;

    private List<Integer> userIDs;
    private List<Integer> privilegeIDs;
    private Integer parentOrganizationID;

    private Date lastModifiedDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public List<Integer> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<Integer> userIDs) {
        this.userIDs = userIDs;
    }

    @JsonIgnore
    public List<Integer> getPrivilegeIDs() {
        return privilegeIDs;
    }

    public void setPrivilegeIDs(List<Integer> privilegeIDs) {
        this.privilegeIDs = privilegeIDs;
    }


    @JsonIgnore
    public Set<User> getUsers() {
        return users;
    }

    @JsonIgnore
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @JsonIgnore
    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    @JsonIgnore
    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public boolean isValid(){
        return true;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Integer getParentOrganizationID() {
        return parentOrganizationID;
    }

    public void setParentOrganizationID(Integer parentOrganizationID) {
        this.parentOrganizationID = parentOrganizationID;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    

}
