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
 * @author russel
 */
public class Organization extends AbstractPersistentDTO<Organization> {

    private String name;
    private String uniqueShortName;
    private String address;
    private String contactPerson;

    private Date lastModifiedDate;

    private Organization() {
    }
    
    public Organization(String name, String uniqueShortName) {
        this.name = name;
        this.uniqueShortName = uniqueShortName;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueShortName() {
        return uniqueShortName;
    }

    @JsonIgnore
    public void setUniqueShortName(String uniqueShortName) {
        this.uniqueShortName = uniqueShortName;
    }

    @JsonIgnore
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    @JsonIgnore
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }


    public String getAddress() {
        if(address == null)
            address="";
        return address;
    }

    @JsonIgnore
    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPerson() {
        if(contactPerson == null)
            contactPerson = "";
        return contactPerson;
    }

    @JsonIgnore
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }       

    public boolean isValid(){
        return StringUtils.isNotBlank(address) && StringUtils.isNotBlank(name) && StringUtils.isNotBlank(contactPerson) && StringUtils.isNotBlank(uniqueShortName);
    }


}
