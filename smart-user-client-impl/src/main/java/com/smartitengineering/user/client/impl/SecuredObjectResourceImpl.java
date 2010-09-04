/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.smartuser.client.api.OrganizationResource;
import com.smartitengineering.smartuser.client.api.SecuredObject;
import com.smartitengineering.smartuser.client.api.SecuredObjectResource;
import com.smartitengineering.util.rest.atom.AtomClientUtil;
import com.smartitengineering.util.rest.client.ClientUtil;
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
public class SecuredObjectResourceImpl extends AbstractClientImpl implements SecuredObjectResource{

  public static final String REL_SECUREDOBJECT = "securedobject";
  public static final String REL_ALT = "alternate";

  public URI securedObjectURI;
  public Link securedObjectLink;

  private boolean isCacheEnabled;
  private Date lastModifiedDate;
  private Date expirationDate;

  private com.smartitengineering.user.client.impl.domain.SecuredObject securedObject;

  public SecuredObjectResourceImpl(Link securedObjectLink){

    this.securedObjectLink = securedObjectLink;
    securedObjectURI = UriBuilder.fromUri(BASE_URI.toString() + securedObjectLink.getHref().toString()).build();

    ClientResponse response = ClientUtil.readClientResponse(securedObjectURI, getHttpClient(), MediaType.APPLICATION_ATOM_XML);

    if(response.getStatus() == 200){

      Feed feed = AtomClientUtil.getFeed(response);

      securedObjectLink = feed.getLink(REL_ALT);

      securedObjectLink = feed.getLink(REL_SECUREDOBJECT);

      URI orgContentURI = UriBuilder.fromUri(BASE_URI.toString() + securedObjectLink.getHref().toString()).build();
      ClientResponse contentResponse = ClientUtil.readClientResponse(orgContentURI, getHttpClient(), MediaType.APPLICATION_JSON);

      if(contentResponse.getStatus() == 200){
        //com.smartitengineering.user.domain.Organization organization = ClientUtil.getResponseEntity(contentResponse, com.smartitengineering.user.domain.Organization.class);
        String str = ClientUtil.getResponseEntity(contentResponse, String.class);

      }

      Feed contentFeed = AtomClientUtil.getFeed(response);

      String href = securedObjectLink.getHref().toString();

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
      securedObjectURI = response.getLocation();

    }else{
      securedObjectLink = null;
    }
  }

  public SecuredObjectResourceImpl(SecuredObject securedObject){
    
    Link createdLink = Abdera.getNewFactory().newLink();
    createdLink.setHref(BASE_URI.toString() + "/securedobjects/" + securedObject.getName());

    this.securedObjectLink = createdLink;
    securedObjectURI = UriBuilder.fromUri(BASE_URI.toString() + securedObjectLink.getHref().toString()).build();

    ClientResponse response = ClientUtil.readClientResponse(securedObjectURI, getHttpClient(), MediaType.APPLICATION_ATOM_XML);

    if(response.getStatus() != 401){

      Feed feed = AtomClientUtil.getFeed(response);

      securedObjectLink = feed.getLink(REL_ALT);

      securedObjectLink = feed.getLink(REL_SECUREDOBJECT);

      URI orgContentURI = UriBuilder.fromUri(BASE_URI.toString() + securedObjectLink.getHref().toString()).build();
      ClientResponse contentResponse = ClientUtil.readClientResponse(orgContentURI, getHttpClient(), MediaType.APPLICATION_JSON);

      if(contentResponse.getStatus() != 401){
        //com.smartitengineering.user.domain.Organization organization = ClientUtil.getResponseEntity(contentResponse, com.smartitengineering.user.domain.Organization.class);
        String str = ClientUtil.getResponseEntity(contentResponse, String.class);

      }

      Feed contentFeed = AtomClientUtil.getFeed(response);

      String href = securedObjectLink.getHref().toString();

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
      securedObjectURI = response.getLocation();

    }else{
      securedObjectLink = null;
    }

  }

  @Override
  public SecuredObject getSecuredObjcet() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public OrganizationResource getOrganizationResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public SecuredObjectResource update() {
    WebResource webResource = getClient().resource(securedObjectURI);
    getClient().getProviders();
    webResource.type(MediaType.APPLICATION_JSON_TYPE).put(securedObject);

    return new SecuredObjectResourceImpl(securedObjectLink);
  }

  @Override
  public void delete() {
    WebResource webResource = getClient().resource(securedObjectURI);
    getClient().getProviders();
    webResource.type(MediaType.APPLICATION_JSON_TYPE).delete();
  }

  @Override
  public SecuredObjectResource refreshAndMerge() {
    throw new UnsupportedOperationException("Not supported yet.");
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
    return securedObjectURI;
  }

  @Override
  public SecuredObjectResource refresh() {
    return new SecuredObjectResourceImpl(securedObjectLink);
  }

}
