/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl.domain;

import java.util.Date;

/**
 *
 * @author modhu7
 */
public class UserGroup extends AbstractClientDomain<Long> implements com.smartitengineering.user.client.api.UserGroup{

  private String name;
  private Date lastModifiedDate;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  @Override
  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

}
