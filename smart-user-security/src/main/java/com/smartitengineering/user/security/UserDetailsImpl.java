/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security;

import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.User;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

/**
 *
 * @author modhu7
 */
public class UserDetailsImpl implements UserDetails {

    private User user = new User();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GrantedAuthority[] getAuthorities() {       
        Set<SmartUserAuthority> authoritiesSet = new HashSet<SmartUserAuthority>();
        for (Role role : user.getRoles()) {
            authoritiesSet.add(new SmartUserAuthority(role.getName()));
        }
        return authoritiesSet.toArray(new GrantedAuthority[authoritiesSet.size()]);
    }

    public String getPassword() {
        System.out.println("--------------------- password " + user.getPassword());
        return user.getPassword();
    }

    public String getUsername() {
        System.out.println("--------------------- username " + user.getUsername());
        return getUserNameWithOrganizationName();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    private String getUserNameWithOrganizationName() {
        return user.getUsername() + "@" + user.getOrganization().getUniqueShortName();
    }
}
