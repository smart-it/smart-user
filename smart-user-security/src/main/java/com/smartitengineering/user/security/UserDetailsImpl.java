/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security;

import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.User;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

/**
 *
 * @author modhu7
 */
public class UserDetailsImpl implements UserDetails {

  private static Logger logger = LoggerFactory.getLogger(UserDetailsImpl.class);

  private User user = new User();

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public GrantedAuthority[] getAuthorities() {
    logger.info("getAuthorities method is called for user: " + user.getUsername());
    Set<SmartUserAuthority> authoritiesSet = new HashSet<SmartUserAuthority>();
    for (Role role : user.getRoles()) {
      authoritiesSet.add(new SmartUserAuthority(role.getName()));
    }
    return authoritiesSet.toArray(new GrantedAuthority[authoritiesSet.size()]);
  }

  @Override
  public String getPassword() {    
    return user.getPassword();
  }

  @Override
  public String getUsername() {    
    return getUserNameWithOrganizationName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  private String getUserNameWithOrganizationName() {
    return user.getUsername() + "@" + user.getOrganization().getUniqueShortName();
  }
}
