/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.dao;

import com.smartitengineering.dao.impl.hbase.spi.ExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.impl.AbstractObjectRowConverter;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKey;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKeyIndex;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author imyousuf
 */
public class UniqueKeyIndexObjectConverter extends AbstractObjectRowConverter<UniqueKeyIndex, UniqueKey> {

  private static final byte[] FAMILY_SELF = Bytes.toBytes("self");
  private static final byte[] CELL_OBJ_ID = Bytes.toBytes("personId");

  @Override
  protected String[] getTablesToAttainLock() {
    return new String[]{getInfoProvider().getMainTableName()};
  }

  @Override
  protected void getPutForTable(UniqueKeyIndex instance, ExecutorService service, Put put) {
    put.add(FAMILY_SELF, CELL_OBJ_ID, Bytes.toBytes(instance.getObjId()));
  }

  @Override
  protected void getDeleteForTable(UniqueKeyIndex instance, ExecutorService service, Delete put) {
    // Nothing needed to be done
  }

  @Override
  public UniqueKeyIndex rowsToObject(Result startRow, ExecutorService executorService) {
    try {
      UniqueKeyIndex index = new UniqueKeyIndex();
      index.setId(getInfoProvider().getIdFromRowId(startRow.getRow()));
      index.setObjId(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_OBJ_ID)));
      return index;
    }
    catch (Exception ex) {
      logger.error("Could not form UniqueKeyIndex", ex);
      return null;
    }
  }
}
