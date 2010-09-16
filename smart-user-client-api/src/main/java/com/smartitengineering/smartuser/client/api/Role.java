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
public interface Role {

  public String getName();

  public String getDisplayName();

  public String getShortDescription();

  public Date getLastModifiedDate();
}
