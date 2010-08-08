/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security.acl.impl;

import com.smartitengineering.user.security.acl.UserSid;
import java.util.StringTokenizer;

/**
 *
 * @author modhu7
 */
public class SidImpl implements UserSid {

    String username;
    String organizationName;

    public SidImpl(String username) {
        StringTokenizer tokenizer = new StringTokenizer(username, "@");
        if (tokenizer.hasMoreTokens()) {
            this.username = tokenizer.nextToken();
        }else{
            this.username = "";
        }
        if(tokenizer.hasMoreTokens()){
            this.organizationName = tokenizer.nextToken();
        }else{
            this.organizationName = "";
        }        
    }

    @Override
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
