/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.Organization;
import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.OrganizationsResource;
import com.smartitengineering.user.client.api.PrivilegesResource;
import com.smartitengineering.user.client.api.SecuredObjectsResource;
import com.smartitengineering.user.client.api.UsersResource;
import com.smartitengineering.util.rest.atom.ClientUtil;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
class OrganizationResourceImpl extends AbstractClientImpl implements OrganizationResource{

  public static final String REL_ORGS = "organizations";
  public static final String REL_ORG = "Organization";
  public static final String REL_ALT = "alternate";
  public static final String REL_USERS = "users";
  public static final String REL_PRIVILEGES = "privileges";
  public static final String REL_SECUREDOBJECTS = "securedobjects";

  private URI orgURI;
  private Link orgLink;
  private Link usersLink;
  private Link privilegesLink;
  private Link securedObjectsLink;
  //private Link userLink;

  private boolean isCacheEnabled;
  private Date lastModifiedDate;
  private Date expirationDate;

  private com.smartitengineering.user.client.impl.domain.Organization organization;

  public OrganizationResourceImpl(com.smartitengineering.user.client.impl.domain.Organization organization){
    Link createdOrgLink = Abdera.getNewFactory().newLink();
    createdOrgLink.setHref(BASE_URI.toString() + "/shortname/"+ organization.getUniqueShortName());

    this.orgLink = createdOrgLink;
    orgURI = UriBuilder.fromUri(BASE_URI.toString() + orgLink.getHref().toString()).build();

    ClientResponse response = ClientUtil.readClientResponse(orgURI, getHttpClient(), MediaType.APPLICATION_ATOM_XML);

    if(response.getStatus() != 401){

      Feed feed = ClientUtil.getFeed(response);

      orgLink = feed.getLink(REL_ALT);

      orgLink = feed.getLink(REL_ORGS);
      usersLink = feed.getLink(REL_USERS);
      privilegesLink = feed.getLink(REL_PRIVILEGES);
      securedObjectsLink = feed.getLink(REL_SECUREDOBJECTS);

      URI orgContentURI = UriBuilder.fromUri(BASE_URI.toString() + orgLink.getHref().toString()).build();
      ClientResponse contentResponse = ClientUtil.readClientResponse(orgContentURI, getHttpClient(), MediaType.APPLICATION_JSON);

      if(contentResponse.getStatus() != 401){        
        organization = ClientUtil.getResponseEntity(contentResponse, com.smartitengineering.user.client.impl.domain.Organization.class);
        //String str = ClientUtil.getResponseEntity(contentResponse, String.class);

      }

      Feed contentFeed = ClientUtil.getFeed(response);

      String href = orgLink.getHref().toString();

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
      orgURI = response.getLocation();
      
    }else{
      orgLink = null;
    }

  }

  public OrganizationResourceImpl(Link orgLink) {

    this.orgLink = orgLink;
    orgURI = UriBuilder.fromUri(BASE_URI.toString() + orgLink.getHref().toString()).build();

    ClientResponse response = ClientUtil.readClientResponse(orgURI, getHttpClient(), MediaType.APPLICATION_ATOM_XML);

    if(response.getStatus() != 401){

      Feed feed = ClientUtil.getFeed(response);

      orgLink = feed.getLink(REL_ALT);

      //orgLink = feed.getLink(REL_ORG);
      usersLink = feed.getLink(REL_USERS);
      privilegesLink = feed.getLink(REL_PRIVILEGES);
      securedObjectsLink = feed.getLink(REL_SECUREDOBJECTS);

      URI orgContentURI = UriBuilder.fromUri(BASE_URI.toString() + orgLink.getHref().toString()).build();
      ClientResponse contentResponse = ClientUtil.readClientResponse(orgContentURI, getHttpClient(), MediaType.APPLICATION_JSON);

      if(contentResponse.getStatus() != 401){        
        organization = ClientUtil.getResponseEntity(contentResponse, com.smartitengineering.user.client.impl.domain.Organization.class);
        
      }

      Feed contentFeed = ClientUtil.getFeed(response);

      String href = orgLink.getHref().toString();

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
      orgURI = response.getLocation();
      
    }else{
      orgLink = null;
    }
  }

  @Override
  public UsersResource getUsersResource() {
    return new UsersResourceImpl(usersLink);
  }

  @Override
  public SecuredObjectsResource getSecuredObjectsResource() {
    return new SecuredObjectsResourceImpl(securedObjectsLink);
  }

  @Override
  public PrivilegesResource getPrivilegesResource() {
    return new PrivilegesResourceImpl(privilegesLink);
  }

  @Override
  public Organization getOrganization() {
    return organization;
  }

  @Override
  public OrganizationsResource getOrganizationsResource() {
    return new OrganizationsResourceImpl(orgLink);
  }

  @Override
  public OrganizationResource update() {
    WebResource webResource = getClient().resource(orgURI);
    getClient().getProviders();
    webResource.type(MediaType.APPLICATION_JSON_TYPE).put(organization);

    return new OrganizationResourceImpl(orgLink);
  }

  @Override
  public void delete() {
    WebResource webResource = getClient().resource(orgURI);
    getClient().getProviders();
    webResource.type(MediaType.APPLICATION_JSON).delete();


  }

  @Override
  public OrganizationResource refreshAndMerge() {
    OrganizationResource organizationResource = new OrganizationResourceImpl(orgLink);

    // need consultation

    return organizationResource;
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
    return orgURI;
  }

  @Override
  public OrganizationResource refresh() {
    return new OrganizationResourceImpl(orgLink);
  }

}
