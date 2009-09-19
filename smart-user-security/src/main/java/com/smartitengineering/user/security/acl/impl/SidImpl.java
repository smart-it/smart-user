/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security.acl.impl;

import com.smartitengineering.user.domain.User;
import org.springframework.security.acls.sid.Sid;

/**
 *
 * @author modhu7
 */
public class SidImpl implements Sid{
    String username;

    public SidImpl(String username) {
        this.username = username;
    }   

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
