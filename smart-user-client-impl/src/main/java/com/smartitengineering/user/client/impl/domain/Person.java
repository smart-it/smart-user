/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl.domain;

import com.smartitengineering.smartuser.client.api.Address;
import java.util.Date;

/**
 *
 * @author modhu7
 */
public class Person extends AbstractClientDomain implements com.smartitengineering.smartuser.client.api.Person {

  private BasicIdentity father;
  private BasicIdentity mother;
  private BasicIdentity spouse;
  private BasicIdentity self;
  private Address address;
  private Date birthday;
  private String primaryEmail;
  private String secondaryEmail;
  private String phoneNumber;
  private String cellPhoneNumber;
  private String faxNumber;
  private Date lastModifiedDate;

  @Override
  public BasicIdentity getFather() {
    return father;
  }

  @Override
  public BasicIdentity getMother() {
    return mother;
  }

  @Override
  public BasicIdentity getSpouse() {
    return spouse;
  }

  @Override
  public BasicIdentity getSelf() {
    return self;
  }

  @Override
  public Address getAddress() {
    return address;
  }

  @Override
  public Date getBirthday() {
    return birthday;
  }

  @Override
  public String getPrimaryEmail() {
    return primaryEmail;
  }

  @Override
  public String getSecondaryEmail() {
    return secondaryEmail;
  }

  @Override
  public String getPhoneNumber() {
    return phoneNumber;
  }

  @Override
  public String getCellPhoneNumber() {
    return cellPhoneNumber;
  }

  @Override
  public String getFaxNumber() {
    return faxNumber;
  }

  public void setAddress(Address address) {
    if (address == null) {
      return;
    }
    this.address = address;
  }

  public void setBirthday(Date birthday) {
    if (birthday == null) {
      return;
    }
    this.birthday = birthday;
  }

  public void setCellPhoneNumber(String cellPhoneNumber) {
    if (cellPhoneNumber == null) {
      return;
    }
    this.cellPhoneNumber = cellPhoneNumber;
  }

  public void setFather(BasicIdentity father) {
    if (father == null) {
      return;
    }
    this.father = father;
  }

  public void setFaxNumber(String faxNumber) {
    if (faxNumber == null) {
      return;
    }
    this.faxNumber = faxNumber;
  }

  public void setMother(BasicIdentity mother) {
    if (mother == null) {
      return;
    }
    this.mother = mother;
  }

  public void setPhoneNumber(String phoneNumber) {
    if (phoneNumber == null) {
      return;
    }
    this.phoneNumber = phoneNumber;
  }

  public void setPrimaryEmail(String primaryEmail) {
    if (primaryEmail == null) {
      return;
    }
    this.primaryEmail = primaryEmail;
  }

  public void setSecondaryEmail(String secondaryEmail) {
    if (secondaryEmail == null) {
      return;
    }
    this.secondaryEmail = secondaryEmail;
  }

  public void setSelf(BasicIdentity self) {
    if (self == null) {
      return;
    }
    this.self = self;
  }

  public void setSpouse(BasicIdentity spouse) {
    if (spouse == null) {
      return;
    }
    this.spouse = spouse;
  }

  @Override
  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    if (lastModifiedDate == null) {
      return;
    }
    this.lastModifiedDate = lastModifiedDate;
  }

}
