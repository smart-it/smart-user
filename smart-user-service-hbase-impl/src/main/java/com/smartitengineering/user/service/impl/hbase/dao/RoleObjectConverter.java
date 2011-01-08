/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.dao;

import com.smartitengineering.dao.impl.hbase.spi.ExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.impl.AbstractObjectRowConverter;
import com.smartitengineering.user.domain.Role;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author imyousuf
 */
public class RoleObjectConverter extends AbstractObjectRowConverter<Role, Long> {

  private static final byte[] FAMILY_SELF = Bytes.toBytes("self");
  private static final byte[] CELL_NAME = Bytes.toBytes("name");
  private static final byte[] CELL_DISPLAY_NAME = Bytes.toBytes("displayName");
  private static final byte[] CELL_SHORT_DESC = Bytes.toBytes("shortDescription");
  private static final byte[] CELL_CREATION_DATE = Bytes.toBytes("creationDate");
  private static final byte[] CELL_LAST_MODIFIED_DATE = Bytes.toBytes("lastModifiedDate");

  @Override
  protected String[] getTablesToAttainLock() {
    return new String[]{getInfoProvider().getMainTableName()};
  }

  @Override
  protected void getPutForTable(Role instance, ExecutorService service, Put put) {
    put.add(FAMILY_SELF, CELL_CREATION_DATE, Utils.toBytes(instance.getCreationDate()));
    put.add(FAMILY_SELF, CELL_LAST_MODIFIED_DATE, Utils.toBytes(instance.getLastModifiedDate()));
    if (StringUtils.isNotBlank(instance.getName())) {
      put.add(FAMILY_SELF, CELL_NAME, Bytes.toBytes(instance.getName()));
    }
    if (StringUtils.isNotBlank(instance.getDisplayName())) {
      put.add(FAMILY_SELF, CELL_DISPLAY_NAME, Bytes.toBytes(instance.getDisplayName()));
    }
    if (StringUtils.isNotBlank(instance.getShortDescription())) {
      put.add(FAMILY_SELF, CELL_SHORT_DESC, Bytes.toBytes(instance.getShortDescription()));
    }
  }

  @Override
  protected void getDeleteForTable(Role instance, ExecutorService service, Delete put) {
    // Nothing is needed
  }

  @Override
  public Role rowsToObject(Result startRow, ExecutorService executorService) {
    try {
      Role role = new Role();
      role.setCreationDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_CREATION_DATE)));
      role.setLastModifiedDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_LAST_MODIFIED_DATE)));
      if (startRow.getValue(FAMILY_SELF, CELL_NAME) != null) {
        role.setName(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_NAME)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_DISPLAY_NAME) != null) {
        role.setDisplayName(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_DISPLAY_NAME)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_SHORT_DESC) != null) {
        role.setShortDescription(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_SHORT_DESC)));
      }
      return role;
    }
    catch (Exception ex) {
      logger.error("Could not form Privilege", ex);
      return null;
    }
  }
}
