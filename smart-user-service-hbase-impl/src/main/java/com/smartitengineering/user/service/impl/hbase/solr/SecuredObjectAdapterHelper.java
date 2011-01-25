/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.impl.MultivalueMapImpl;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.service.Services;
import com.smartitengineering.util.bean.adapter.AbstractAdapterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author saumitra
 */
public class SecuredObjectAdapterHelper extends AbstractAdapterHelper<SecuredObject, MultivalueMap<String, Object>> {

  public static final String PREFIX = "securedObject:";
  private static final int PREFIX_INDEX = PREFIX.length();
  public static final Logger logger = LoggerFactory.getLogger(SecuredObjectAdapterHelper.class);

  @Override
  protected MultivalueMap<String, Object> newTInstance() {
    return new MultivalueMapImpl<String, Object>();
  }

  @Override
  protected void mergeFromF2T(SecuredObject fromBean,
                              MultivalueMap<String, Object> toBean) {
    toBean.addValue("id", new StringBuilder(PREFIX).append(fromBean.getId().toString()).toString());
    toBean.addValue("name", fromBean.getName().toString());
    toBean.addValue("organizationUniqueShortName", fromBean.getOrganization().getUniqueShortName().toString());
    if (fromBean.getParentOrganizationID() != null) {
      if (fromBean.getParentOrganizationID() != null){
      toBean.addValue("parentOrganization", fromBean.getParentOrganizationID().toString());
      }
    }
    else {
      toBean.addValue("parentOrganization", "null");
    }
    toBean.addValue("objectID", fromBean.getObjectID().toString());
    if (fromBean.getParentObjectID() != null) {
    toBean.addValue("parentObjectID", fromBean.getParentObjectID().toString());
    }
    else{
      toBean.addValue("parentObjectID", "NULL");
    }
    toBean.addValue("lastModifiedDate", fromBean.getLastModifiedDate().toString());
    toBean.addValue("creationDate", fromBean.getCreationDate().toString());
  }

  @Override
  protected SecuredObject convertFromT2F(MultivalueMap<String, Object> toBean) {
    return Services.getInstance().getSecuredObjectService().getById(Long.parseLong(toBean.getFirst("id").toString().substring(PREFIX_INDEX)));
  }
}
