/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.smartuser.client.api.Privilege;
import com.smartitengineering.smartuser.client.api.PrivilegeFilter;
import com.smartitengineering.smartuser.client.api.PrivilegeResource;
import com.smartitengineering.smartuser.client.api.PrivilegesResource;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.atom.AtomClientUtil;
import com.smartitengineering.util.rest.client.ClientUtil;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.apache.abdera.model.Entry;

/**
 *
 * @author russel
 */
public class PrivilegesResourceImpl extends AbstractFeedClientResource<PrivilegesResourceImpl> implements PrivilegesResource{

  private static final String REL_PRIV = "Privilege";
  private static final String REL_ALT = "alternate";

  
  public PrivilegesResourceImpl(ResourceLink privsLink, Resource referrer){
    super(referrer, privsLink);
  }

  @Override
  public PrivilegesResource search(PrivilegeFilter filter) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {    
  }

  @Override
  protected PrivilegesResourceImpl instantiatePageableResource(ResourceLink link) {
    return new PrivilegesResourceImpl(link, this);
  }

  @Override
  public List<PrivilegeResource> getPrivilegeResources() {
    List<PrivilegeResource> privilegeResources = new ArrayList<PrivilegeResource>();

    for (Entry entry : getLastReadStateOfEntity().getEntries()) {
      privilegeResources.add(new PrivilegeResourceImpl(AtomClientUtil.convertFromAtomLinkToResourceLink(
          entry.getLink(REL_ALT)), this));
    }

    return privilegeResources;
  }

  @Override
  public PrivilegeResource create(Privilege privilege) {
    ClientResponse response = post(MediaType.APPLICATION_JSON, privilege, ClientResponse.Status.CREATED);
    final ResourceLink privLink = ClientUtil.createResourceLink(REL_PRIV, response.getLocation(),
                                                               MediaType.APPLICATION_ATOM_XML);
    return new PrivilegeResourceImpl(privLink, this);
  }
}
