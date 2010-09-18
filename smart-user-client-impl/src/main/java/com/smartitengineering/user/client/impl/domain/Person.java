/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl.domain;

import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 *
 * @author modhu7
 */
public class Person extends AbstractClientDomain implements com.smartitengineering.user.client.api.Person {

  private com.smartitengineering.user.client.api.BasicIdentity father;
  private com.smartitengineering.user.client.api.BasicIdentity mother;
  private com.smartitengineering.user.client.api.BasicIdentity spouse;
  private com.smartitengineering.user.client.api.BasicIdentity self;
  private com.smartitengineering.user.client.api.Address address;
  private Date birthday;
  private String primaryEmail;
  private String secondaryEmail;
  private String phoneNumber;
  private String cellPhoneNumber;
  private String faxNumber;

  @Override
  public com.smartitengineering.user.client.api.BasicIdentity getFather() {
    return father;
  }

  @Override
  public com.smartitengineering.user.client.api.BasicIdentity getMother() {
    return mother;
  }

  @Override
  public com.smartitengineering.user.client.api.BasicIdentity getSpouse() {
    return spouse;
  }

  @Override
  public com.smartitengineering.user.client.api.BasicIdentity getSelf() {
    return self;
  }

  @Override
  public com.smartitengineering.user.client.api.Address getAddress() {
    return address;
  }

  @Override
  public Date getBirthDay() {
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

  @Override
  @JsonDeserialize(as = Address.class)
  public void setAddress(com.smartitengineering.user.client.api.Address address) {
    if (address == null) {
      return;
    }
    this.address = address;
  }

  @Override
  public void setBirthDay(Date birthday) {
    if (birthday == null) {
      return;
    }
    this.birthday = birthday;
  }

  @Override
  public void setCellPhoneNumber(String cellPhoneNumber) {
    if (cellPhoneNumber == null) {
      return;
    }
    this.cellPhoneNumber = cellPhoneNumber;
  }

  @Override
  @JsonDeserialize(as = BasicIdentity.class)
  public void setFather(com.smartitengineering.user.client.api.BasicIdentity father) {
    if (father == null) {
      return;
    }
    this.father = father;
  }

  @Override
  public void setFaxNumber(String faxNumber) {
    if (faxNumber == null) {
      return;
    }
    this.faxNumber = faxNumber;
  }

  @Override
  @JsonDeserialize(as = BasicIdentity.class)
  public void setMother(com.smartitengineering.user.client.api.BasicIdentity mother) {
    if (mother == null) {
      return;
    }
    this.mother = mother;
  }

  @Override
  public void setPhoneNumber(String phoneNumber) {
    if (phoneNumber == null) {
      return;
    }
    this.phoneNumber = phoneNumber;
  }

  @Override
  public void setPrimaryEmail(String primaryEmail) {
    if (primaryEmail == null) {
      return;
    }
    this.primaryEmail = primaryEmail;
  }

  @Override
  public void setSecondaryEmail(String secondaryEmail) {
    if (secondaryEmail == null) {
      return;
    }
    this.secondaryEmail = secondaryEmail;
  }

  @Override
  @JsonDeserialize(as = BasicIdentity.class)
  public void setSelf(com.smartitengineering.user.client.api.BasicIdentity self) {
    if (self == null) {
      return;
    }
    this.self = self;
  }

  @Override
  @JsonDeserialize(as = BasicIdentity.class)
  public void setSpouse(com.smartitengineering.user.client.api.BasicIdentity spouse) {
    if (spouse == null) {
      return;
    }
    this.spouse = spouse;
  }
}
