/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.SecuredObject;
import com.smartitengineering.user.client.api.SecuredObjectResource;
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
public class SecuredObjectResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements
    SecuredObjectResource {

  public static final String REL_SECUREDOBJECT = "securedobject";
  public static final String REL_ALT = "alternate";

  public SecuredObjectResourceImpl(ResourceLink securedObjectLink, Resource referrer) {
    super(referrer, securedObjectLink);
    final ResourceLink altLink = getRelatedResourceUris().getFirst(Link.REL_ALTERNATE);
    addNestedResource(REL_SECUREDOBJECT,
                      new SimpleResourceImpl<com.smartitengineering.user.client.impl.domain.SecuredObject>(this, altLink.
        getUri(), altLink.getMimeType(), com.smartitengineering.user.client.impl.domain.SecuredObject.class, null, false,
                                                                                                           null, null));
  }

  @Override
  public SecuredObject getSecuredObjcet() {
    return getSecuredObject(false);
  }

  @Override
  public OrganizationResource getOrganizationResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void update() {
    put(MediaType.APPLICATION_JSON, getSecuredObjcet(), ClientResponse.Status.OK, ClientResponse.Status.SEE_OTHER,
        ClientResponse.Status.FOUND);
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return null;
  }

  private SecuredObject getSecuredObject(boolean reload) {
    Resource<SecuredObject> securedObject = super.<SecuredObject>getNestedResource(REL_SECUREDOBJECT);
    if(reload){
      return securedObject.get();
    }
    else{
      return securedObject.getLastReadStateOfEntity();
    }
  }
}
