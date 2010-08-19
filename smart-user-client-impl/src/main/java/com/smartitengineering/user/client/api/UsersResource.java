/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import com.smartitengineering.user.resource.api.LinkedResource;
import com.smartitengineering.user.resource.api.Resource;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author modhu7
 */
public interface UsersResource extends Resource {

  //public Collection<LinkedResource<UserResource>> getUserResources();

  public List<UserResource> getUserResources();

  public UserResource create(com.smartitengineering.user.client.impl.domain.User user);

  public UsersResource search(UserFilter filter);
}
