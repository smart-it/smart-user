package com.smartitengineering.user.security;

import org.springframework.security.GrantedAuthority;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author modhu7
 */
public class SmartUserAuthority implements GrantedAuthority {
    
    private String authority;

    public SmartUserAuthority(String authority) {
        this.authority = authority;
    }

    

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public int compareTo(Object o) {
        GrantedAuthority comparableGrantedAuthority = (GrantedAuthority) o;
        return getAuthority().compareTo(comparableGrantedAuthority.getAuthority());
    }

    public String getAuthority() {
        return authority;
    }

}
