/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.UserLinkResource;
import com.smartitengineering.user.client.api.UserResource;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.sun.jersey.api.client.config.ClientConfig;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public class UserLinkResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements
    UserLinkResource {

  public static final String REL_USER_LINK = "userLink";

  public UserLinkResourceImpl(ResourceLink userGetterLink, String username, Resource referrer) {
    super(referrer, getSelfUri(userGetterLink, username));
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
    return new UserResourceImpl(getRelatedResourceUris().getFirst(REL_USER_LINK), this);
  }

  protected static URI getSelfUri(ResourceLink userGetterLink,
                                  String username)
      throws IllegalArgumentException,
             UriBuilderException {
    URI UserLinkResourUri =
        UriBuilder.fromUri(BASE_URI.toString()).path(userGetterLink.getUri().toString()).queryParam("username", username).
        build();
    return UserLinkResourUri;
  }
}
