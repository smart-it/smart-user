/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl.domain;

import java.util.Date;

/**
 *
 * @author russel
 */
public class SecuredObject extends AbstractClientDomain implements
    com.smartitengineering.user.client.api.SecuredObject {

  private String name;
  private String objectID;
  private String parentObjectID;
  private Date lastModifiedDate;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getObjectID() {
    return objectID;
  }

  @Override
  public String getParentObjectID() {
    return parentObjectID;
  }

  public void setName(String name) {
    if (name == null) {
      return;
    }
    this.name = name;
  }

  public void setObjectID(String objectID) {
    if (objectID == null) {
      return;
    }
    this.objectID = objectID;
  }

  public void setParentObjectID(String parentObjectID) {
    if (parentObjectID == null) {
      return;
    }
    this.parentObjectID = parentObjectID;
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
