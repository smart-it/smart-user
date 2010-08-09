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
public interface PrivilegesResource extends Resource {

  public Collection<LinkedResource<PrivilegeResource>> getPrivilegeResources();

  public PrivilegeResource create(Privilege Privilege);

  public PrivilegesResource search(PrivilegeFilter filter);
}
