/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.RoleResource;
import com.smartitengineering.user.client.api.UserRoleResource;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.smartitengineering.util.rest.client.SimpleResourceImpl;
import com.sun.jersey.api.client.config.ClientConfig;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author modhu7
 */
public class UserRoleResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements
    UserRoleResource {

  public final String REL_ROLE = "role";

  public UserRoleResourceImpl(ResourceLink roleLink, Resource referrer) {
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
  public RoleResource getRoleResource() {
    return new RoleResourceImpl(getRelatedResourceUris().getFirst(REL_ROLE), this);
  }
}
