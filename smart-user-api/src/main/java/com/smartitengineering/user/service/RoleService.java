/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.filter.RoleFilter;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public interface RoleService {

    void create(Role role);

    void delete(Role role);

    Role getRoleByName(String roleName);

    Collection<Role> getRolesByOrganizationAndUser(String organizationShortname, String username);

    Collection<Role> getRolesByOrganization(String organizationShortName);

    Collection<Role> getAllRoles();

    Role getRoleByOrganizationAndUserAndRole(String organizationShortName, String username, String roleName);

    Role getRoleByOrganizationAndRoleName(String organizationShortName, String roleName);

    Collection<Role> search(RoleFilter filter);

    void update(Role role);
}
