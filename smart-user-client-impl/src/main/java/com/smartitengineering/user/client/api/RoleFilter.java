/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import com.smartitengineering.user.resource.api.Filter;

/**
 *
 * @author modhu7
 */
public interface RoleFilter extends Filter{

  public void setNameRegex(String name);

  public void setDisplayNameRegex(String displayName);
}
