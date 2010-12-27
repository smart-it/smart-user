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
public class Organization extends AbstractGenericPersistentDTO<Organization, String, Integer> {

  private String name;
  private Address address;
  //private String contactPerson;
  private Date lastModifiedDate;

  public Organization() {
  }

  public Organization(String name, String uniqueShortName) {
    this.name = name;
    setUniqueShortName(uniqueShortName);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUniqueShortName() {
    return getId();
  }

  public final void setUniqueShortName(String uniqueShortName) {
    setId(uniqueShortName);
  }

  @JsonIgnore
  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  public Address getAddress() {
    if (address == null) {
      address = new Address();
    }
    return address;
  }

  public void setAddress(Address address) {
    if (address == null) {
      return;
    }
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
  @Override
  public boolean isValid() {
    return StringUtils.isNotBlank(name) && StringUtils.isNotBlank(getUniqueShortName());
  }
}
