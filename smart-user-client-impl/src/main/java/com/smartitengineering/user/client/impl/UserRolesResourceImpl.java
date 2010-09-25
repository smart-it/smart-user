/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.Role;
import com.smartitengineering.user.client.api.UserRoleResource;
import com.smartitengineering.user.client.api.UserRolesResource;
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
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public class UserRolesResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>>implements UserRolesResource{

  public static final String REL_ALT = "alternate";
  public static final String REL_USER_ROLE = "role";

  public UserRolesResourceImpl(ResourceLink rolesLink, Resource referrer){
    super(referrer, rolesLink);
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {    
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return new UserRolesResourceImpl(link, this);
  }

  @Override
  public List<UserRoleResource> getUserPrivilegeResources() {
    List<UserRoleResource> roleResources = new ArrayList<UserRoleResource>();
    for (Entry entry : getLastReadStateOfEntity().getEntries()) {
      roleResources.add(new UserRoleResourceImpl(AtomClientUtil.convertFromAtomLinkToResourceLink(
          entry.getLink(REL_ALT)), this));
    }
    return roleResources;
  }

  @Override
  public UserRoleResource add(Role role) {
    ClientResponse response = post(MediaType.APPLICATION_JSON, role, ClientResponse.Status.CREATED);
    final ResourceLink roleLink = ClientUtil.createResourceLink(REL_USER_ROLE, response.getLocation(),
                                                               MediaType.APPLICATION_ATOM_XML);
    return new UserRoleResourceImpl(roleLink, this);
  }
}
