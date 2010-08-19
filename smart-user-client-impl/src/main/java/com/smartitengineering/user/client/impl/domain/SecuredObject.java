/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl.domain;

import com.smartitengineering.user.client.api.Organization;

/**
 *
 * @author russel
 */
public class SecuredObject implements com.smartitengineering.user.client.api.SecuredObject{

  @Override
  public String getName() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public String getObjectID() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public String getParentObjectID() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Organization getOrganization() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
