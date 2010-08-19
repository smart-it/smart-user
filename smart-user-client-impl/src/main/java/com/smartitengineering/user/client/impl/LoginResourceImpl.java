/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.LoginResource;
import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.OrganizationsResource;
import com.smartitengineering.user.client.api.UserResource;
import com.smartitengineering.user.client.api.UsersResource;
import com.smartitengineering.util.rest.atom.ClientUtil;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
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

  public LoginResourceImpl(String userName, String password, Link loginLink) {
    this.userName = userName;
    this.password = password;
    this.loginLink = loginLink;
    URI uri = UriBuilder.fromUri(BASE_URI.toString() + loginLink.getHref().toString() + "?username=" + this.userName).build();
    ClientResponse response = ClientUtil.readClientResponse(uri, getHttpClient(), MediaType.APPLICATION_ATOM_XML);

    if (response.getStatus() != 401) {
      Feed feed = ClientUtil.getFeed(response);
      orgsLink = feed.getLink(REL_ORGS);
      orgLink = feed.getLink(REL_ORG);
      usersLink = feed.getLink(REL_USERS);
      userLink = feed.getLink(REL_USER);

      if(response.getHeaders().getFirst("Cache-Control") != null)
        isCacheEnabled = response.getHeaders().getFirst("Cache-Control").equals("no-cache");
      String dateString = response.getHeaders().getFirst("Last-Modified");
      SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
      try {
        lastModifiedDate = format.parse(dateString);
      }
      catch (Exception ex) {
      }
      dateString = response.getHeaders().getFirst("Expires");
      try {
        lastModifiedDate = format.parse(dateString);
      }
      catch (Exception ex) {
      }
      uri = response.getLocation();

    }else{
      orgsLink=null;
      orgLink=null;
      usersLink=null;
      userLink=null;
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
}
