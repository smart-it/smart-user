/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security;

import org.springframework.security.acls.sid.Sid;

/**
 *
 * @author modhu7
 */
public interface UserSid extends Sid{

    public String getUsername();

    public String getOrganizationName();

}
