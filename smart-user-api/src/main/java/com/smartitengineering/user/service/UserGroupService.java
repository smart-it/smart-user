/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.UserGroup;
import java.util.Collection;

/**
 *
 * @author russel
 */
public interface UserGroupService {

    public void save(UserGroup user);

    public void update(UserGroup user);

    public void delete(UserGroup user);

    public Collection<UserGroup> getByOrganizationName(String organizationName);

    public UserGroup getByOrganizationAndUserGroupName(String organizationShortName, String userGroupName);

    public Collection<UserGroup> getAllUserGroup();

}
