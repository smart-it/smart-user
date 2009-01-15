/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.UserPerson;

/**
 *
 * @author modhu7
 */
public interface UserPersonService {
    
    public void create(UserPerson userPerson);
    
    public void update(UserPerson userPerson);
    
    public void delete(UserPerson userPerson);    
   
}
