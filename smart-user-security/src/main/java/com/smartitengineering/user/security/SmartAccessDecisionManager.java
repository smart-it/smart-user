/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.vote.AbstractAccessDecisionManager;
import org.springframework.security.vote.AccessDecisionVoter;
import org.springframework.security.vote.RoleVoter;

/**
 *
 * @author modhu7
 */
public class SmartAccessDecisionManager extends AbstractAccessDecisionManager {

  private final String ROLE_PREFIX = "ROLE_";

  @Override
  public void decide(Authentication authentication, Object object, ConfigAttributeDefinition config)
      throws AccessDeniedException {

    int grant = 0;
    int abstain = 0;
    Iterator voterList = getDecisionVoters().iterator();
    Set<VoterConfigTuple> roleVoterConfigTuple = new HashSet<VoterConfigTuple>();
    Set<VoterConfigTuple> aclVoterConfigTuple = new HashSet<VoterConfigTuple>();
    while (voterList.hasNext()) {
      AccessDecisionVoter voter = (AccessDecisionVoter) voterList.next();      
      Iterator configIter = config.getConfigAttributes().iterator();
      while (configIter.hasNext()) {
        ConfigAttribute configAttribute = (ConfigAttribute) configIter.next();
        if (configAttribute.getAttribute().startsWith(ROLE_PREFIX)) {          
          if (voter instanceof RoleVoter) {
            VoterConfigTuple tuple = new VoterConfigTuple();
            tuple.setConfigAttribute(configAttribute);
            tuple.setVoter(voter);
            roleVoterConfigTuple.add(tuple);
          }
        }
        else if (!(voter instanceof RoleVoter)) {
          VoterConfigTuple tuple = new VoterConfigTuple();
          tuple.setConfigAttribute(configAttribute);
          tuple.setVoter(voter);
          aclVoterConfigTuple.add(tuple);
        }
      }
    }
    for (VoterConfigTuple voterConfigTuple : roleVoterConfigTuple) {
      int result = getVotingResult(authentication, object, voterConfigTuple);
      if (result == AccessDecisionVoter.ACCESS_GRANTED) {
        return;
      }
    }
    for (VoterConfigTuple voterConfigTuple : aclVoterConfigTuple) {      
      int result = getVotingResult(authentication, object, voterConfigTuple);

      switch (result) {
        case AccessDecisionVoter.ACCESS_GRANTED: {
          grant++;
          break;
        }

        case AccessDecisionVoter.ACCESS_DENIED: {          
          throw new AccessDeniedException(messages.getMessage("AbstractAccessDecisionManager.accessDenied",
                                                              "Access is denied"));
        }
        default: {
          abstain++;          
          break;
        }
      }
    }
    // To get this far, there were no deny votes
    if (grant > 0) {
      return;
    }

    // To get this far, every AccessDecisionVoter abstained
    checkAllowIfAllAbstainDecisions();    
  }

  private int getVotingResult(Authentication authentication, Object object, VoterConfigTuple voterConfigTuple) {
    return voterConfigTuple.getVoter().vote(authentication, object, new ConfigAttributeDefinition(voterConfigTuple.
        getConfigAttribute()));
  }

  private class VoterConfigTuple {

    private AccessDecisionVoter voter;
    private ConfigAttribute configAttribute;

    public ConfigAttribute getConfigAttribute() {
      return configAttribute;
    }

    public void setConfigAttribute(ConfigAttribute configAttribute) {
      this.configAttribute = configAttribute;
    }

    public AccessDecisionVoter getVoter() {
      return voter;
    }

    public void setVoter(AccessDecisionVoter voter) {
      this.voter = voter;
    }
  }
}
