/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractGenericPersistentDTO;
import java.util.List;

/**
 *
 * @author russel
 */
public class RegisteredService extends AbstractGenericPersistentDTO<RegisteredService, Long, Integer>{

  private List<Organization> organizationList;
  private Service service;
  private boolean isRegistered;
  private boolean isActive;

  public boolean isIsActive() {
    return isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public boolean isIsRegistered() {
    return isRegistered;
  }

  public void setIsRegistered(boolean isRegistered) {
    this.isRegistered = isRegistered;
  }

  public List<Organization> getOrganizationList() {
    return organizationList;
  }

  public void setOrganizationList(List<Organization> organizationList) {
    this.organizationList = organizationList;
  }

  public Service getService() {
    return service;
  }

  public void setService(Service service) {
    this.service = service;
  }

  public boolean isValid(){
    if(organizationList != null && service != null){
      return true;
    }
    return false;
  }
}
