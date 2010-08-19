/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.Privilege;
import com.smartitengineering.user.client.api.PrivilegeResource;
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
public class PrivilegeResourceImpl extends AbstractClientImpl implements PrivilegeResource{

  public static final String REL_ALT = "alternate";
  public static final String REL_PRIV = "privilege";

  private Link privilegeLink;
  private Link securedObjectLink;
  private URI privilegeURI;

  private boolean isCacheEnabled;
  private Date lastModifiedDate;
  private Date expirationDate;

  private Privilege privilege;

  public PrivilegeResourceImpl(com.smartitengineering.user.client.impl.domain.Privilege privilege){

    Link createdPrivilegeLink = Abdera.getNewFactory().newLink();
    createdPrivilegeLink.setHref(BASE_URI.toString() + "/privs/" + privilege.getName());

    this.privilegeLink = createdPrivilegeLink;

    privilegeURI = UriBuilder.fromUri(BASE_URI.toString() + privilegeLink.getHref().toString()).build();

    ClientResponse response = ClientUtil.readClientResponse(privilegeURI, getHttpClient(), MediaType.APPLICATION_JSON);

    if(response.getStatus() != 401){
      Feed feed = ClientUtil.getFeed(response);

      Link privilegeContentLink = feed.getLink(REL_ALT);

      URI privilegeContentURI = UriBuilder.fromUri(BASE_URI.toString() + privilegeContentLink.getHref().toString()).build();

      ClientResponse contentResponse = ClientUtil.readClientResponse(privilegeContentURI, getHttpClient(), MediaType.APPLICATION_JSON);

      if(contentResponse.getStatus() != 401){
        privilege = ClientUtil.getResponseEntity(contentResponse, com.smartitengineering.user.client.impl.domain.Privilege.class);
      }
      Feed contentFeed = ClientUtil.getFeed(response);



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
      privilegeURI = response.getLocation();

    }
  }

  public PrivilegeResourceImpl(Link privilegeLink){

    this.privilegeLink = privilegeLink;

    privilegeURI = UriBuilder.fromUri(BASE_URI.toString() + privilegeLink.getHref().toString()).build();

    ClientResponse response = ClientUtil.readClientResponse(privilegeURI, getHttpClient(), MediaType.APPLICATION_JSON);

    if(response.getStatus() != 401){
      Feed feed = ClientUtil.getFeed(response);

      Link privilegeContentLink = feed.getLink(REL_ALT);

      URI privilegeContentURI = UriBuilder.fromUri(BASE_URI.toString() + privilegeContentLink.getHref().toString()).build();

      ClientResponse contentResponse = ClientUtil.readClientResponse(privilegeContentURI, getHttpClient(), MediaType.APPLICATION_JSON);

      if(contentResponse.getStatus() != 401){
        privilege = ClientUtil.getResponseEntity(contentResponse, com.smartitengineering.user.client.impl.domain.Privilege.class);
      }
      Feed contentFeed = ClientUtil.getFeed(response);



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
      privilegeURI = response.getLocation();
      
    }
  }

  @Override
  public Privilege getPrivilege() {
    return privilege;
  }

  @Override
  public OrganizationResource getOrganizationResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public PrivilegeResource update() {
    WebResource webResource = getClient().resource(privilegeURI);
    webResource.type(MediaType.APPLICATION_JSON).put();

    return new PrivilegeResourceImpl(privilegeLink);
  }

  @Override
  public void delete() {
    WebResource webResource = getClient().resource(privilegeURI);
    webResource.type(MediaType.APPLICATION_JSON).delete();
    
  }

  @Override
  public PrivilegeResource refreshAndMerge() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean isCacheEnabled() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Date getLastModifiedDate() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Date getExpirationDate() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public String getUUID() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public URI getUri() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public PrivilegeResource refresh() {
    throw new UnsupportedOperationException("Not supported yet.");
  }


}
