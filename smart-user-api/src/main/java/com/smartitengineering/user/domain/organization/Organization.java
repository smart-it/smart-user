/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.domain.organization;

import com.smartitengineering.domain.AbstractPersistentDTO;
import com.smartitengineering.domain.PersistentDTO;
import com.smartitengineering.user.domain.Address;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author modhu7
 */
public class Organization extends AbstractPersistentDTO<Organization>{

    private String name;
    private String uniqueShortName;
    private Address address;

    public Address getAddress() {
        if(address==null){
            address = new Address();
        }
        return address;
    }

    public void setAddress(Address address) {
        if(address==null)
            return;
        this.address = address;
    }

    public String getName() {
        if(name==null){
            name = "";
        }
        return name;
    }

    public void setName(String name) {
        if(name==null)
            return;
        this.name = name;
    }

    public String getUniqueShortName() {
        if(uniqueShortName==null){
            uniqueShortName = "";
        }
        return uniqueShortName;
    }

    public void setUniqueShortName(String uniqueShortName) {
        if(uniqueShortName==null)
            return;
        this.uniqueShortName = uniqueShortName;
    }



    public boolean isValid() {
        if (StringUtils.isEmpty(uniqueShortName)) {
            return false;
        }
        return true;
    }

}
