/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.Person;
import com.smartitengineering.user.client.api.PrivilegesResource;
import com.smartitengineering.user.client.api.RolesResource;
import com.smartitengineering.user.client.api.User;
import com.smartitengineering.user.client.api.UserResource;
import java.net.URI;
import java.util.Date;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
public class UserResourceImpl extends AbstractClientImpl implements UserResource{

  UserResourceImpl(Link usersLink) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public User getUser() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Person getProfile() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public PrivilegesResource getPrivilegesResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public RolesResource getRolesResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public OrganizationResource getOrganizationResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public UserResource update() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void delete() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public UserResource refreshAndMerge() {
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
  public UserResource refresh() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
