/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.smartuser.client.api;

/**
 *
 * @author modhu7
 */
public interface Privilege {

  public String getName();

  public String getDisplayName();

  public String getShortDescription();

  public SecuredObject getSecuredObjcet();

  public Integer getPermissionMask();
}
