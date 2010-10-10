/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.User;
import com.smartitengineering.user.client.api.UserGroupUserResource;
import com.smartitengineering.user.client.api.UserGroupUsersResource;
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
public class UserGroupUsersResouceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements
    UserGroupUsersResource {

  public static final String REL_ALT = "alternate";
  public static final String REL_USER_GROUP_USER = "user";

  public UserGroupUsersResouceImpl(ResourceLink link, Resource referrer) {
    super(referrer, link);
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return new UserGroupUsersResouceImpl(link, this);
  }

  @Override
  public List<UserGroupUserResource> getUserGroupUserResources() {
    List<UserGroupUserResource> userGroupUserResources = new ArrayList<UserGroupUserResource>();
    for (Entry entry : getLastReadStateOfEntity().getEntries()) {
      userGroupUserResources.add(new UserGroupUserResourceImpl(AtomClientUtil.convertFromAtomLinkToResourceLink(
          entry.getLink(REL_ALT)), this));
    }
    return userGroupUserResources;
  }

  @Override
  public UserGroupUserResource add(User user) {
    ClientResponse response = post(MediaType.APPLICATION_JSON, user, ClientResponse.Status.CREATED);
    final ResourceLink userLink = ClientUtil.createResourceLink(REL_USER_GROUP_USER, response.getLocation(),
                                                               MediaType.APPLICATION_ATOM_XML);
    return new UserGroupUserResourceImpl(userLink, this);
  }
}
