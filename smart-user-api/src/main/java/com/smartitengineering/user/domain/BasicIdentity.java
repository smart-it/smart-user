/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractGenericPersistentDTO;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author modhu7
 */
public class BasicIdentity extends AbstractGenericPersistentDTO<BasicIdentity, Long, Integer> {

  private String nationalID;
  private Name name;

  public Name getName() {
    if (name == null) {
      name = new Name();
    }
    return name;
  }

  public void setName(Name name) {
    if (name == null) {
      return;
    }
    this.name = name;
  }

  public String getNationalID() {
    if (nationalID == null) {
      nationalID = "";
    }
    return nationalID;
  }

  public void setNationalID(String nationalID) {
    if (nationalID == null) {
      return;
    }
    this.nationalID = nationalID;
  }

  @JsonIgnore
  @Override
  public boolean isValid() {
    if (name != null) {
      if (name.isValid()) {
        return true;
      }
    }
    return false;
  }
}
