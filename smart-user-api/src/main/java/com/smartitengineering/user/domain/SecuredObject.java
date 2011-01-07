/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractGenericPersistentDTO;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author russel
 */
public class SecuredObject extends AbstractGenericPersistentDTO<SecuredObject, Long, Long> {

  private String name;
  private String objectID;
  private String parentObjectID;
  private Organization organization;
  private Integer parentOrganizationID;
  private Date lastModifiedDate;
  private Date creationDate;

  @JsonIgnore
  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  @JsonIgnore
  public Date getLastModifiedDate() {
    if (lastModifiedDate == null) {
      lastModifiedDate = new Date();
    }
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    if (lastModifiedDate == null) {
      return;
    }
    this.lastModifiedDate = lastModifiedDate;
  }

  public String getName() {
    if (name == null) {
      name = "";
    }
    return name;
  }

  public void setName(String name) {
    if (name == null) {
      return;
    }
    this.name = name;
  }

  @JsonIgnore
  public Organization getOrganization() {
    if (organization == null) {
      organization = new Organization("", "");
    }
    return organization;
  }

  @JsonIgnore
  public void setOrganization(Organization organization) {
    if (organization == null) {
      return;
    }
    this.organization = organization;
  }

  public String getParentObjectID() {
    return parentObjectID;
  }

  public void setParentObjectID(String parentObjectID) {
    this.parentObjectID = parentObjectID;
  }

  public String getObjectID() {
    if (objectID == null) {
      objectID = "";
    }
    return objectID;
  }

  public void setObjectID(String objectID) {
    if (objectID == null) {
      return;
    }
    this.objectID = objectID;
  }

  @Override
  @JsonIgnore
  public boolean isValid() {
    return StringUtils.isNotBlank(objectID) && (organization != null);
  }

  @JsonIgnore
  public Integer getParentOrganizationID() {
    return parentOrganizationID;
  }

  public void setParentOrganizationID(Integer parentOrganizationID) {
    this.parentOrganizationID = parentOrganizationID;
  }
}
