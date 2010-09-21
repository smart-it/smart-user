/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.smartuser.client.api;

import com.smartitengineering.util.rest.client.ResouceLink;

/**
 *
 * @author russel
 */
public interface RootResource{

  //public OrganizationsResource getOrganizationsResource();
  LoginResource performAuthentication(String userName, String password);

  ResouceLink getLoginLink();
}
