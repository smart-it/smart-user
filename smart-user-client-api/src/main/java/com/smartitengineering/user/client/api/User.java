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
public interface User {

  public String getUsername();

  public String getPassword();

  public Date getLastModifiedDate();

  public void setUsername(String username);

  public void setPassword(String password);
}
