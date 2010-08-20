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
public interface UserFilter extends Filter{

  public void  setUserNameRegex(String username);

  public void setOrganizationNameRegex(String organizationName);

}
