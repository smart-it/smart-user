/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.impl.MultivalueMapImpl;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.util.bean.adapter.AbstractAdapterHelper;

/**
 *
 * @author saumitra
 */
public class UserPersonAdapterHelper extends AbstractAdapterHelper<UserPerson, MultivalueMap<String, Object>>{

  public static final String PREFIX = "userPerson:";
  private static final int PREFIX_INDEX = PREFIX.length();
  @Override
  protected MultivalueMap<String, Object> newTInstance() {
    return new MultivalueMapImpl<String, Object>();
  }

  @Override
  protected void mergeFromF2T(UserPerson fromBean,
                              MultivalueMap<String, Object> toBean) {
    toBean.addValue("id", new StringBuilder(PREFIX).append(fromBean.getId().toString()).toString());
    toBean.addValue("name", fromBean.getPerson().getSelf().getName().toString());
    toBean.addValue("userName", fromBean.getUser().getUsername().toString());
    toBean.addValue("nationalId", fromBean.getPerson().getSelf().getNationalID().toString());
    toBean.addValue("fatherName", fromBean.getPerson().getFather().getName().toString());
    toBean.addValue("sposeName", fromBean.getPerson().getSpouse().getName().toString());
    toBean.addValue("email", fromBean.getPerson().getPrimaryEmail().toString());
    toBean.addValue("cellNo", fromBean.getPerson().getCellPhoneNumber().toString());
    toBean.addValue("organization", fromBean.getUser().getOrganization().toString());
    toBean.addValue("lastModifiedDate", fromBean.getLastModifiedDate().toString());
    toBean.addValue("creationDate", fromBean.getCreationDate().toString());
    toBean.addValue("address", new StringBuilder(fromBean.getPerson().getAddress().getStreetAddress()).append(",").append(fromBean.getPerson().getAddress().getCity()).append(",").append(fromBean.getPerson().getAddress().getCountry()).toString());
  }

  @Override
  protected UserPerson convertFromT2F(MultivalueMap<String, Object> toBean) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
