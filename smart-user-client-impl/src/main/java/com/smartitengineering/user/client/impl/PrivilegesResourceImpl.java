/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.smartuser.client.api.Privilege;
import com.smartitengineering.smartuser.client.api.PrivilegeFilter;
import com.smartitengineering.smartuser.client.api.PrivilegeResource;
import com.smartitengineering.smartuser.client.api.PrivilegesResource;
import com.smartitengineering.util.rest.atom.AtomClientUtil;
import com.smartitengineering.util.rest.client.ClientUtil;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
public class PrivilegesResourceImpl extends AbstractClientImpl implements PrivilegesResource{

  public static final String REL_PRIVS = "Privileges";
  public static final String REL_ALT = "alternate";

  private Link privilegesLink;
  private Link privilegeLink;
  private URI privilegesURI;

  private boolean isCacheEnabled;
  private Date lastModifiedDate;
  private Date expirationDate;
  private List<Entry> entries;

  public PrivilegesResourceImpl(Link privilegesLink){

    this.privilegesLink = privilegesLink;
    privilegesURI = UriBuilder.fromUri(BASE_URI.toString() + privilegesLink.getHref().toString()).build();

    URI uri = UriBuilder.fromUri(BASE_URI.toString() + privilegesLink.getHref().toString()).build();

    ClientResponse response = ClientUtil.readClientResponse(privilegesURI, getHttpClient(), MediaType.APPLICATION_ATOM_XML);
    if (response.getStatus() == 200) {
      Feed feed = AtomClientUtil.getFeed(response);

      entries = feed.getEntries();

      privilegesLink = feed.getLink(REL_PRIVS);


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

      privilegesLink=null;

    }
  }

  @Override
  public List<PrivilegeResource> getPrivilegeResources(){

    List<PrivilegeResource> privilegeResources = new ArrayList<PrivilegeResource>();

    for(Entry entry: entries){
      privilegeResources.add(new PrivilegeResourceImpl(entry.getLink(REL_ALT)));
    }
    return privilegeResources;
  }

//  @Override
//  public Collection<LinkedResource<PrivilegeResource>> getPrivilegeResources() {
//
//    List<PrivilegeResource> privilegeResources = new ArrayList<PrivilegeResource>();
//
//    for(Entry entry: entries){
//      privilegeResources.add(new PrivilegeResourceImpl(entry.getLink(REL_ALT)));
//    }
//    return null;
//    //throw new UnsupportedOperationException("Not supported yet.");
//  }

  @Override
  public PrivilegeResource create(Privilege privilege) {

    WebResource webResource = getClient().resource(privilegesURI);
    webResource.type(MediaType.APPLICATION_JSON).post(privilege);

    return new PrivilegeResourceImpl(privilege);

  }

  @Override
  public PrivilegesResource search(PrivilegeFilter filter) {
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
    return privilegesURI;
  }

  @Override
  public Object refresh() {
    return new PrivilegesResourceImpl(privilegesLink);
  }





}
