/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import com.smartitengineering.util.rest.client.WritableResource;
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public interface UserResource extends WritableResource<Feed> {

  public UserPerson getUser();

  public UserPerson getUserReloaded();

  public UserPrivilegesResource getPrivilegesResource();

  public UserRolesResource getRolesResource();

  public OrganizationResource getOrganizationResource();

  public void update();
}
