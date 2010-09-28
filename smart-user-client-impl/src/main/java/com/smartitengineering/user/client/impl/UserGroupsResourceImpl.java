/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.UserGroup;
import com.smartitengineering.user.client.api.UserGroupFilter;
import com.smartitengineering.user.client.api.UserGroupResource;
import com.smartitengineering.user.client.api.UserGroupsResource;
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
import org.apache.abdera.model.Link;


/**
 *
 * @author modhu7
 */
public class UserGroupsResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements UserGroupsResource{

  private String REL_USER_GROUP = "UserGroup";

  public UserGroupsResourceImpl(ResourceLink link, Resource referrer) {
    super(referrer, link);
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return new UserGroupsResourceImpl(link, this);
  }

  @Override
  public List<UserGroupResource> getUserGroupResources() {
    List<UserGroupResource> userGroupResources = new ArrayList<UserGroupResource>();
    for (Entry entry : getLastReadStateOfEntity().getEntries()) {
      userGroupResources.add(new UserGroupResourceImpl(AtomClientUtil.convertFromAtomLinkToResourceLink(
          entry.getLink(Link.REL_ALTERNATE)), this));
    }

    return userGroupResources;
  }

  @Override
  public UserGroupResource create(UserGroup userGroup) {
    ClientResponse response = post(MediaType.APPLICATION_JSON, userGroup, ClientResponse.Status.CREATED);
    final ResourceLink userGroupLink = ClientUtil.createResourceLink(REL_USER_GROUP, response.getLocation(),
                                                               MediaType.APPLICATION_ATOM_XML);
    return new UserGroupResourceImpl(userGroupLink, this);
  }

  @Override
  public UserGroupsResource search(UserGroupFilter filter) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
