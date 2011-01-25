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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author saumitra
 */
public class PersonAdapterHelper extends AbstractAdapterHelper<Person, MultivalueMap<String, Object>> {

  public static final String PREFIX = "person:";
  private static final int PREFIX_INDEX = PREFIX.length();
  public static final Logger logger = LoggerFactory.getLogger(PersonAdapterHelper.class);

  @Override
  protected MultivalueMap<String, Object> newTInstance() {
    return new MultivalueMapImpl<String, Object>();
  }

  @Override
  protected void mergeFromF2T(Person fromBean,
                              MultivalueMap<String, Object> toBean) {
    toBean.addValue("id", new StringBuilder(PREFIX).append(fromBean.getId().toString()).toString());
    if (fromBean.getSelf() != null) {
      if (fromBean.getSelf().getName() != null) {
        toBean.addValue("name", fromBean.getSelf().getName().toString());
      }
      if (StringUtils.isNotBlank(fromBean.getSelf().getNationalID())) {
        toBean.addValue("nationalId", fromBean.getSelf().getNationalID().toString());
      }
    }
    if (fromBean.getFather() != null) {
      if (fromBean.getFather().getName() != null) {
        toBean.addValue("fatherName", fromBean.getFather().getName().toString());
      }
      if (StringUtils.isNotBlank(fromBean.getFather().getNationalID())) {
        toBean.addValue("fatherNationalId", fromBean.getFather().getNationalID().toString());
      }
    }
    if (fromBean.getFather() != null) {
      if (fromBean.getFather().getName() != null) {
        toBean.addValue("sposeName", fromBean.getSpouse().getName().toString());
      }
    }
    if (StringUtils.isNotBlank(fromBean.getPrimaryEmail())) {
      toBean.addValue("email", fromBean.getPrimaryEmail().toString());
    }
    if (StringUtils.isNotBlank(fromBean.getCellPhoneNumber())) {
      toBean.addValue("cellNo", fromBean.getCellPhoneNumber().toString());
    }
    if (StringUtils.isNotBlank(fromBean.getAddress().getStreetAddress())) {
    toBean.addValue("streetAddress", fromBean.getAddress().getStreetAddress());
    }
    if (StringUtils.isNotBlank(fromBean.getAddress().getCity())) {
    toBean.addValue("city", fromBean.getAddress().getCity());
    }
    if (StringUtils.isNotBlank(fromBean.getAddress().getCountry())) {
    toBean.addValue("country", fromBean.getAddress().getCountry());
    }
  }

  @Override
  protected Person convertFromT2F(MultivalueMap<String, Object> toBean) {
    return Services.getInstance().getPersonService().getById(Long.parseLong(toBean.getFirst("id").toString().substring(
        PREFIX_INDEX)));
  }
}
