/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.smartuser.client.api.SecuredObject;
import com.smartitengineering.smartuser.client.api.SecuredObjectFilter;
import com.smartitengineering.smartuser.client.api.SecuredObjectResource;
import com.smartitengineering.smartuser.client.api.SecuredObjectsResource;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.atom.AtomClientUtil;
import com.smartitengineering.util.rest.client.ClientUtil;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import java.net.URI;
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
public class SecuredObjectsResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements
    SecuredObjectsResource {

  private static final String REL_SECUREDOBJECT = "securedobject";
  private static final String REL_ALT = "alternate";

  public SecuredObjectsResourceImpl(ResourceLink securedObjectsLink, Resource referrer) {
    super(referrer, securedObjectsLink);
  }

  @Override
  public List<SecuredObjectResource> getSecuredObjectResources() {

    List<SecuredObjectResource> securedObjectResources = new ArrayList<SecuredObjectResource>();

    for (Entry entry : getLastReadStateOfEntity().getEntries()) {
      securedObjectResources.add(new SecuredObjectResourceImpl(AtomClientUtil.convertFromAtomLinkToResourceLink(entry.
          getLink(REL_ALT)), this));
    }
    return securedObjectResources;
  }

  @Override
  public SecuredObjectResource create(SecuredObject securedObjcet) {
    ClientResponse response = post(MediaType.APPLICATION_JSON, securedObjcet, ClientResponse.Status.CREATED);
    final ResourceLink securedObjectLink = ClientUtil.createResourceLink(REL_SECUREDOBJECT, response.getLocation(),
                                                                         MediaType.APPLICATION_ATOM_XML);
    return new SecuredObjectResourceImpl(securedObjectLink, this);
  }

  @Override
  public SecuredObjectsResource search(SecuredObjectFilter filter) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return new SecuredObjectsResourceImpl(link, this);
  }
}
