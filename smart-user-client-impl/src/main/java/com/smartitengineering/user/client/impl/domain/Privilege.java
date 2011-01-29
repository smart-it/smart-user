/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl.domain;

import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

/**
 *
 * @author russel
 */
public class Privilege extends AbstractClientDomain<Long> implements com.smartitengineering.user.client.api.Privilege {

  private String name;
  private String displayName;
  private String shortDescription;
  private Integer permissionMask;
  private com.smartitengineering.user.client.api.SecuredObject securedObject;
  private Date lastModifiedDate;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String getShortDescription() {
    return shortDescription;
  }

  @Override
  public com.smartitengineering.user.client.api.SecuredObject getSecuredObject() {
    return securedObject;
  }

  @Override
  public Integer getPermissionMask() {
    return permissionMask;
  }

  @Override
  public void setDisplayName(String displayName) {
    if (displayName == null) {
      return;
    }
    this.displayName = displayName;
  }

  @Override
  public void setName(String name) {
    if (name == null) {
      return;
    }
    this.name = name;
  }

  @Override
  public void setPermissionMask(Integer permissionMask) {
    if (permissionMask == null) {
      return;
    }
    this.permissionMask = permissionMask;
  }

  @Override
  @JsonDeserialize(as = SecuredObject.class)
  public void setSecuredObject(com.smartitengineering.user.client.api.SecuredObject securedObject) {
    if (securedObject == null) {
      return;
    }
    this.securedObject = securedObject;
  }

  @Override
  public void setShortDescription(String shortDescription) {
    if (shortDescription == null) {
      return;
    }
    this.shortDescription = shortDescription;
  }

  @Override
  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    if (lastModifiedDate == null) {
      return;
    }
    this.lastModifiedDate = lastModifiedDate;
  }
}
