/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.filter;

/**
 *
 * @author modhu7
 */
public class UserPersonFilter extends AbstractFilter {

  private String username;
  private String organizationShortName;

  public String getUsername() {
    if (username == null) {
      return "";
    }
    return username;
  }

  public void setUsername(String username) {
    if (username == null) {
      return;
    }
    this.username = username;
  }

  public String getOrganizationShortName() {
    return organizationShortName;
  }

  public void setOrganizationShortName(String organizationShortName) {
    this.organizationShortName = organizationShortName;
  }
}
