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
public class Organization extends AbstractPersistentDTO<Organization> {

    private String organizationName;
    private String address;
    private String contactPerson;

    public String getAddress() {
        if(address == null)
            address="";
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPerson() {
        if(contactPerson == null)
            contactPerson = "";
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getOrganizationName() {
        if(organizationName == null)
            organizationName = "";
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public boolean isValid(){
        return StringUtils.isNotBlank(address) && StringUtils.isNotBlank(organizationName) && StringUtils.isNotBlank(contactPerson);
    }


}
