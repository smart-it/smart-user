/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;


import com.smartitengineering.smartuser.client.api.AuthorizationResource;
import com.smartitengineering.smartuser.client.api.LoginResource;
import com.smartitengineering.smartuser.client.api.OrganizationResource;
import com.smartitengineering.smartuser.client.api.OrganizationsResource;
import com.smartitengineering.smartuser.client.api.UserResource;
import com.smartitengineering.smartuser.client.api.UsersResource;
import com.smartitengineering.util.rest.atom.ClientUtil;
import com.sun.jersey.api.client.ClientResponse;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
class LoginResourceImpl extends AbstractClientImpl implements LoginResource {

  private static final String REL_ORGS = "Organizations";
  private static final String REL_ORG = "Organization";
  private static final String REL_USERS = "Users";
  private static final String REL_USER = "User";
  private static final String REL_ACL_AUTH = "aclAuth";
  private static final String REL_ROLE_AUTH = "roleAuth";
  private final Link orgsLink;
  private final Link orgLink;
  private final Link usersLink;
  private final Link userLink;
  private boolean isCacheEnabled;
  private Date lastModifiedDate;
  private Date expirationDate;
  private URI uri;
  private String userName;
  private String password;
  private Link loginLink;
  private Link aclAuthLink;
  private Link roleAuthLink;

  public LoginResourceImpl(String userName, String password, Link loginLink) {
    this.userName = userName;
    this.password = password;
    this.loginLink = loginLink;
    URI uri = UriBuilder.fromUri(BASE_URI.toString() + loginLink.getHref().toString() + "?username=" + this.userName).
        build();
    ClientResponse response = ClientUtil.readClientResponse(uri, getHttpClient(), MediaType.APPLICATION_ATOM_XML);


    if (response.getStatus() != 401) {
      Feed feed = ClientUtil.getFeed(response);
      orgsLink = feed.getLink(REL_ORGS);
      orgLink = feed.getLink(REL_ORG);
      usersLink = feed.getLink(REL_USERS);
      userLink = feed.getLink(REL_USER);
      aclAuthLink = feed.getLink(REL_ACL_AUTH);
      roleAuthLink = feed.getLink(REL_ROLE_AUTH);

      if (response.getHeaders().getFirst("Cache-Control") != null) {
        isCacheEnabled = response.getHeaders().getFirst("Cache-Control").equals("no-cache");
      }
      String dateString = response.getHeaders().getFirst("Last-Modified");
      SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
      try {
        lastModifiedDate = format.parse(dateString);
      }
      catch (Exception ex) {
      }
      dateString = response.getHeaders().getFirst("Expires");
      try {
        expirationDate = format.parse(dateString);
      }
      catch (Exception ex) {
      }
      uri = response.getLocation();

    }
    else {
      orgsLink = null;
      orgLink = null;
      usersLink = null;
      userLink = null;
      aclAuthLink = null;
      roleAuthLink = null;
    }

    //ClientResponse response = webResource.post();

  }

  @Override
  public OrganizationsResource getOrganizationsResource() {
    return new OrganizationsResourceImpl(orgsLink);
  }

  @Override
  public UsersResource getUsersResource(String OrganizationShortName) {
    return new UsersResourceImpl(usersLink);
  }

  @Override
  public UserResource getUserResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public OrganizationResource getOrganizationResource() {
    return new OrganizationResourceImpl(orgLink);
  }

  @Override
  public boolean isCacheEnabled() {
    return isCacheEnabled;
  }

  @Override
  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  @Override
  public Date getExpirationDate() {
    return expirationDate;
  }

  @Override
  public String getUUID() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public URI getUri() {
    return uri;
  }

  @Override
  public Object refresh() {
    return new LoginResourceImpl(userName, password, loginLink);
  }

  @Override
  public AuthorizationResource getAclAuthorizationResource(String username, String organizationName, String oid,
                                                           Integer permission) {
    return new AuthorizationResourceImpl(username, organizationName, oid, permission, aclAuthLink);
  }

  @Override
  public AuthorizationResource getRoleAuthorizationResource(String username, String configAttribute) {
    return new AuthorizationResourceImpl(username, configAttribute, roleAuthLink);
  }
}
