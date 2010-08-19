/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import com.smartitengineering.user.resource.api.Resource;

/**
 *
 * @author russel
 */
public interface LoginResource extends Resource {

  public OrganizationsResource getOrganizationsResource();

  public UsersResource getUsersResource(String OrganizationShortName);

  public UserResource getUserResource();

  public OrganizationResource getOrganizationResource();
}
