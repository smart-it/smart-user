/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.filter.RoleFilter;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author modhu7
 */
public interface RoleService {

  void create(Role role);

  void delete(Role role);

  List<Role> getRolesByIds(Long... ids);

  List<Role> getRolesByIds(List<Long> ids);

  Role getRoleByName(String roleName);

  Collection<Role> getAllRoles();

  Collection<Role> search(RoleFilter filter);

  void update(Role role);
}
