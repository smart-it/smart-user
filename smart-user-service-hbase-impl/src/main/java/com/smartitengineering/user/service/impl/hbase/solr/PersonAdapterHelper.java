/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.impl.MultivalueMapImpl;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.service.Services;
import com.smartitengineering.util.bean.adapter.AbstractAdapterHelper;

/**
 *
 * @author saumitra
 */
public class PersonAdapterHelper extends AbstractAdapterHelper<Person, MultivalueMap<String, Object>>{

  public static final String PREFIX = "person:";
  private static final int PREFIX_INDEX = PREFIX.length();
  @Override
  protected MultivalueMap<String, Object> newTInstance() {
    return new MultivalueMapImpl<String, Object>();
  }

  @Override
  protected void mergeFromF2T(Person fromBean,
                              MultivalueMap<String, Object> toBean) {
    toBean.addValue("id", new StringBuilder(PREFIX).append(fromBean.getId().toString()).toString());
    toBean.addValue("name", fromBean.getSelf().getName().toString());
    toBean.addValue("nationalId", fromBean.getSelf().getNationalID().toString());
    toBean.addValue("fatherName", fromBean.getFather().getName().toString());
    toBean.addValue("sposeName", fromBean.getSpouse().getName().toString());
    toBean.addValue("email", fromBean.getPrimaryEmail().toString());
    toBean.addValue("cellNo", fromBean.getCellPhoneNumber().toString());
    toBean.addValue("address", new StringBuilder(fromBean.getAddress().getStreetAddress()).append(",").append(fromBean.getAddress().getCity()).append(",").append(fromBean.getAddress().getCountry()).toString());
  }

  @Override
  protected Person convertFromT2F(MultivalueMap<String, Object> toBean) {
    return Services.getInstance().getPersonService().getById(Long.parseLong(toBean.getFirst("id").toString().substring(PREFIX_INDEX)));
  }

}
