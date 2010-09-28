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
public interface BasicIdentity {

  public Name getName();

  public String getNationalID();

  public void setName(Name name);

  public void setNationalID(String nationalID);
}
