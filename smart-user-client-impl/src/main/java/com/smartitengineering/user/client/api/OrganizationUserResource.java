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
public interface OrganizationUserResource {

  OrganizationSecuredObjectResource getOrganizationSecuredObjectResource();

  void update(User user);

  void delete(User user);

}
