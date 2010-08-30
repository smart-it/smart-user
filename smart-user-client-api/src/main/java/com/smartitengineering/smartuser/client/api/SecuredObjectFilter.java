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
public interface SecuredObjectFilter extends Filter{

  public void setNameRegex(String name);

  public void setOidRegex(String oid);

  public void setOrganizationNameRegex(String organizationName);

}
