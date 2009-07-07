/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security;

import com.smartitengineering.user.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 *
 * @author modhu7
 */
public class UserDetailsServiceImpl implements UserDetailsService{

    UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUser(userService.getUserByUsername(username));
        return userDetails;
    }

}
