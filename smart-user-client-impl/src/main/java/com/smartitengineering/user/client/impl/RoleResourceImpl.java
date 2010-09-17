/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.Role;
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
public class RoleResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements com.smartitengineering.user.client.api.RoleResource{
  
  public static final String REL_ROLE = "role";

  public RoleResourceImpl(ResourceLink roleLink, Resource referrer) {
    super(referrer, roleLink);
    final ResourceLink altLink = getRelatedResourceUris().getFirst(Link.REL_ALTERNATE);
    addNestedResource(REL_ROLE, new SimpleResourceImpl<com.smartitengineering.user.client.impl.domain.Role>(
        this, altLink.getUri(), altLink.getMimeType(), com.smartitengineering.user.client.impl.domain.Role.class,
        null, false, null, null));
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
    return getRole(false);
  }

  @Override
  public void update() {
    put(MediaType.APPLICATION_JSON, getRole(), ClientResponse.Status.OK, ClientResponse.Status.SEE_OTHER,
        ClientResponse.Status.FOUND);
  }

  private Role getRole(boolean reload) {
    Resource<Role> role = super.<Role>getNestedResource(REL_ROLE);
    if(reload){
      return role.get();
    }
    else{
      return role.getLastReadStateOfEntity();
    }
  }

}
