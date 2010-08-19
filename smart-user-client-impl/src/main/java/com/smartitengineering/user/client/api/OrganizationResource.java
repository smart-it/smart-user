/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.api;

import com.smartitengineering.user.resource.api.WritableResource;

/**
 *
 * @author russel
 */
public interface OrganizationResource extends WritableResource<OrganizationResource>{

  public UsersResource getUsersResource();

  public SecuredObjectsResource getSecuredObjectsResource();

  public PrivilegesResource getPrivilegesResource();

  //public RolesResource getRolesResource();

  public Organization getOrganization();

  public OrganizationsResource getOrganizationsResource();

}
