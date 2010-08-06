/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security;

import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 *
 * @author modhu7
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            System.out.println("context null");
            return loadUserFromDB(username);
        } else {
            System.out.println("context not null");
            System.out.println(context.toString());
            return loadUserFromDB(username);
        }

    }

    private UserDetails loadUserFromDB(String username) {
        UserDetailsImpl userDetails = new UserDetailsImpl();
        User user = userService.getUserByUsername(username);
        if (user != null) {
            userDetails.setUser(user);
        }else{
            userDetails.setUser(new User());
        }
        return userDetails;
    }
}
