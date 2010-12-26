/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractGenericPersistentDTO;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;


/**
 *
 * @author russel
 */

public class Organization extends AbstractGenericPersistentDTO<Organization, Long, Integer> {


    private String name;

    private String uniqueShortName;
    
    private Address address;
    //private String contactPerson;

    private Date lastModifiedDate;

    public Organization() {
    }
    
    public Organization(String name, String uniqueShortName) {
        this.name = name;
        this.uniqueShortName = uniqueShortName;
    }

    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueShortName() {
        return uniqueShortName;
    }

    
    public void setUniqueShortName(String uniqueShortName) {
        this.uniqueShortName = uniqueShortName;
    }

    @JsonIgnore
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Address getAddress() {
        if(address==null)
            address = new Address();
        return address;
    }

    public void setAddress(Address address) {
        if(address==null)
            return;
        this.address = address;
    }


   


//    public String getContactPerson() {
//        if(contactPerson == null)
//            contactPerson = "";
//        return contactPerson;
//    }
//
//    public void setContactPerson(String contactPerson) {
//        this.contactPerson = contactPerson;
//    }

    @JsonIgnore
    public boolean isValid(){
        return StringUtils.isNotBlank(name) && StringUtils.isNotBlank(uniqueShortName);
    }


}
