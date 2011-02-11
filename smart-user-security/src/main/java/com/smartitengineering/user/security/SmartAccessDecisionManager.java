/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static Logger logger = LoggerFactory.getLogger(SmartAccessDecisionManager.class);

  @Override
  public void decide(Authentication authentication, Object object, ConfigAttributeDefinition config)
      throws AccessDeniedException {
    logger.info("@@@@@@@@@@ Access decision manager decide method called : username " + authentication.getName() +
        " @@@@@@@@@@@@@");
    int grant = 0;
    int abstain = 0;
    Iterator voterList = getDecisionVoters().iterator();
    Set<VoterConfigTuple> roleVoterConfigTuple = new HashSet<VoterConfigTuple>();
    Set<VoterConfigTuple> aclVoterConfigTuple = new HashSet<VoterConfigTuple>();
    while (voterList.hasNext()) {
      AccessDecisionVoter voter = (AccessDecisionVoter) voterList.next();
      logger.debug("Voter : " + voter.toString());
      Iterator configIter = config.getConfigAttributes().iterator();
      while (configIter.hasNext()) {
        ConfigAttribute configAttribute = (ConfigAttribute) configIter.next();
        logger.debug("Config Attribute : " + configAttribute.getAttribute());
        if (configAttribute.getAttribute().startsWith(ROLE_PREFIX)) {
          if (voter instanceof RoleVoter) {
            logger.debug("The Voter is RoleVoter");
            VoterConfigTuple tuple = new VoterConfigTuple();
            tuple.setConfigAttribute(configAttribute);
            tuple.setVoter(voter);
            roleVoterConfigTuple.add(tuple);
            logger.info("The Voting tuple added voter: " + voter.toString() + " and ConfigAttribute: " + configAttribute.
                getAttribute());
          }
        }
        else if (!(voter instanceof RoleVoter)) {
          logger.debug("The Voter is not a Role Voter");
          VoterConfigTuple tuple = new VoterConfigTuple();
          tuple.setConfigAttribute(configAttribute);
          tuple.setVoter(voter);
          aclVoterConfigTuple.add(tuple);
          logger.info("The Voting tuple added voter: " + voter.toString() + " and ConfigAttribute: " + configAttribute.
              getAttribute());
        }
      }
    }
    logger.info("Start voting with Role voters config");
    for (VoterConfigTuple voterConfigTuple : roleVoterConfigTuple) {
      int result = getVotingResult(authentication, object, voterConfigTuple);
      if (result == AccessDecisionVoter.ACCESS_GRANTED) {
        return;
      }
    }
    logger.info("Start voting with Acl voters config");
    for (VoterConfigTuple voterConfigTuple : aclVoterConfigTuple) {
      int result = getVotingResult(authentication, object, voterConfigTuple);

      logger.info("The voting result is: " + result);

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
    logger.info("Called getVotingResult Method: username-" + authentication.getName() + "and voter " + voterConfigTuple.
        getVoter().toString() + "and config attribute" + voterConfigTuple.getConfigAttribute().getAttribute());
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
