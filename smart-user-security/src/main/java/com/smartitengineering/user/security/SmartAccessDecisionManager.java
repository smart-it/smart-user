/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security;

import java.util.Iterator;
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

  @Override
  public void decide(Authentication authentication, Object object, ConfigAttributeDefinition config)
      throws AccessDeniedException {

    int grant = 0;
    int abstain = 0;

    Iterator configIter = config.getConfigAttributes().iterator();

    while (configIter.hasNext()) {
      ConfigAttributeDefinition singleAttrDef =
                                new ConfigAttributeDefinition((ConfigAttribute) configIter.next());

      Iterator voters = this.getDecisionVoters().iterator();

      while (voters.hasNext()) {
        AccessDecisionVoter voter = (AccessDecisionVoter) voters.next();

        if (voter instanceof RoleVoter) {
          int result = voter.vote(authentication, object, singleAttrDef);
          if (result == AccessDecisionVoter.ACCESS_GRANTED) {
            return;
          }
        }
      }

      while (voters.hasNext()) {
        AccessDecisionVoter voter = (AccessDecisionVoter) voters.next();

        if (voter instanceof SmartUserAdminVoter) {
          int result = voter.vote(authentication, object, singleAttrDef);

          switch (result) {
            case AccessDecisionVoter.ACCESS_GRANTED:
              grant++;

              break;

            case AccessDecisionVoter.ACCESS_DENIED:
              throw new AccessDeniedException(messages.getMessage("AbstractAccessDecisionManager.accessDenied",
                                                                  "Access is denied"));

            default:
              abstain++;

              break;
          }
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
}
