/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;


import com.smartitengineering.smartuser.client.api.OrganizationResource;
import com.smartitengineering.smartuser.client.api.Person;
import com.smartitengineering.smartuser.client.api.PrivilegesResource;
import com.smartitengineering.smartuser.client.api.RolesResource;
import com.smartitengineering.smartuser.client.api.User;
import com.smartitengineering.smartuser.client.api.UserResource;
import com.smartitengineering.util.rest.atom.AtomClientUtil;
import com.smartitengineering.util.rest.client.ClientUtil;
import com.sun.jersey.api.client.ClientResponse;
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
public class UserResourceImpl extends AbstractClientImpl implements UserResource{

  public static final String REL_USER = "user";
  public static final String REL_ALT = "alternate";

  private URI userURI;
  private Link userLink;
  private Link usersLink;
  private Link privilegesLink;
  private Link rolesLink;
  

  private boolean isCacheEnabled;
  private Date lastModifiedDate;
  private Date expirationDate;

  private User user;

  UserResourceImpl(User user){

    Link createdUserLink = Abdera.getNewFactory().newLink();
    createdUserLink.setHref(BASE_URI.toString() + "/username/"+ user.getUsername());

    this.userLink = createdUserLink;
    userURI = UriBuilder.fromUri(BASE_URI.toString() + userLink.getHref().toString()).build();

    ClientResponse response = ClientUtil.readClientResponse(userURI, getHttpClient(), MediaType.APPLICATION_ATOM_XML);

    if(response.getStatus() != 401){

      Feed feed = AtomClientUtil.getFeed(response);



      Link userContentLink = feed.getLink(REL_ALT);

      URI orgContentURI = UriBuilder.fromUri(BASE_URI.toString() + userContentLink.getHref().toString()).build();
      ClientResponse contentResponse = ClientUtil.readClientResponse(orgContentURI, getHttpClient(), MediaType.APPLICATION_JSON);

      if(contentResponse.getStatus() != 401){
        user = ClientUtil.getResponseEntity(contentResponse, com.smartitengineering.user.client.impl.domain.User.class);
        //String str = ClientUtil.getResponseEntity(contentResponse, String.class);

      }

      Feed contentFeed = AtomClientUtil.getFeed(response);

      String href = userLink.getHref().toString();

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
      userURI = response.getLocation();

    }else{
      userLink = null;
    }
  }

  UserResourceImpl(Link usersLink) {

    this.userLink = userLink;
    userURI = UriBuilder.fromUri(BASE_URI.toString() + userLink.getHref().toString()).build();

    ClientResponse response = ClientUtil.readClientResponse(userURI, getHttpClient(), MediaType.APPLICATION_ATOM_XML);

    if(response.getStatus() == 200){

      Feed feed = AtomClientUtil.getFeed(response);



      Link userContentLink = feed.getLink(REL_ALT);

      URI orgContentURI = UriBuilder.fromUri(BASE_URI.toString() + userContentLink.getHref().toString()).build();
      ClientResponse contentResponse = ClientUtil.readClientResponse(orgContentURI, getHttpClient(), MediaType.APPLICATION_JSON);

      if(contentResponse.getStatus() != 401){
        user = ClientUtil.getResponseEntity(contentResponse, com.smartitengineering.user.client.impl.domain.User.class);        
        //String str = ClientUtil.getResponseEntity(contentResponse, String.class);

      }

      Feed contentFeed = AtomClientUtil.getFeed(response);

      String href = userLink.getHref().toString();

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
      userURI = response.getLocation();

    }else{
      userLink = null;
    }
  }

  @Override
  public User getUser() {
    return user;
  }

  @Override
  public Person getProfile() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public PrivilegesResource getPrivilegesResource() {
    //throw new UnsupportedOperationException("Not supported yet.");
    //return new PrivilegesResourceImpl(privilegesLink);
    return null;

  }

  @Override
  public RolesResource getRolesResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public OrganizationResource getOrganizationResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public UserResource update() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void delete() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public UserResource refreshAndMerge() {
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
    return userURI;
  }

  @Override
  public UserResource refresh() {
    return new UserResourceImpl(usersLink);
  }

}
