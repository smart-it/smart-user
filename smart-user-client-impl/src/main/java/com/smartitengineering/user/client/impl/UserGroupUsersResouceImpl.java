/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.User;
import com.smartitengineering.user.client.api.UserGroupResource;
import com.smartitengineering.user.client.api.UserGroupUserResource;
import com.smartitengineering.user.client.api.UserGroupUsersResource;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.atom.AtomClientUtil;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.sun.jersey.api.client.config.ClientConfig;
import java.util.ArrayList;
import java.util.List;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public class UserGroupUsersResouceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements
    UserGroupUsersResource {

  public static final String REL_ALT = "alternate";

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
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
