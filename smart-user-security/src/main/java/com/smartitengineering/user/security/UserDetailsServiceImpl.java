/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security;

import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.service.UserServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 *
 * @author modhu7
 */
public class UserDetailsServiceImpl implements UserDetailsService {

  private static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

  public UserService getUserService() {
    return UserServiceFactory.getInstance().getUserService();
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
    return loadUserFromDB(username);
  }

  private UserDetails loadUserFromDB(String username) {
    logger.info("loadUser is call with username : " + username);
    UserDetailsImpl userDetails = new UserDetailsImpl();
    User user = getUserService().getUserByUsername(username);
    if (user != null) {
      logger.debug("user is not null");
      userDetails.setUser(user);
    }
    else {
      logger.debug("user is null");
      userDetails.setUser(new User());
    }
    return userDetails;
  }
}
