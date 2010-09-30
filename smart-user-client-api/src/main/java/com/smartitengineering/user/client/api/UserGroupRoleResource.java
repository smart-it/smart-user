/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import com.smartitengineering.util.rest.client.WritableResource;
import org.apache.abdera.model.Feed;

/**
 *
 * @author uzzal
 */
public interface UserGroupRoleResource extends WritableResource<Feed> {

  public RoleResource getRoleResource();
}
