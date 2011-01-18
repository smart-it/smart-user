/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.common.dao.search.solr.spi.ObjectIdentifierQuery;
import com.smartitengineering.user.domain.Privilege;
import org.apache.solr.client.solrj.util.ClientUtils;

/**
 *
 * @author saumitra
 */
public class PrivilegeIdentifierQueryImpl implements ObjectIdentifierQuery<Privilege>{

  @Override
  public String getQuery(Privilege object) {
    return new StringBuilder("id: privilige\\:").append(ClientUtils.escapeQueryChars(object.getId().toString())).toString();
  }

}
