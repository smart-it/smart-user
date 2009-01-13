/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.domain;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author modhu7
 */
public class User {
    private String username;
    private String password;
    private Set<Role> roles;

    public String getPassword() {
        if(password==null){
            return "";
        }
        return password;
    }

    public void setPassword(String password) {
        if(password==null){
            return;
        }
        this.password = password;
    }

    public Set<Role> getRoles() {
        if(roles==null){
            return new HashSet<Role>();
        }
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        if(roles==null){
            return;
        }
        this.roles = roles;
    }

    public String getUsername() {
        if(username==null){
            return "";
        }
        return username;
    }

    public void setUsername(String username) {
        if(username==null){
            return;
        }
        this.username = username;
    }
    
    
}
