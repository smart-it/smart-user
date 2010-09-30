/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.RoleResource;
import com.smartitengineering.user.client.api.UserGroupRoleResource;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.smartitengineering.util.rest.client.SimpleResourceImpl;
import com.sun.jersey.api.client.config.ClientConfig;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author uzzal
 */
public class UserGroupRoleResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements
    UserGroupRoleResource {

  public final String REL_ROLE = "role";

  public UserGroupRoleResourceImpl(ResourceLink roleLink, Resource referrer) {
    super(referrer, roleLink);
    final ResourceLink altLink = getRelatedResourceUris().getFirst(Link.REL_ALTERNATE);
    addNestedResource(REL_ROLE, new SimpleResourceImpl<com.smartitengineering.user.client.impl.domain.Role>(
        this, altLink.getUri(), altLink.getMimeType(), com.smartitengineering.user.client.impl.domain.Role.class,
        null, false, null, null));
  }

  @Override
  protected void processClientConfig(ClientConfig cc) {
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink rl) {
    return null;
  }

  @Override
  public RoleResource getRoleResource() {
    return new RoleResourceImpl(getRelatedResourceUris().getFirst(REL_ROLE), this);
  }
}
  
  
