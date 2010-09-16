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
public class BasicIdentity extends AbstractClientDomain implements
    com.smartitengineering.smartuser.client.api.BasicIdentity {

  private Name name;
  private String nationalID;

  @Override
  public Name getName() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public String getNationalID() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setName(Name name) {
    if (name == null) {
      return;
    }
    this.name = name;
  }

  public void setNationalID(String nationalID) {
    if (nationalID == null) {
      return;
    }
    this.nationalID = nationalID;
  }
}
