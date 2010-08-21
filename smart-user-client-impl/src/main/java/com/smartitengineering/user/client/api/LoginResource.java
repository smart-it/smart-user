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

  public AuthorizationResource getAclAuthorizationResource(String username, String organizationName, String oid, Integer permission);

  public AuthorizationResource getRoleAuthorizationResource(String username, String configAttribute);
}
