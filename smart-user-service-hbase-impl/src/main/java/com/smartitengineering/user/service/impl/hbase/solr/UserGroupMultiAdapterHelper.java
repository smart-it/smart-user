/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.impl.MultivalueMapImpl;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserGroup;
import com.smartitengineering.util.bean.adapter.AbstractAdapterHelper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author saumitra
 */
public class UserGroupMultiAdapterHelper extends AbstractAdapterHelper<UserGroup, List<MultivalueMap<String, Object>>>{

  public static final String PREFIX = "userGroup:";
  private static final int PREFIX_INDEX = PREFIX.length();
  @Override
  protected List<MultivalueMap<String, Object>> newTInstance() {
    return new ArrayList<MultivalueMap<String, Object>>();
  }

  @Override
  protected void mergeFromF2T(UserGroup fromBean,
                              List<MultivalueMap<String, Object>> toBean) {
    int index = 1;
    for (User user : fromBean.getUsers()) {
      MultivalueMap<String, Object> multiValueMap = new MultivalueMapImpl<String, Object>();
      multiValueMap.addValue("id", (new StringBuilder(PREFIX).append(fromBean.getId().toString()).append(":").append(index)).toString());
      multiValueMap.addValue("userName", user.getUsername());
      multiValueMap.addValue("organization", user.getOrganization());
      toBean.add(multiValueMap);
      index ++;
    }
  }

  @Override
  protected UserGroup convertFromT2F(List<MultivalueMap<String, Object>> toBean) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
