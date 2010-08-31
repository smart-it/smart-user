/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl.domain;

/**
 *
 * @author russel
 */

import java.util.Date;


public class Organization implements com.smartitengineering.smartuser.client.api.Organization{

  private String name;

    private String uniqueShortName;

    private Address address;
    //private String contactPerson;

    private Date lastModifiedDate;

  @Override
  public String getName() {
    return name;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setUniqueShortName(String uniqueShortName) {
    this.uniqueShortName = uniqueShortName;
  }
  

  @Override
  public String getUniqueShortName() {
    return uniqueShortName;
  }

  @Override
  public com.smartitengineering.smartuser.client.api.Address getAddress() {
    return address;
  }

  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

}
