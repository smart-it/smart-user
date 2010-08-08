/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.api;

import com.smartitengineering.user.domain.User;

/**
 *
 * @author russel
 */
public interface OrganizationResource {

  public OrganizationUsersResource getUsersResource();

  public OrganizationSecuredObjectsResource getSecuredObject();

  public void update(User user);

  public void delete(User user);

}
