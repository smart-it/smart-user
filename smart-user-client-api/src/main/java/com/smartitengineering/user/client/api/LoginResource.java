/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import com.smartitengineering.util.rest.client.Resource;
import org.apache.abdera.model.Feed;


/**
 *
 * @author russel
 */
public interface LoginResource extends Resource<Feed> {

  public OrganizationsResource getOrganizationsResource();

  public RolesResource getRolesResource(); 

  public UserResource getUserResource();

  public OrganizationResource getOrganizationResource();

  public AuthorizationResource getAclAuthorizationResource(String username, String organizationName, String oid, Integer permission);

  public AuthorizationResource getRoleAuthorizationResource(String username, String organizationName, String configAttribute);
}
