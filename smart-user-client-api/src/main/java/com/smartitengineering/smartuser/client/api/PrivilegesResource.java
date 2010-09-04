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
public interface PrivilegesResource extends Resource {

  //public Collection<LinkedResource<PrivilegeResource>> getPrivilegeResources();
  public List<PrivilegeResource> getPrivilegeResources();

  public PrivilegeResource create(Privilege Privilege);

  public PrivilegesResource search(PrivilegeFilter filter);
}
