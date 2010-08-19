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
public interface RoleResource extends WritableResource<RoleResource> {

  public Role getRole();

  public OrganizationResource getOrganizationResource();
}
