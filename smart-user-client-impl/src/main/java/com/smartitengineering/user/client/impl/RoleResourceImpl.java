/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.smartuser.client.api.Role;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.smartitengineering.util.rest.client.SimpleResourceImpl;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import javax.ws.rs.core.MediaType;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;


/**
 *
 * @author modhu7
 */
public class RoleResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements com.smartitengineering.smartuser.client.api.RoleResource{
  
  private Resource<? extends Role> role;

  public RoleResourceImpl(ResourceLink roleLink, Resource referrer) {
    super(referrer, roleLink);
    final ResourceLink altLink = getRelatedResourceUris().getFirst(Link.REL_ALTERNATE);
    role = new SimpleResourceImpl<com.smartitengineering.user.client.impl.domain.Role>(
        this, altLink.getUri(), altLink.getMimeType(), com.smartitengineering.user.client.impl.domain.Role.class,
        null, false, null, null);
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return null;
  }

  @Override
  public Role getRole() {
    return role.getLastReadStateOfEntity();
  }

  @Override
  public void update() {
    put(MediaType.APPLICATION_JSON, getRole(), ClientResponse.Status.OK, ClientResponse.Status.SEE_OTHER,
        ClientResponse.Status.FOUND);
  }

}
