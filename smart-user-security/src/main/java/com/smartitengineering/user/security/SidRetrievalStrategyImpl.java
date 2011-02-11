/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.acls.sid.Sid;
import org.springframework.security.userdetails.UserDetails;

/**
 *
 * @author modhu7
 */
public class SidRetrievalStrategyImpl implements org.springframework.security.acls.sid.SidRetrievalStrategy {

  private static Logger logger = LoggerFactory.getLogger(SidRetrievalStrategyImpl.class);


  @Override
  public Sid[] getSids(Authentication authentication) {
    logger.info("getSids method is called for authentication object with username: " + authentication.getName());
    Sid[] sids = new Sid[1];
    if (authentication.getPrincipal() instanceof UserDetails) {
      sids[0] = new SidImpl(((UserDetails) authentication.getPrincipal()).getUsername());
    }
    return sids;
  }
}
