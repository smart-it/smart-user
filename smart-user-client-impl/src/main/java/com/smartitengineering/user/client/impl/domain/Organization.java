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

public class Organization extends AbstractClientDomain implements
    com.smartitengineering.user.client.api.Organization {

  private String name;
  private String uniqueShortName;
  private Address address;
  private Date lastModifiedDate;

  @Override
  public String getName() {
    return name;
  }

  public void setAddress(Address address) {
    if (address == null) {
      return;
    }
    this.address = address;
  }

  public void setName(String name) {
    if (name == null) {
      return;
    }
    this.name = name;
  }

  public void setUniqueShortName(String uniqueShortName) {
    if (uniqueShortName == null) {
      return;
    }
    this.uniqueShortName = uniqueShortName;
  }

  @Override
  public String getUniqueShortName() {
    return uniqueShortName;
  }

  @Override
  public Address getAddress() {
    return address;
  }

  @Override
  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    if(lastModifiedDate==null)
      return;
    this.lastModifiedDate = lastModifiedDate;
  }
}
