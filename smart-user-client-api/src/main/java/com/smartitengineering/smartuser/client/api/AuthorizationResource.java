/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.smartuser.client.api;

import com.smartitengineering.util.rest.client.Resource;

/**
 *
 * @author modhu7
 */
public interface AuthorizationResource extends Resource<String>{
  
  public Boolean getAuthorization();
}
