/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.filter;


/**
 *
 * @author russel
 */
public class OrganizationFilter extends AbstractFilter{

  private String organizationUniqueShortName;
  private  String name;

  public String getOrganizationUniqueShortName() {
    if (organizationUniqueShortName == null) {
      organizationUniqueShortName = "";
    }
    return organizationUniqueShortName;
  }

  public void setOrganizationUniqueShortName(String organizationUniqueShortName) {
    this.organizationUniqueShortName = organizationUniqueShortName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  }
