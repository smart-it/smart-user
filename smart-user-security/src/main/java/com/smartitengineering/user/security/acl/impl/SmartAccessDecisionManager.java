/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security.acl.impl;

import com.smartitengineering.user.security.VoterListProviderImpl;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.vote.AccessDecisionVoter;
import org.springframework.security.vote.RoleVoter;
import org.springframework.security.vote.UnanimousBased;

/**
 *
 * @author modhu7
 */
public class SmartAccessDecisionManager extends UnanimousBased {

    public SmartAccessDecisionManager() {
        super();
        System.out.println("Constructor is running");
        VoterListProviderImpl voterListProvider = new VoterListProviderImpl("voterlist.xml");
        List<AccessDecisionVoter> listVoter = new ArrayList<AccessDecisionVoter>(voterListProvider.getListVoter());
        listVoter.add(new RoleVoter());
        setDecisionVoters(listVoter);
    }
}
