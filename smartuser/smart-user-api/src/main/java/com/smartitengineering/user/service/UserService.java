/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.filter.UserFilter;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public interface UserService {
    
    public void update(User user);
    
    public void delete(User user);
    
    public Collection<User> search(UserFilter filter);
    
    public Collection<User> getAllUser();
    
    public User getUserByUsername(String username);
    
    public void validateUser(User user);
    
   
    /*Role Service*/
    
    public void create(Role role);
    
    public void update(Role role);
    
    public void delete(Role role);
    
    public Role getRoleByName(String name);
    
    public Collection<Role> getRolesByName(String name);
    
    
    /*Privilege Service*/
    
    public void create(Privilege privilege);
    
    public void update(Privilege privilege);
    
    public void delete(Privilege privilege);
    
    public Privilege getPrivilegeByName(String name);
    
    public Collection<Privilege> getPrivilegesByName(String name);
    
}
