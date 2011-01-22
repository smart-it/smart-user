/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.solr;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.impl.MultivalueMapImpl;
import com.smartitengineering.user.service.Services;
import com.smartitengineering.util.bean.adapter.AbstractAdapterHelper;

/**
 *
 * @author saumitra
 */
public class OrganizationAdapterHelper extends AbstractAdapterHelper<Organization, MultivalueMap<String, Object>>{

  public static final String PREFIX = "org:";
  private static final int PREFIX_INDEX = PREFIX.length();
  @Override
  protected MultivalueMap<String, Object> newTInstance() {
    return new MultivalueMapImpl<String, Object>();
  }

  @Override
  protected void mergeFromF2T(Organization fromBean,
                              MultivalueMap<String, Object> toBean) {
    toBean.addValue("id", new StringBuilder(PREFIX).append(fromBean.getUniqueShortName()).toString());
    toBean.addValue("name", fromBean.getName().toString());
    toBean.addValue("address", new StringBuilder(fromBean.getAddress().getStreetAddress()).append(",").append(fromBean.getAddress().getCity()).append(",").append(fromBean.getAddress().getCountry()).toString());
    toBean.addValue("lastModifiedDate", fromBean.getLastModifiedDate().toString());
    toBean.addValue("creationDate", fromBean.getCreationDate().toString());
  }

  @Override
  protected Organization convertFromT2F(MultivalueMap<String, Object> toBean) {
   return Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(toBean.getFirst("id").toString().substring(PREFIX_INDEX));
  }
}
