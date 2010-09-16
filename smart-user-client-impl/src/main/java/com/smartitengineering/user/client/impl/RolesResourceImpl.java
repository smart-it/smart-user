/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.smartuser.client.api.Role;
import com.smartitengineering.smartuser.client.api.RoleFilter;
import com.smartitengineering.smartuser.client.api.RoleResource;
import com.smartitengineering.smartuser.client.api.RolesResource;
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
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public class RolesResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements RolesResource {

  private static final String REL_ROLE = "Role";
  private static final String REL_ALT = "alternate";

  public RolesResourceImpl(ResourceLink rolesLink, Resource referrer) {
    super(referrer, rolesLink);
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return new RolesResourceImpl(link, this);
  }

  @Override
  public List<RoleResource> getRoleResources() {
    List<RoleResource> roleResources = new ArrayList<RoleResource>();

    for (Entry entry : getLastReadStateOfEntity().getEntries()) {
      roleResources.add(new RoleResourceImpl(AtomClientUtil.convertFromAtomLinkToResourceLink(
          entry.getLink(REL_ALT)), this));
    }

    return roleResources;
  }

  @Override
  public RoleResource create(Role role) {
    ClientResponse response = post(MediaType.APPLICATION_JSON, role, ClientResponse.Status.CREATED);
    final ResourceLink roleLink = ClientUtil.createResourceLink(REL_ROLE, response.getLocation(),
                                                               MediaType.APPLICATION_ATOM_XML);
    return new RoleResourceImpl(roleLink, this);
  }

  @Override
  public RolesResource search(RoleFilter filter) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
