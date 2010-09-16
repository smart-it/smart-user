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
public interface Organization {

  public String getName();

  public String getUniqueShortName();

  public Address getAddress();

  public Date getLastModifiedDate();

}
