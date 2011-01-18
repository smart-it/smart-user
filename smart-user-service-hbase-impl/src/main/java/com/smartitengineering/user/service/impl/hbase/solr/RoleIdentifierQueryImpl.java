/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.common.dao.search.solr.spi.ObjectIdentifierQuery;
import com.smartitengineering.user.domain.Role;
import org.apache.solr.client.solrj.util.ClientUtils;

/**
 *
 * @author saumitra
 */
public class RoleIdentifierQueryImpl implements ObjectIdentifierQuery<Role>{

  @Override
  public String getQuery(Role object) {
    return new StringBuilder("id: role\\:").append(ClientUtils.escapeQueryChars(object.getId().toString())).toString();
  }

}
