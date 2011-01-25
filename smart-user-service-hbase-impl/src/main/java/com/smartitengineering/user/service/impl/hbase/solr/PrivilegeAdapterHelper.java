/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.solr;

import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.impl.MultivalueMapImpl;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.service.Services;
import com.smartitengineering.util.bean.adapter.AbstractAdapterHelper;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author saumitra
 */
public class PrivilegeAdapterHelper extends AbstractAdapterHelper<Privilege, MultivalueMap<String, Object>> {

  public static final String PREFIX = "privilege:";
  private static final int PREFIX_INDEX = PREFIX.length();
  public static final Logger logger = LoggerFactory.getLogger(PrivilegeAdapterHelper.class);

  @Override
  protected MultivalueMap<String, Object> newTInstance() {
    return new MultivalueMapImpl<String, Object>();
  }

  @Override
  protected void mergeFromF2T(Privilege fromBean,
                              MultivalueMap<String, Object> toBean) {
    toBean.addValue("id", new StringBuilder(PREFIX).append(fromBean.getId().toString()).toString());
    toBean.addValue("name", fromBean.getName().toString());
    toBean.addValue("displayName", fromBean.getDisplayName().toString());
    toBean.addValue("parentOrganization", fromBean.getParentOrganization().getUniqueShortName().toString());
    if (fromBean.getSecuredObject() != null) {
      logger.info(">>>>>>>>>>>>parentOrganization>>>>>>>>>>>>>>> " +
          fromBean.getSecuredObject().getObjectID().toString());
      toBean.addValue("objectID", fromBean.getSecuredObject().getObjectID().toString());
    }
    else {
      toBean.addValue("objectID", "NULL_OBJ_ID");

    }
    toBean.addValue("permissionMask", fromBean.getPermissionMask());
    toBean.addValue("lastModifiedDate", fromBean.getLastModifiedDate().toString());
    toBean.addValue("creationDate", fromBean.getCreationDate().toString());
  }

  @Override
  protected Privilege convertFromT2F(MultivalueMap<String, Object> toBean) {
    logger.info(">>>>>>>>privileges are>>>>>>>>>"+ toBean.getFirst("id"));
    final Set<Privilege> privilegesByIds =
                         Services.getInstance().getPrivilegeService().getPrivilegesByIds(Long.parseLong(toBean.getFirst(
        "id").toString().substring(PREFIX_INDEX)));
    logger.info(">>>>>>>>privileges are>>>>>>>>>"+ privilegesByIds.iterator().next());
    if (privilegesByIds == null || privilegesByIds.isEmpty()) {
      return null;
    }
    return privilegesByIds.iterator().next();
  }
}
