/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.smartuser.client.api;


import com.smartitengineering.user.resource.api.Resource;
import java.util.List;

/**
 *
 * @author modhu7
 */
public interface UsersResource extends Resource {

  //public Collection<LinkedResource<UserResource>> getUserResources();

  public List<UserResource> getUserResources();

  public UserResource create(User user);

  public UsersResource search(UserFilter filter);
}
