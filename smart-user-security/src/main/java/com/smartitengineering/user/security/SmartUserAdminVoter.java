/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security;

import com.smartitengineering.user.service.AuthorizationService;
import com.smartitengineering.user.service.Services;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.acls.sid.Sid;
import org.springframework.security.acls.sid.SidRetrievalStrategy;
import org.springframework.security.vote.AccessDecisionVoter;

/**
 *
 * @author modhu7
 */
public class SmartUserAdminVoter implements AccessDecisionVoter {

  private VotingConfigProvider votingConfigProvider;
  private OidRetrievalStrategy oidRetrievalStrategy;
  private SidRetrievalStrategy sidRetrievalStrategy;
  private static Logger logger = LoggerFactory.getLogger(SmartUserAdminVoter.class);
  

  public AuthorizationService getAuthorizationService() {
    return Services.getInstance().getAuthorizationService();
  }

  public OidRetrievalStrategy getOidRetrievalStrategy() {
    return oidRetrievalStrategy;
  }

  public void setOidRetrievalStrategy(OidRetrievalStrategy oidRetrievalStrategy) {
    this.oidRetrievalStrategy = oidRetrievalStrategy;
  }

  public SidRetrievalStrategy getSidRetrievalStrategy() {
    return sidRetrievalStrategy;
  }

  public void setSidRetrievalStrategy(SidRetrievalStrategy sidRetrievalStrategy) {
    this.sidRetrievalStrategy = sidRetrievalStrategy;
  }

  public VotingConfigProvider getVotingConfigProvider() {
    return votingConfigProvider;
  }

  public void setVotingConfigProvider(VotingConfigProvider votingConfigProvider) {
    this.votingConfigProvider = votingConfigProvider;
  }

  @Override
  public boolean supports(ConfigAttribute ca) {
    if (getVotingConfig(ca).getProcessConfigAttribute() == null) {
      return false;
    }
    return true;
  }

  @Override
  public boolean supports(Class type) {
    return true;
  }

  @Override
  public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {

    logger.info("vote method is called: " + authentication.getName() + ", Total number of config attributes: " + config.getConfigAttributes().size() );

    Iterator iter = config.getConfigAttributes().iterator();

    while (iter.hasNext()) {
      ConfigAttribute attr = (ConfigAttribute) iter.next();

      if (!this.supports(attr)) {
        continue;
      }
      // Need to make an access decision on this invocation
      // Attempt to locate the domain object instance to process\

      logger.info("Call authorize method");
      return authorize(authentication, object, getVotingConfig(attr));

    }

    // No configuration attribute matched, so abstain
    return AccessDecisionVoter.ACCESS_ABSTAIN;
  }

  private VotingConfig getVotingConfig(ConfigAttribute attr) {
    for (VotingConfig votingConfig : votingConfigProvider.getVotingConfigList()) {
      if (votingConfig.getProcessConfigAttribute().getAttribute().equals(attr.getAttribute())) {
        return votingConfig;
      }
    }
    return new VotingConfig();
  }

  private int authorize(Authentication authentication, Object object, VotingConfig votingConfig) {
    logger.info("Start authorize method: " + authentication.getName() + ", config: " + votingConfig.toString());
    String oid = oidRetrievalStrategy.getOid(object);
    Sid[] sids = sidRetrievalStrategy.getSids(authentication);
    UserSid sid = (UserSid) sids[0];
    // If domain object is null, vote to abstain
    if (object == null) {
      logger.info("Object is null, Access abstain");
      return AccessDecisionVoter.ACCESS_ABSTAIN;
    }
    if (sid == null) {
      logger.info("sid is null, Access denied");
      return AccessDecisionVoter.ACCESS_DENIED;
    }

    // Obtain the OID applicable to the domain object
    return getAuthorizationService().authorize(sid.getUsername(), sid.getOrganizationName(), oid, votingConfig.
        getRequirePermission()[0].getMask());
  }
}
