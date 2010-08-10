/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.OrganizationFilter;
import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.OrganizationsResource;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.resource.api.LinkedResource;
import java.util.Collection;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
class OrganizationsResourceImpl extends AbstractClientImpl implements OrganizationsResource{

  public Link link;

  OrganizationsResourceImpl(Link orgsLink) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public OrganizationResource create(Organization organization) {

    getClient().resource(BASE_URI).post(organization);
    
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Collection<LinkedResource<OrganizationResource>> getOrganizationResources() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public OrganizationsResource search(OrganizationFilter filter) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
