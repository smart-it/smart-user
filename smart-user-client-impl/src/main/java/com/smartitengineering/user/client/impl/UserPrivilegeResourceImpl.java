/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.PrivilegeResource;
import com.smartitengineering.user.client.api.UserPrivilegeResource;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.sun.jersey.api.client.config.ClientConfig;
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public class UserPrivilegeResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements
    UserPrivilegeResource {

  public static final String REL_USER_PRIV = "privilege";

  public UserPrivilegeResourceImpl(ResourceLink privLink, Resource referrer) {
    super(referrer, privLink);
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {    
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return null;
  }

  @Override
  public PrivilegeResource getPrivilegeResource() {
    return new PrivilegeResourceImpl(getRelatedResourceUris().getFirst(REL_USER_PRIV), this);
  }
}
