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
    
    public void create(Role role);
    
    public void update(Role role);
    
    public void delete(Role role);
    
    public void getByName(String name);
    
    public Collection<Role> search(RoleFilter roleFilter);
    
    public Collection<Role> getAllRoles();
}
