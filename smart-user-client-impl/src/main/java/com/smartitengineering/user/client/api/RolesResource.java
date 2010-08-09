/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import com.smartitengineering.user.resource.api.LinkedResource;
import com.smartitengineering.user.resource.api.Resource;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public interface RolesResource extends Resource {

  public Collection<LinkedResource<RoleResource>> getRoleResources();

  public RoleResource create(Role role);

  public RolesResource search(RoleFilter filter);
}
