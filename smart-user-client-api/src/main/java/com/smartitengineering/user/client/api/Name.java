/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

/**
 *
 * @author modhu7
 */
public interface Name {

  public String getFirstName();

  public String getLastName();

  public String getMiddleInitial();

  public void setFirstName(String firstName);

  public void setLastName(String lastName);

  public void setMiddleInitial(String middleInitial);
}
