/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.smartuser.client.api.GenericClientException;
import com.smartitengineering.smartuser.client.api.Organization;
import com.smartitengineering.smartuser.client.api.OrganizationFilter;
import com.smartitengineering.smartuser.client.api.OrganizationResource;
import com.smartitengineering.smartuser.client.api.OrganizationsResource;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.atom.AtomClientUtil;

import com.smartitengineering.util.rest.client.ClientUtil;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.apache.abdera.model.Entry;

/**
 *
 * @author russel
 */
class OrganizationsResourceImpl extends AbstractFeedClientResource<OrganizationsResourceImpl> implements
    OrganizationsResource {

  private static final String REL_ORG = "Organization";
  private static final String REL_ALT = "alternate";

  OrganizationsResourceImpl(ResourceLink orgsLink, Resource referrer) {
    super(referrer, orgsLink);
  }

  @Override
  public OrganizationResource create(Organization organization) {
    ClientResponse response = post(MediaType.APPLICATION_JSON, organization, ClientResponse.Status.CREATED);
    final ResourceLink orgLink = ClientUtil.createResourceLink(REL_ORG, response.getLocation(),
                                                               MediaType.APPLICATION_ATOM_XML);
    return new OrganizationResourceImpl(orgLink, this);
  }

  @Override
  public List<OrganizationResource> getOrganizationResources() {

    List<OrganizationResource> organizationResources = new ArrayList<OrganizationResource>();

    for (Entry entry : getLastReadStateOfEntity().getEntries()) {
      organizationResources.add(new OrganizationResourceImpl(AtomClientUtil.convertFromAtomLinkToResourceLink(
          entry.getLink(REL_ALT)), this));
    }

    return organizationResources;
  }

  @Override
  public OrganizationsResource search(OrganizationFilter filter) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
  }

  @Override
  protected OrganizationsResourceImpl instantiatePageableResource(ResourceLink link) {
    return new OrganizationsResourceImpl(link, this);
  }
}
