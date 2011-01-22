/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.common.dao.search.solr.spi.ObjectIdentifierQuery;
import com.smartitengineering.user.domain.User;
import org.apache.solr.client.solrj.util.ClientUtils;

/**
 *
 * @author saumitra
 */
public class UserIdentifierQueryImpl implements ObjectIdentifierQuery<User>{

  @Override
  public String getQuery(User object) {
    return new StringBuilder("id: user\\:").append(ClientUtils.escapeQueryChars(object.getId().toString())).toString();
  }

}
