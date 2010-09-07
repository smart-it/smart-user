/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security;

import org.springframework.security.Authentication;
import org.springframework.security.acls.sid.Sid;
import org.springframework.security.userdetails.UserDetails;

/**
 *
 * @author modhu7
 */
public class SidRetrievalStrategyImpl implements org.springframework.security.acls.sid.SidRetrievalStrategy {

  public Sid[] getSids(Authentication authentication) {

    Sid[] sids = new Sid[1];
    if (authentication.getPrincipal() instanceof UserDetails) {
      sids[0] = new SidImpl(((UserDetails) authentication.getPrincipal()).getUsername());
    }

    return sids;
  }
}
