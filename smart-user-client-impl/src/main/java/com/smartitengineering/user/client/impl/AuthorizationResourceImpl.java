/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;


import com.smartitengineering.smartuser.client.api.AuthorizationResource;
import com.smartitengineering.util.rest.atom.ClientUtil;
import com.sun.jersey.api.client.ClientResponse;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.model.Link;

/**
 *
 * @author modhu7
 */
class AuthorizationResourceImpl extends AbstractClientImpl implements AuthorizationResource {

  private String username;
  private String organizationName;
  private String oid;
  private Integer permission;
  private Link authLink;
  private String configAttribute;
  private boolean isCacheEnabled;
  private Date lastModifiedDate;
  private Date expirationDate;
  private URI uri;
  private Integer authorizationResult;

  AuthorizationResourceImpl(String username, String organizationName, String oid, Integer permission, Link aclAuthLink) {
    this.username = username;
    this.organizationName = organizationName;
    this.oid = oid;
    this.permission = permission;
    this.authLink = aclAuthLink;
    this.configAttribute = null;
    URI uri = UriBuilder.fromUri(BASE_URI.toString() + authLink.getHref().toString() + "?username=" + this.username +
        "&orgname=" + organizationName + "&oid=" + oid + "&permission=" + permission).build();
    ClientResponse response = ClientUtil.readClientResponse(uri, getHttpClient(), MediaType.TEXT_PLAIN);

    if (response.getStatus() == 200) {
      String stringAuthorizationResult = response.getEntity(String.class);
      authorizationResult = Integer.parseInt(stringAuthorizationResult);
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
  }

  AuthorizationResourceImpl(String username, String configAttribute, Link roleAuthLink) {    
  }

  private AuthorizationResourceImpl() {
  }

  @Override
  public Integer getAuthorization() {
    return authorizationResult;
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
    if(configAttribute == null && username != null && organizationName != null && permission != null && oid != null)
      return new AuthorizationResourceImpl(username, organizationName, oid, permission, authLink);
    else if (configAttribute != null && username == null && organizationName == null && permission == null && oid == null)
      return new AuthorizationResourceImpl(username, configAttribute, authLink);
    else
      return new AuthorizationResourceImpl();
  }
}
