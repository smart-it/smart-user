/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;


import com.smartitengineering.smartuser.client.api.Organization;
import com.smartitengineering.smartuser.client.api.OrganizationFilter;
import com.smartitengineering.smartuser.client.api.OrganizationResource;
import com.smartitengineering.smartuser.client.api.OrganizationsResource;
import com.smartitengineering.util.rest.atom.AtomClientUtil;

import com.smartitengineering.util.rest.atom.PaginatedFeedEntitiesList;
import com.smartitengineering.util.rest.client.ClientUtil;
import com.smartitengineering.util.rest.client.ResouceLink;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

/**
 *
 * @author russel
 */
class OrganizationsResourceImpl extends AbstractClientImpl implements OrganizationsResource{

  private ResouceLink orgsLink;
  private URI orgsURI;
  private static final String REL_ORG = "Organization";
  private static final String REL_ALT = "alternate";

  private boolean isCacheEnabled;
  private Date lastModifiedDate;
  private Date expirationDate;
  private List<Entry> entries;
 

  OrganizationsResourceImpl(ResouceLink orgsLink) {

    this.orgsLink = orgsLink;

    orgsURI = getBaseUriBuilder().path(orgsLink.getUri().toString()).build();
    
    ClientResponse response = ClientUtil.readClientResponse(orgsURI, getHttpClient(), MediaType.APPLICATION_ATOM_XML);

    PaginatedFeedEntitiesList<Organization> pgs;
    if (response.getStatus() == 200) {
      Feed feed = AtomClientUtil.getFeed(response);

      entries = feed.getEntries();

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

    }else{
      
    }

  }

  @Override
  public OrganizationResource create(Organization organization) {
    
    WebResource webResource = getClient().resource(orgsURI);
    getClient().getProviders();
    try{
    webResource.type(MediaType.APPLICATION_JSON_TYPE).post(organization);
    }catch(Exception ex){
      ex.printStackTrace();
    }

    return new OrganizationResourceImpl(organization);    
  }

//  @Override
//  public Collection<LinkedResource<OrganizationResource>> getOrganizationResources() {
//    //throw new UnsupportedOperationException("Not supported yet.");
//
//    List<OrganizationResource> organizationResources = new ArrayList<OrganizationResource>();
//
//    //LinkedResource<OrganizationResource> linkedResource = new LinkedList<OrganizationResource>();
//
//    for(Entry entry: entries){
//      organizationResources.add( new OrganizationResourceImpl(entry.getLink(REL_ALT)));
//    }
//
//
//    return null;
//  }

  @Override
  public List<OrganizationResource> getOrganizationResources(){

    List<OrganizationResource> organizationResources = new ArrayList<OrganizationResource>();

    for(Entry entry: entries){
      organizationResources.add( new OrganizationResourceImpl(AtomClientUtil.convertFromAtomLinkToResourceLink(
          entry.getLink(REL_ALT))));
    }

    return organizationResources;
  }

  @Override
  public OrganizationsResource search(OrganizationFilter filter) {   
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
    return orgsURI;
  }

  @Override
  public Object refresh() {
    return new OrganizationsResourceImpl(orgsLink);
  }

}
