/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;


import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.UserPerson;
import com.smartitengineering.user.client.api.UserPrivilegesResource;
import com.smartitengineering.user.client.api.UserResource;
import com.smartitengineering.user.client.api.UserRolesResource;
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
public class UserResourceImpl extends AbstractFeedClientResource<Resource<? extends Feed>> implements UserResource{

  public static final String REL_USER = "user";
  public static final String REL_ALT = "alternate";
  public static final String REL_USER_PRIVS = "privileges";
  public static final String REL_USER_ROLES = "roles";
  public static final String REL_ORGANIZATION = "organization";
  

  public UserResourceImpl(ResourceLink userLink, Resource referrer) {
    super(referrer, userLink);
    final ResourceLink altLink = getRelatedResourceUris().getFirst(Link.REL_ALTERNATE);
    addNestedResource(REL_USER, new SimpleResourceImpl<com.smartitengineering.user.client.impl.domain.UserPerson>(
        this, altLink.getUri(), altLink.getMimeType(), com.smartitengineering.user.client.impl.domain.UserPerson.class,
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
  public UserPrivilegesResource getPrivilegesResource() {
    return new UserPrivilegesResourceImpl(getRelatedResourceUris().getFirst(REL_USER_PRIVS), this);
  }

  @Override
  public UserRolesResource getRolesResource() {
    return new UserRolesResourceImpl(getRelatedResourceUris().getFirst(REL_USER_ROLES), this);
  }

  @Override
  public void update() {
    put(MediaType.APPLICATION_JSON, getUser(), ClientResponse.Status.OK, ClientResponse.Status.SEE_OTHER,
        ClientResponse.Status.FOUND);
  }

  @Override
  public UserPerson getUser() {
    return getUser(false);
  }

  @Override
  public UserPerson getUserReloaded() {
    return getUser(true);
  }

  protected UserPerson getUser(boolean reload) {
    Resource<UserPerson> user = super.<UserPerson>getNestedResource(REL_USER);
    if(reload){
      return user.get();
    }
    else{
      return user.getLastReadStateOfEntity();
    }
  }

  @Override
  public OrganizationResource getOrganizationResource() {
    return new OrganizationResourceImpl(getRelatedResourceUris().getFirst(REL_ORGANIZATION), this);
  }
}
