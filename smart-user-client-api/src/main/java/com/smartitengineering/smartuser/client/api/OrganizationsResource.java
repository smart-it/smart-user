/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.smartuser.client.api;

import com.smartitengineering.user.resource.api.Resource;
import java.util.List;

/**
 *
 * @author russel
 */
public interface OrganizationsResource extends Resource{

  public OrganizationResource create(Organization organization);

  //public Collection<LinkedResource<OrganizationResource>> getOrganizationResources();
  public List<OrganizationResource> getOrganizationResources();

  public OrganizationsResource search(OrganizationFilter filter);

}
