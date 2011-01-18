/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;
import com.smartitengineering.common.dao.search.solr.spi.ObjectIdentifierQuery;
import com.smartitengineering.user.domain.Organization;
import org.apache.solr.client.solrj.util.ClientUtils;

/**
 *
 * @author saumitra
 */
public class OrganizationIdentifierQueryImpl implements ObjectIdentifierQuery<Organization>{

  @Override
  public String getQuery(Organization object) {
    return new StringBuilder("id: org\\:").append(ClientUtils.escapeQueryChars(object.getId().toString())).toString();
  }
}
