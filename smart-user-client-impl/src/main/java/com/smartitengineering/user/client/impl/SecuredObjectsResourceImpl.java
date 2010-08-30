/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.smartuser.client.api.SecuredObject;
import com.smartitengineering.smartuser.client.api.SecuredObjectFilter;
import com.smartitengineering.smartuser.client.api.SecuredObjectResource;
import com.smartitengineering.smartuser.client.api.SecuredObjectsResource;
import com.smartitengineering.util.rest.atom.ClientUtil;
import com.sun.jersey.api.client.ClientResponse;
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
public class SecuredObjectsResourceImpl extends AbstractClientImpl implements SecuredObjectsResource{

  private Link securedObjectsLink;
  private URI securedObjectsURI;

  private static final String REL_SECUREDOBJECT ="securedobject";
  private static final String REL_ALT = "alternate";

  private boolean isCacheEnabled;
  private Date lastModifiedDate;
  private Date expirationDate;
  private List<Entry> entries;

  public SecuredObjectsResourceImpl(Link securedObjectsLink){

    this.securedObjectsLink = securedObjectsLink;

    securedObjectsURI = UriBuilder.fromUri(BASE_URI.toString() + securedObjectsLink.getHref().toString()).build();
    URI uri = UriBuilder.fromUri(BASE_URI.toString() + securedObjectsLink.getHref().toString()).build();

    ClientResponse response = ClientUtil.readClientResponse(uri, getHttpClient(), MediaType.APPLICATION_ATOM_XML);

    if (response.getStatus() == 200) {
      Feed feed = ClientUtil.getFeed(response);

      entries = feed.getEntries();

      securedObjectsLink = feed.getLink(REL_SECUREDOBJECT);


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

      securedObjectsLink=null;

    }
  }

  @Override
  public List<SecuredObjectResource> getSecuredObjectResources(){

    List<SecuredObjectResource> securedObjectResources = new ArrayList<SecuredObjectResource>();

    for(Entry entry: entries){
      securedObjectResources.add( new SecuredObjectResourceImpl(entry.getLink(REL_ALT)));
    }

    return securedObjectResources;
  }

//  @Override
//  public Collection<LinkedResource<SecuredObjectResource>> getSecuredObjectResources() {
//    throw new UnsupportedOperationException("Not supported yet.");
//  }

  @Override
  public SecuredObjectResource create(SecuredObject securedObjcet) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public SecuredObjectsResource search(SecuredObjectFilter filter) {
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
  public Object refresh() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
