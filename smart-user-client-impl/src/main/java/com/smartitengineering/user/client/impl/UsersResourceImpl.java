/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.User;
import com.smartitengineering.user.client.api.UserFilter;
import com.smartitengineering.user.client.api.UserResource;
import com.smartitengineering.user.client.api.UsersResource;
import com.smartitengineering.user.resource.api.LinkedResource;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
class UsersResourceImpl extends AbstractClientImpl implements UsersResource{

  public UsersResourceImpl(Link usersLink) {
  }

  @Override
  public Collection<LinkedResource<UserResource>> getUserResources() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public UserResource create(User user) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public UsersResource search(UserFilter filter) {
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
