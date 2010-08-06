/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.security;

import java.util.List;
import org.springframework.security.vote.AccessDecisionVoter;

/**
 *
 * @author modhu7
 */
public interface VotingConfigProvider {

    public List<VotingConfig> getVotingConfigList();

}
