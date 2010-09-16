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
public interface SecuredObject {

  public String getName();

  public String getObjectID();

  public String getParentObjectID();

  public Date getLastModifiedDate();
}
