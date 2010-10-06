/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.AuthorizationResource;
import com.smartitengineering.user.client.api.LoginResource;
import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.OrganizationsResource;
import com.smartitengineering.user.client.api.RolesResource;
import com.smartitengineering.user.client.api.UserResource;
import com.smartitengineering.user.client.api.UsersResource;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.sun.jersey.api.client.config.ClientConfig;
import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import org.apache.abdera.model.Feed;

/**
 *
 * @author russel
 */
class LoginResourceImpl
    extends AbstractFeedClientResource<Resource<? extends Feed>>
    implements LoginResource {

  private static final String REL_ORGS = "Organizations";
  private static final String REL_ORG = "Organization";
  private static final String REL_USERS = "Users";
  private static final String REL_USER = "User";
  private static final String REL_ROLES = "Roles";
  private static final String REL_ACL_AUTH = "aclAuth";
  private static final String REL_ROLE_AUTH = "roleAuth";
  private String userName;
  private String password;

  public LoginResourceImpl(String userName,
                           String password,
                           ResourceLink loginLink,
                           Resource referrer)
      throws URISyntaxException {
    super(referrer, getSelfUri(loginLink, userName));
    this.userName = userName;
    this.password = password;
  }

  @Override
  public OrganizationsResource getOrganizationsResource() {
    return new OrganizationsResourceImpl(getRelatedResourceUris().getFirst(REL_ORGS), this);
  }

  @Override
  public UserResource getUserResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public OrganizationResource getOrganizationResource() {
    return new OrganizationResourceImpl(getRelatedResourceUris().getFirst(REL_ORG), this);
  }

  @Override
  public AuthorizationResource getAclAuthorizationResource(String username,
                                                           String organizationName,
                                                           String oid,
                                                           Integer permission) {
    return new AuthorizationResourceImpl(username, organizationName, oid, permission, getRelatedResourceUris().getFirst(
        REL_ACL_AUTH), this);
  }

  @Override
  public AuthorizationResource getRoleAuthorizationResource(String username, String organizationName,
                                                            String configAttribute) {
    return new AuthorizationResourceImpl(username, organizationName, configAttribute, getRelatedResourceUris().getFirst(
        REL_ROLE_AUTH), this);
  }

  protected static URI getSelfUri(ResourceLink loginLink,
                                  String username)
      throws IllegalArgumentException,
             UriBuilderException {
    URI loginResourceUri =
        UriBuilder.fromUri(BASE_URI.toString()).path(loginLink.getUri().toString()).queryParam("username", username).
        build();
    return loginResourceUri;
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return null;
  }

  @Override
  public RolesResource getRolesResource() {
    return new RolesResourceImpl(getRelatedResourceUris().getFirst(REL_ROLES), this);
  }
}
