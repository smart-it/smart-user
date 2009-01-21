/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.filter.UserPersonFilter;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public interface UserService{
    
    public void create(UserPerson userPerson);
    
    public void update(User user);
    
    public void delete(User user);
    
    public void update(UserPerson userPerson);
    
    public void delete(UserPerson userPerson);
    
    public Collection<User> search(UserFilter filter);
    
    public Collection<UserPerson> search(UserPersonFilter filter);
    
    public Collection<UserPerson> getAllUserPerson();
    
    public Collection<User> getAllUser();
    
    public User getUserByUsername(String username);
    
    public UserPerson getUserPersonByUsername(String username);  
    
    
    /*Role Service*/
    
    public void create(Role role);
    
    public void update(Role role);
    
    public void delete(Role role);
    
    public Role getRoleByName(String name);
    
    
    /*Privilege Service*/
    
    public void create(Privilege privilege);
    
    public void update(Privilege privilege);
    
    public void delete(Privilege privilege);
    
    public Privilege getPrivilegeByName(String name);
    
}
