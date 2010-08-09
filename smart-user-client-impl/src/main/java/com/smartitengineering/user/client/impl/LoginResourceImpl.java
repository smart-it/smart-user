/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.LoginResource;
import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.OrganizationsResource;
import com.smartitengineering.user.client.api.UserResource;
import com.smartitengineering.user.client.api.UsersResource;
import java.net.URI;
import java.util.Date;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
class LoginResourceImpl extends AbstractClientImpl implements LoginResource{

  public LoginResourceImpl(String userName, String password, Link loginLink) {
  }

  @Override
  public OrganizationsResource getOrganizationsResource() {
    return new OrganizationsResourceImpl();
  }

  @Override
  public UsersResource getUsersResource(String OrganizationShortName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public UserResource getUserResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public OrganizationResource getOrganizationResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean isCacheEnabled() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Date getLastModifiedDate() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Date getExpirationDate() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public String getUUID() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public URI getUri() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Object refresh() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
