/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.dao;

import com.smartitengineering.dao.impl.hbase.spi.ExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.impl.AbstractObjectRowConverter;
import com.smartitengineering.user.service.impl.hbase.domain.AutoId;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author imyousuf
 */
public class AutoIdObjectConverter extends AbstractObjectRowConverter<AutoId, String> {

  private static final byte[] FAMILY_SELF = Bytes.toBytes("self");
  private static final byte[] CELL_VALUE = Bytes.toBytes("value");

  @Override
  protected String[] getTablesToAttainLock() {
    return new String[]{getInfoProvider().getMainTableName()};
  }

  @Override
  protected void getPutForTable(AutoId instance, ExecutorService service, Put put) {
    put.add(FAMILY_SELF, CELL_VALUE, Bytes.toBytes(instance.getValue()));
  }

  @Override
  protected void getDeleteForTable(AutoId instance, ExecutorService service, Delete put) {
    // Nothing is needed
  }

  @Override
  public AutoId rowsToObject(Result startRow, ExecutorService executorService) {
    try {
      AutoId index = new AutoId();
      index.setId(getInfoProvider().getIdFromRowId(startRow.getRow()));
      index.setValue(Bytes.toLong(startRow.getValue(FAMILY_SELF, CELL_VALUE)));
      return index;
    }
    catch (Exception ex) {
      logger.error("Could not form AutoId", ex);
      return null;
    }
  }
}
