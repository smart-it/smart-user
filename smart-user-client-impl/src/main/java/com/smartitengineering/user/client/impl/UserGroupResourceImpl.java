/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.UserGroup;
import com.smartitengineering.user.client.api.UserGroupPrivilegesResource;
import com.smartitengineering.user.client.api.UserGroupResource;
import com.smartitengineering.user.client.api.UserGroupRolesResource;
import com.smartitengineering.user.client.api.UserGroupUsersResource;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.smartitengineering.util.rest.client.SimpleResourceImpl;
import com.sun.jersey.api.client.config.ClientConfig;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author modhu7
 */
public class UserGroupResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements UserGroupResource{

  public final String REL_USER_GROUP = "usergroup";
  public final String REL_USERS = "users";
  public final String REL_ROLES = "roles";
  public final String REL_PRIVILEGES = "privileges";


  public UserGroupResourceImpl(ResourceLink link, Resource referrer) {
    super(referrer, link);
    final ResourceLink altLink = getRelatedResourceUris().getFirst(Link.REL_ALTERNATE);
    addNestedResource(REL_USER_GROUP, new SimpleResourceImpl<com.smartitengineering.user.client.impl.domain.UserGroup>(
        this, altLink.getUri(), altLink.getMimeType(), com.smartitengineering.user.client.impl.domain.UserGroup.class,
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
  public UserGroup getUserGroup() {
    return getUserGroup(false);
  }

  @Override
  public UserGroupUsersResource getUserGroupUsersResource() {
    return new UserGroupUsersResouceImpl(getRelatedResourceUris().getFirst(REL_USERS), this);
  }

  @Override
  public UserGroupPrivilegesResource getUserGroupPrivilegesResource() {
    return new UserGroupPrivilegesResourceImpl(getRelatedResourceUris().getFirst(REL_PRIVILEGES), this);
  }

  protected UserGroup getUserGroup(boolean reload) {
    Resource<UserGroup> userGroup = super.<UserGroup>getNestedResource(REL_USER_GROUP);
    if(reload){
      return userGroup.get();
    }
    else{
      return userGroup.getLastReadStateOfEntity();
    }
  }

  @Override
  public UserGroupRolesResource getUserGroupRolesResource() {
    return new UserGroupRolesResourceImpl(getRelatedResourceUris().getFirst(REL_ROLES), this);
  }

}
