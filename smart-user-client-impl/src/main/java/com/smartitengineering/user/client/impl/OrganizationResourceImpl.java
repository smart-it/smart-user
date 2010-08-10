/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.Organization;
import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.OrganizationsResource;
import com.smartitengineering.user.client.api.PrivilegesResource;
import com.smartitengineering.user.client.api.SecuredObjectsResource;
import com.smartitengineering.user.client.api.UsersResource;
import java.net.URI;
import java.util.Date;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
class OrganizationResourceImpl extends AbstractClientImpl implements OrganizationResource{

  public OrganizationResourceImpl(Link orgLink) {
  }

  @Override
  public UsersResource getUsersResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public SecuredObjectsResource getSecuredObjectsResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public PrivilegesResource getPrivilegesResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Organization getOrganization() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public OrganizationsResource getOrganizationsResource() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public OrganizationResource update() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void delete() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public OrganizationResource refreshAndMerge() {
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
  public OrganizationResource refresh() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
