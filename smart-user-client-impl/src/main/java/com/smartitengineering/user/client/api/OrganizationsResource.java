/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.resource.api.LinkedResource;
import java.util.Collection;

/**
 *
 * @author russel
 */
public interface OrganizationsResource {

  public OrganizationResource create(Organization organization);

  public Collection<LinkedResource<OrganizationResource>> getOrganizationResources();

  public OrganizationsResource search(OrganizationFilter filter);

}
