/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.common.dao.search.solr.spi.ObjectIdentifierQuery;
import com.smartitengineering.user.domain.SecuredObject;
import org.apache.solr.client.solrj.util.ClientUtils;

/**
 *
 * @author saumitra
 */
public class SecuredObjectIdentifierQueryImpl implements ObjectIdentifierQuery<SecuredObject>{

  @Override
  public String getQuery(SecuredObject object) {
    return new StringBuilder("id: securedObject\\:").append(ClientUtils.escapeQueryChars(object.getId().toString())).toString();
  }

}
