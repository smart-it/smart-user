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
public interface SecuredObject {

  public String getName();

  public String getObjectID();

  public String getParentObjectID();

  public Date getLastModifiedDate();

  public void setName(String name);

  public void setObjectID(String objectID);

  public void setParentObjectID(String parentObjectID);
}
