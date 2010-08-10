/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

import com.smartitengineering.user.resource.api.Resource;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
public interface RootResource{

  //public OrganizationsResource getOrganizationsResource();
  LoginResource performAuthentication(String userName, String password);

  Link getLoginLink();
}
