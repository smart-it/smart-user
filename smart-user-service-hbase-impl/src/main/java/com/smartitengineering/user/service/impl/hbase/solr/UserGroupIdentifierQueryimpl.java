/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.common.dao.search.solr.spi.ObjectIdentifierQuery;
import com.smartitengineering.user.domain.UserGroup;
import org.apache.solr.client.solrj.util.ClientUtils;

/**
 *
 * @author saumitra
 */
public class UserGroupIdentifierQueryimpl implements ObjectIdentifierQuery<UserGroup> {

  @Override
  public String getQuery(UserGroup object) {
    return new StringBuilder("id: userGroup\\:").append(ClientUtils.escapeQueryChars(object.getId().toString())).toString();
  }

}
