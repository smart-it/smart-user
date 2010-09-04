/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.smartuser.client.api;

import com.smartitengineering.user.resource.api.Filter;

/**
 *
 * @author modhu7
 */
public interface OrganizationFilter extends Filter{

  public void setNameRegex(String name);

  public void setShortNameRegex(String shortName);

  public void setAddressRegex(String address);

}
