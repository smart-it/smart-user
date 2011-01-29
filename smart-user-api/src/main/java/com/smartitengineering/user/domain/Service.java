/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractGenericPersistentDTO;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author russel
 */
public class Service extends AbstractGenericPersistentDTO<Service, Long, Integer>{

  private String name;
  private boolean isActive;

  public boolean isIsActive() {
    return isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
    
  public boolean isValid(){
    if(StringUtils.isNotBlank(name)){
      return true;
    }
    return false;
  }
}
