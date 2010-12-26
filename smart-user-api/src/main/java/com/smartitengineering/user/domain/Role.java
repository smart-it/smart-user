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
 * @author modhu7
 */
public class Role extends AbstractGenericPersistentDTO<Role, Long, Integer> {

  private String name;
  private String displayName;
  private String shortDescription;
  private Date lastModifiedDate;

  public Role() {
  }

  public Role(String name, String displayName, String shortDescription) {
    this.name = name;
    this.displayName = displayName;
    this.shortDescription = shortDescription;
  }

  public String getDisplayName() {
    if (displayName == null) {
      displayName = "";
    }
    return displayName;
  }

  public void setDisplayName(String displayName) {
    if (displayName == null) {
      return;
    }
    this.displayName = displayName;
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

  public String getShortDescription() {
    if (shortDescription == null) {
      shortDescription = "";
    }
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    if (shortDescription == null) {
      return;
    }
    this.shortDescription = shortDescription;
  }

  @JsonIgnore
  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  @Override
  @JsonIgnore
  public boolean isValid() {
    if (StringUtils.isEmpty(name) || StringUtils.isEmpty(displayName)) {
      return false;
    }
    return true;
  }
}
