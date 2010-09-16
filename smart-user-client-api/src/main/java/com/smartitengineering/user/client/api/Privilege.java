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
public interface Privilege {

  public String getName();

  public String getDisplayName();

  public String getShortDescription();

  public SecuredObject getSecuredObject();

  public Integer getPermissionMask();

  public Date getLastModifiedDate();
}
