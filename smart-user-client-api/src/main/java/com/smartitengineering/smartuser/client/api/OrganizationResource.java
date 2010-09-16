/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.smartuser.client.api;

import com.smartitengineering.util.rest.client.WritableResource;
import org.apache.abdera.model.Feed;

/**
 *
 * @author russel
 */
public interface OrganizationResource extends WritableResource<Feed> {

  public UsersResource getUsersResource();

  public SecuredObjectsResource getSecuredObjectsResource();

  public PrivilegesResource getPrivilegesResource();

  public Organization getOrganization();

  public void update();
}
