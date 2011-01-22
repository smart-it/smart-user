/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.impl.MultivalueMapImpl;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.service.Services;
import com.smartitengineering.util.bean.adapter.AbstractAdapterHelper;

/**
 *
 * @author saumitra
 */
public class UserAdapterHelper extends AbstractAdapterHelper<User, MultivalueMap<String, Object>>{

  public static final String PREFIX = "user:";
  private static final int PREFIX_INDEX = PREFIX.length();

  @Override
  protected MultivalueMap<String, Object> newTInstance() {
    return new MultivalueMapImpl<String, Object>();
  }

  @Override
  protected void mergeFromF2T(User fromBean,
                              MultivalueMap<String, Object> toBean) {
    toBean.addValue("id", new StringBuilder(PREFIX).append(fromBean.getId().toString()).toString());
    toBean.addValue("userName", fromBean.getUsername().toString());
    toBean.addValue("organization", fromBean.getOrganization().toString());
    toBean.addValue("lastModifiedDate", fromBean.getLastModifiedDate().toString());
    toBean.addValue("creationDate", fromBean.getCreationDate().toString());
  }

  @Override
  protected User convertFromT2F(MultivalueMap<String, Object> toBean) {
    return Services.getInstance().getUserService().getById(Long.parseLong(toBean.getFirst("id").toString().substring(PREFIX_INDEX)));
  }
}
