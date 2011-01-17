/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.impl.MultivalueMapImpl;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.util.bean.adapter.AbstractAdapterHelper;

/**
 *
 * @author saumitra
 */
public class SecuredObjectAdapterHelper extends AbstractAdapterHelper<SecuredObject, MultivalueMap<String, Object>>{
  public static final String PREFIX = "securedObject:";
  private static final int PREFIX_INDEX = PREFIX.length();

  @Override
  protected MultivalueMap<String, Object> newTInstance() {
    return new MultivalueMapImpl<String, Object>();
  }

  @Override
  protected void mergeFromF2T(SecuredObject fromBean,
                              MultivalueMap<String, Object> toBean) {
    toBean.addValue("id", new StringBuilder(PREFIX).append(fromBean.getId().toString()).toString());
    toBean.addValue("name", fromBean.getName().toString());
    toBean.addValue("organization", fromBean.getOrganization().getName().toString());
    toBean.addValue("parentOrganization", fromBean.getParentOrganizationID().toString());
    toBean.addValue("objectID", fromBean.getObjectID().toString());
    toBean.addValue("parentObjectID", fromBean.getParentObjectID().toString());
    toBean.addValue("lastModifiedDate", fromBean.getLastModifiedDate().toString());
    toBean.addValue("creationDate", fromBean.getCreationDate().toString());
  }

  @Override
  protected SecuredObject convertFromT2F(MultivalueMap<String, Object> toBean) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
