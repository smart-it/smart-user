/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl.domain;

/**
 *
 * @author modhu7
 */
public abstract class AbstractClientDomain {

  private Integer id;
  private Integer version;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    if (id == null) {
      return;
    }
    this.id = id;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    if (version == null) {
      return;
    }
    this.version = version;
  }
}
