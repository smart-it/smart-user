/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.impl.MultivalueMapImpl;
import com.smartitengineering.user.domain.UserGroup;
import com.smartitengineering.util.bean.adapter.AbstractAdapterHelper;

/**
 *
 * @author saumitra
 */
public class UserGroupAdapterHelper extends AbstractAdapterHelper<UserGroup, MultivalueMap<String, Object>>{

  @Override
  protected MultivalueMap<String, Object> newTInstance() {
    return new MultivalueMapImpl<String, Object>();
  }

  @Override
  protected void mergeFromF2T(UserGroup fromBean,
                              MultivalueMap<String, Object> toBean) {
    
  }

  @Override
  protected UserGroup convertFromT2F(MultivalueMap<String, Object> toBean) {
    return null;
  }

}
