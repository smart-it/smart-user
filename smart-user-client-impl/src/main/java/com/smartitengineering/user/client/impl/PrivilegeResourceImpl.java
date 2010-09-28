/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.Privilege;
import com.smartitengineering.user.client.api.PrivilegeResource;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.smartitengineering.util.rest.client.SimpleResourceImpl;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import javax.ws.rs.core.MediaType;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
public class PrivilegeResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements
    PrivilegeResource {

  public static final String REL_PRIV = "privilege";  

  public PrivilegeResourceImpl(ResourceLink privLink, Resource referrer) {

    super(referrer, privLink);
    final ResourceLink altLink = getRelatedResourceUris().getFirst(Link.REL_ALTERNATE);
    addNestedResource(REL_PRIV, new SimpleResourceImpl<com.smartitengineering.user.client.impl.domain.Privilege>(
        this, altLink.getUri(), altLink.getMimeType(), com.smartitengineering.user.client.impl.domain.Privilege.class,
        null, false, null, null));
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return null;
  }

  @Override
  public Privilege getPrivilege() {
    return getPrivilege(false);
  }

  @Override
  public OrganizationResource getOrganizationResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void update() {
    put(MediaType.APPLICATION_JSON, getPrivilege(), ClientResponse.Status.OK, ClientResponse.Status.SEE_OTHER,
        ClientResponse.Status.FOUND);
  }

  protected Privilege getPrivilege(boolean reload) {
    Resource<Privilege> privilege = super.<Privilege>getNestedResource(REL_PRIV);
    if(reload){
      return privilege.get();
    }
    else{
      return privilege.getLastReadStateOfEntity();
    }
  }
}
