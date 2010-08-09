/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import com.smartitengineering.user.resource.api.WritableResource;

/**
 *
 * @author modhu7
 */
public interface UserResource extends WritableResource<UserResource> {

  public User getUser();

  public Person getProfile();

  public PrivilegesResource getPrivilegesResource();

  public RolesResource getRolesResource();

  public OrganizationResource getOrganizationResource();
}
