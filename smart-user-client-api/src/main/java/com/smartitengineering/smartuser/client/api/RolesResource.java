/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.smartuser.client.api;

import com.smartitengineering.user.resource.api.Resource;
import com.smartitengineering.util.rest.client.WritableResource;
import java.util.List;
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public interface RolesResource extends WritableResource<Feed> {

  public List<RoleResource> getRoleResources();

  public RoleResource create(Role role);

  public RolesResource search(RoleFilter filter);
}
