/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.Role;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public interface RoleService {

    void create(Role role);

    void delete(Role role);

    Role getRoleByUserID(String userID);

    Collection<Role> getRolesByOrganizationAndUser(String organization, String user);

    void update(Role role);
}
