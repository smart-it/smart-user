/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.hbase.dao;

import com.smartitengineering.dao.impl.hbase.spi.ExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.impl.AbstractObjectRowConverter;
import com.smartitengineering.user.domain.UserPerson;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author modhu7
 */
public class UserPersonObjectConverter extends AbstractObjectRowConverter<UserPerson, Long> {

  private static final byte[] FAMILY_SELF = Bytes.toBytes("self");
  private static final byte[] CELL_USER_ID = Bytes.toBytes("user");
  private static final byte[] CELL_PERSON_ID = Bytes.toBytes("person");
  private static final byte[] CELL_CREATION_DATE = Bytes.toBytes("creationDate");
  private static final byte[] CELL_LAST_MODIFIED_DATE = Bytes.toBytes("lastModifiedDate");
  @Override
  protected String[] getTablesToAttainLock() {
    return new String[]{getInfoProvider().getMainTableName()};
  }

  @Override
  protected void getPutForTable(UserPerson instance, ExecutorService executorService, Put put) {
    if (instance.getUser().getId() != null) {
      put.add(FAMILY_SELF, CELL_USER_ID, Bytes.toBytes(instance.getUser().getId()));
    }
    if (instance.getPerson().getId() != null) {
      put.add(FAMILY_SELF, CELL_USER_ID, Bytes.toBytes(instance.getPerson().getId()));
    }
    put.add(FAMILY_SELF, CELL_CREATION_DATE, Utils.toBytes(instance.getCreationDate()));
    put.add(FAMILY_SELF, CELL_LAST_MODIFIED_DATE, Utils.toBytes(instance.getLastModifiedDate()));
    
  }

  @Override
  protected void getDeleteForTable(UserPerson t, ExecutorService es, Delete delete) {
    //Nothing is nedded
  }

  @Override
  public UserPerson rowsToObject(Result startRow, ExecutorService executorService) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
