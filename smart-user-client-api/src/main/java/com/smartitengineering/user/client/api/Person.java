/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import java.util.Date;

/**
 *
 * @author modhu7
 */
public interface Person {

  public BasicIdentity getFather();

  public BasicIdentity getMother();

  public BasicIdentity getSpouse();

  public BasicIdentity getSelf();

  public Address getAddress();

  public Date getBirthday();

  public String getPrimaryEmail();

  public String getSecondaryEmail();

  public String getPhoneNumber();

  public String getCellPhoneNumber();

  public String getFaxNumber();

  public void setFather(BasicIdentity father);

  public void setMother(BasicIdentity mother);

  public void setSpouse(BasicIdentity spouse);

  public void setSelf(BasicIdentity self);

  public void setAddress(Address address);

  public void setBirthday(Date birthday);

  public void setPrimaryEmail(String primaryEmail);

  public void setSecondaryEmail(String secondaryEmail);

  public void setPhoneNumber(String phoneNumber);

  public void setCellPhoneNumber(String cellPhoneNumber);

  public void setFaxNumber(String faxNumber);
}
