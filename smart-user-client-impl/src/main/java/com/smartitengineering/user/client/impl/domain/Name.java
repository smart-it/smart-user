/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl.domain;

import java.util.Date;

/**
 *
 * @author modhu7
 */
public class Name extends AbstractClientDomain implements com.smartitengineering.smartuser.client.api.Name {

  private String firstName;
  private String lastName;
  private String middleInitial;

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public String getMiddleInitial() {
    return middleInitial;
  }

  public void setFirstName(String firstName) {
    if (firstName == null) {
      return;
    }
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    if (lastName == null) {
      return;
    }
    this.lastName = lastName;
  }

  public void setMiddleInitial(String middleInitial) {
    if (middleInitial == null) {
      return;
    }
    this.middleInitial = middleInitial;
  }
}
