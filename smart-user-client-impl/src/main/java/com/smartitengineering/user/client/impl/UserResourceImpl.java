/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;


import com.smartitengineering.smartuser.client.api.PrivilegesResource;
import com.smartitengineering.smartuser.client.api.RolesResource;
import com.smartitengineering.smartuser.client.api.UserPerson;
import com.smartitengineering.smartuser.client.api.UserResource;
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

  private Resource<? extends UserPerson> user;

  public UserResourceImpl(ResourceLink userLink, Resource referrer) {
    super(referrer, userLink);
    final ResourceLink altLink = getRelatedResourceUris().getFirst(Link.REL_ALTERNATE);
    user = new SimpleResourceImpl<com.smartitengineering.user.client.impl.domain.UserPerson>(
        this, altLink.getUri(), altLink.getMimeType(), com.smartitengineering.user.client.impl.domain.UserPerson.class,
        null, false, null, null);
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {    
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return null;
  }

   @Override
  public PrivilegesResource getPrivilegesResource() {
    return new PrivilegesResourceImpl(getRelatedResourceUris().getFirst(REL_USER_PRIVS), this);
  }

  @Override
  public RolesResource getRolesResource() {
    return new RolesResourceImpl(getRelatedResourceUris().getFirst(REL_USER_ROLES), this);
  }

  @Override
  public void update() {
    put(MediaType.APPLICATION_JSON, getUser(), ClientResponse.Status.OK, ClientResponse.Status.SEE_OTHER,
        ClientResponse.Status.FOUND);
  }

  @Override
  public UserPerson getUser() {
    return user.getLastReadStateOfEntity();
  } 
}
