/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.filter.PrivilegeFilter;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public interface PrivilegeService {
    
    public void create(Privilege privilege);
    
    public void update(Privilege privilege);
    
    public void delete(Privilege privilege);
    
    public void getByName(String name);
    
    public Collection<Privilege> search(PrivilegeFilter privilegeFilter);
    
    public Collection<Privilege> getAllPrivileges();
}
