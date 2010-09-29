/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.UserGroupUserResource;
import com.smartitengineering.user.client.api.UserResource;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.sun.jersey.api.client.config.ClientConfig;
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public class UserGroupUserResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements UserGroupUserResource{

  public static final String REL_USER = "user";

  public UserGroupUserResourceImpl(ResourceLink link, Resource referrer) {
    super(referrer, link);
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {    
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return null;
  }

  @Override
  public UserResource getUserResource() {
    return new UserResourceImpl(getRelatedResourceUris().getFirst(REL_USER), this);
  }

}
