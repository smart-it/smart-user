/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.impl.MultivalueMapImpl;
import com.smartitengineering.user.domain.UserGroup;
import com.smartitengineering.user.service.Services;
import com.smartitengineering.util.bean.adapter.AbstractAdapterHelper;

/**
 *
 * @author saumitra
 */
public class UserGroupAdapterHelper extends AbstractAdapterHelper<UserGroup, MultivalueMap<String, Object>>{

  public static final String PREFIX = "userGroup:";
  private static final int PREFIX_INDEX = PREFIX.length();
  @Override
  protected MultivalueMap<String, Object> newTInstance() {
    return new MultivalueMapImpl<String, Object>();
  }

  @Override
  protected void mergeFromF2T(UserGroup fromBean,
                              MultivalueMap<String, Object> toBean) {
    toBean.addValue("id", new StringBuilder(PREFIX).append(fromBean.getId().toString()).toString());
    toBean.addValue("organizationUniqueShortName", fromBean.getOrganization().getUniqueShortName());
    toBean.addValue("name", fromBean.getName());
  }

  @Override
  protected UserGroup convertFromT2F(MultivalueMap<String, Object> toBean) {
    return Services.getInstance().getUserGroupService().getByOrganizationAndUserGroupName(toBean.getFirst("organizationUniqueShortName").toString(),toBean.getFirst("name").toString());
  }
}
