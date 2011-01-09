/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.Privilege;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author modhu7
 */
public interface PrivilegeService {

  void create(Privilege privilege);

  void delete(Privilege privilege);

  List<Privilege> getPrivilegesByIds(Long... ids);

  List<Privilege> getPrivilegesByIds(List<Long> ids);

  Privilege getPrivilegeByName(String name);

  Collection<Privilege> getPrivilegesByOrganizationNameAndObjectID(String organizationName, String objectID);
  //Privilege getPrivilegesByObjectID(String objectID);

  Privilege getPrivilegeByOrganizationAndPrivilegeName(String organizationName, String privilegename);

  Collection<Privilege> getPrivilegesByOrganizationAndUser(String organizationName, String userName);

  Collection<Privilege> getPrivilegesByOrganization(String organization);

  void update(Privilege privilege);
}
