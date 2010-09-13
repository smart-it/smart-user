/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.smartuser.client.api;

import com.smartitengineering.util.rest.client.WritableResource;
import java.util.List;
import org.apache.abdera.model.Feed;

/**
 *
 * @author russel
 */
public interface OrganizationsResource extends WritableResource<Feed> {

  public OrganizationResource create(Organization organization);

  //public Collection<LinkedResource<OrganizationResource>> getOrganizationResources();
  public List<OrganizationResource> getOrganizationResources();

  public OrganizationsResource search(OrganizationFilter filter);
}
