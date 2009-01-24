/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractPersistentDTO;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author modhu7
 */
public class User extends AbstractPersistentDTO<User>{
    private String username;
    private String password;
    private Set<Role> roles;

    public String getPassword() {
        if(password==null){
            password = "";
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
            roles = new HashSet<Role>();
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
            username = "";
        }
        return username;
    }

    public void setUsername(String username) {
        if(username==null){
            return;
        }
        this.username = username;
    }

    public boolean isValid() {
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return false;
        }
        return true;
    }
    
    
}
