/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.intercept.web.FilterInvocation;

/**
 *
 * @author modhu7
 */
public class OidRetrievalStrategyImpl implements OidRetrievalStrategy {

  private static Logger logger = LoggerFactory.getLogger(OidRetrievalStrategy.class);

  @Override
  public String getOid(Object object) {
    logger.info("getOid method is called");
    if (object instanceof FilterInvocation) {
      return ((FilterInvocation) object).getRequestUrl();
    }
    return "";
  }
}
