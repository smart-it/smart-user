/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.domain;

import com.smartitengineering.domain.AbstractGenericPersistentDTO;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author imyousuf
 */
public class UniqueKeyIndex extends AbstractGenericPersistentDTO<UniqueKeyIndex, String, Long> {

  private String objId;

  public String getObjId() {
    return objId;
  }

  public void setObjId(String objId) {
    this.objId = objId;
  }

  @Override
  public boolean isValid() {
    return StringUtils.isNotBlank(objId);
  }
}
