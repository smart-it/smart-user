/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl.domain;

/**
 *
 * @author modhu7
 */
public class Role extends AbstractClientDomain implements com.smartitengineering.smartuser.client.api.Role {

  private String name;
  private String displayName;
  private String shortDescription;

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

  public void setDisplayName(String displayName) {
    if (displayName == null) {
      return;
    }
    this.displayName = displayName;
  }

  public void setName(String name) {
    if (name == null) {
      return;
    }
    this.name = name;
  }

  public void setShortDescription(String shortDescription) {
    if (shortDescription == null) {
      return;
    }
    this.shortDescription = shortDescription;
  }
}
