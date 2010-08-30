/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.smartuser.client.api;

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

  public Address address();

  public Date getBirthday();

  public String getPrimaryEmail();

  public String getSecondaryEmail();

  public String getPhoneNumber();

  public String getCellPhoneNumber();

  public String getFaxNumber();
}
