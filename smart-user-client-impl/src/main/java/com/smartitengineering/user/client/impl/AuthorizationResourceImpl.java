/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.AuthorizationResource;
import com.smartitengineering.util.rest.client.AbstractClientResource;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.sun.jersey.api.client.config.ClientConfig;
import java.net.URI;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

/**
 *
 * @author modhu7
 */
class AuthorizationResourceImpl extends AbstractClientResource<String, Resource> implements
    AuthorizationResource {

  AuthorizationResourceImpl(String username, String organizationName, String oid, Integer permission,
                            ResourceLink aclAuthLink, Resource referrer) {
    super(referrer, getSelfUriForAcl(aclAuthLink, username, organizationName, oid, permission), MediaType.TEXT_PLAIN,
          String.class);
  }

  AuthorizationResourceImpl(String username, String organizationName, String configAttribute, ResourceLink aclRoleLink,
                            Resource referrer) {
    super(referrer, getSelfUriForRole(aclRoleLink, username, organizationName, configAttribute), MediaType.TEXT_PLAIN,
          String.class);
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
  }

  @Override
  public Boolean getAuthorization() {
    return Boolean.valueOf(getLastReadStateOfEntity());
  }

  protected static URI getSelfUriForAcl(ResourceLink authLink,
                                        String username,
                                        String organizationName,
                                        String oid,
                                        Integer permission)
      throws IllegalArgumentException, UriBuilderException {
    URI authResourceUri = UriBuilder.fromPath(BASE_URI.toString()).path(authLink.getUri().toString()).queryParam(
        "username", username).queryParam("orgname", organizationName).queryParam("oid", oid).queryParam("permission",
                                                                                                        permission).
        build();

    return authResourceUri;
  }

  protected static URI getSelfUriForRole(ResourceLink authLink,
                                         String username,
                                         String organizationName,
                                         String configAttribute)
      throws IllegalArgumentException, UriBuilderException {
    URI authResourceUri = UriBuilder.fromPath(BASE_URI.toString()).path(authLink.getUri().toString()).queryParam(
        "username", username).queryParam("orgname", organizationName).queryParam("configAttribute", configAttribute).
        build();

    return authResourceUri;
  }

  @Override
  protected ResourceLink getNextUri() {
    return null;
  }

  @Override
  protected ResourceLink getPreviousUri() {
    return null;
  }

  @Override
  protected Resource instantiatePageableResource(ResourceLink link) {
    return null;
  }
}
