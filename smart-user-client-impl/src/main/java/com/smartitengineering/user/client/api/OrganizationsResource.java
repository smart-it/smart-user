/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.api;

import com.smartitengineering.user.domain.Organization;

/**
 *
 * @author russel
 */
public interface OrganizationsResource {

  public void create(Organization organization);  

  public OrganizationResource getOrganizationResource(String uniqueShortName);
}
