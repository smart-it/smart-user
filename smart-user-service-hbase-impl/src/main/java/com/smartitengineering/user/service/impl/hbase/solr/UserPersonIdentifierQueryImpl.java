/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.common.dao.search.solr.spi.ObjectIdentifierQuery;
import com.smartitengineering.user.domain.UserPerson;
import org.apache.solr.client.solrj.util.ClientUtils;

/**
 *
 * @author saumitra
 */
public class UserPersonIdentifierQueryImpl implements ObjectIdentifierQuery<UserPerson>{

  @Override
  public String getQuery(UserPerson object) {
    return new StringBuilder("id: userPerson\\:").append(ClientUtils.escapeQueryChars(object.getId().toString())).toString();
  }

}
