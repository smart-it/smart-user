/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.filter.UserFilter;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public interface UserService {

    public void save(User user);

    public void update(User user);

    public void delete(User user);

    public User getById(Long userId);

    public Collection<User> search(UserFilter filter);

    public Collection<User> getAllUser();

    public Collection<User> getUsers(String userNameLike, String userName, boolean isSmallerThan, int count);

    public User getUserByUsername(String usernameWithOrganizationName);

    public User getUserByOrganizationAndUserName(String organizationShortName, String userName);

    public Collection<User> getUserByOrganization(String organizationName);

    public Collection<User> getUserByOrganization(String organizationName, String userName, boolean isSmallerThan, int count);

    public void validateUser(User user);
}
