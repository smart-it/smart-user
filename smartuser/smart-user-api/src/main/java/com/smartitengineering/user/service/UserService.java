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
public interface UserService extends PersonService{
    
    public void create(User user);
    
    public void update(User user);
    
    public void delete(User user);
    
    public Collection<User> search(UserFilter filter);
}
