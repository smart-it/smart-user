/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.SecuredObject;
import java.util.Collection;

/**
 *
 * @author russel
 */
public interface SecuredObjectService {

  public void save(SecuredObject securedObject);

  public void update(SecuredObject securedObject);

  public void delete(SecuredObject securedObject);

  public SecuredObject getById(Long id);

  public Collection<SecuredObject> getByOrganization(String organizationName);

  public SecuredObject getByOrganizationAndObjectID(String organizationName, String objectID);

  public SecuredObject getByOrganizationAndName(String organizationName, String name);
}
