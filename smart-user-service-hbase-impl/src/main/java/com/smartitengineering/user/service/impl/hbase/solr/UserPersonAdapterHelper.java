/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.impl.MultivalueMapImpl;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.service.Services;
import com.smartitengineering.util.bean.adapter.AbstractAdapterHelper;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author saumitra
 */
public class UserPersonAdapterHelper extends AbstractAdapterHelper<UserPerson, MultivalueMap<String, Object>> {

  public static final String PREFIX = "userPerson:";
  private static final int PREFIX_INDEX = PREFIX.length();
  public static final Logger logger = LoggerFactory.getLogger(UserPersonAdapterHelper.class);

  @Override
  protected MultivalueMap<String, Object> newTInstance() {
    return new MultivalueMapImpl<String, Object>();
  }

  @Override
  protected void mergeFromF2T(UserPerson fromBean,
                              MultivalueMap<String, Object> toBean) {
    toBean.addValue("id", new StringBuilder(PREFIX).append(fromBean.getId().toString()).toString());
    toBean.addValue("name", fromBean.getPerson().getSelf().getName().toString());
    if(fromBean.getUser().getUsername() != null){
    logger.info(">>>>>>>>userName>>>>>"+fromBean.getUser().getUsername());
    }
    else{
      logger.info(">>>>>>>>1.userName is null>>>>>");
    }
    toBean.addValue("userName", fromBean.getUser().getUsername().toString());
    toBean.addValue("organizationUniqueShortName", fromBean.getUser().getOrganization().getUniqueShortName());
  }

  @Override
  protected UserPerson convertFromT2F(MultivalueMap<String, Object> toBean) {
    return Services.getInstance().getUserPersonService().getUserPersonByUsernameAndOrgName(toBean.getFirst("userName").toString(),toBean.getFirst("organizationUniqueShortName").toString());
  }
}
