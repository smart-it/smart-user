/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserGroup;
import java.util.Collection;

/**
 *
 * @author russel
 */
public interface UserGroupService {

    public void save(UserGroup userGroup);

    public void update(UserGroup userGroup);

    public void delete(UserGroup userGroup);

    public Collection<UserGroup> getByOrganizationName(String organizationName);

    public UserGroup getByOrganizationAndUserGroupName(String organizationShortName, String userGroupName);

    public Collection<UserGroup> getAllUserGroup();

    public void validateUserGroup(UserGroup userGroup);

    public Collection<UserGroup> getUserGroupsByUser(User user);

}
