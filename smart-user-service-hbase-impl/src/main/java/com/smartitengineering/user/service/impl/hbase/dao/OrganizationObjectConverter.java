/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.dao;

import com.smartitengineering.dao.impl.hbase.spi.ExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.impl.AbstractObjectRowConverter;
import com.smartitengineering.user.domain.Organization;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author imyousuf
 */
public class OrganizationObjectConverter extends AbstractObjectRowConverter<Organization, String> {

  private static final byte[] FAMILY_SELF = Bytes.toBytes("self");
  private static final byte[] CELL_NAME = Bytes.toBytes("name");
  private static final byte[] CELL_CREATION_DATE = Bytes.toBytes("creationDate");
  private static final byte[] CELL_LAST_MODIFIED_DATE = Bytes.toBytes("lastModifiedDate");
  private static final byte[] CELL_ADDRESS_STREET = Bytes.toBytes("address.street");
  private static final byte[] CELL_ADDRESS_CITY = Bytes.toBytes("address.street");
  private static final byte[] CELL_ADDRESS_STATE = Bytes.toBytes("address.state");
  private static final byte[] CELL_ADDRESS_ZIP = Bytes.toBytes("address.zip");
  private static final byte[] CELL_ADDRESS_COUNTRY = Bytes.toBytes("address.country");
  private static final byte[] CELL_ADDRESS_GEO_LOCATION_LATITUDE = Bytes.toBytes("address.geoLocation.latitude");
  private static final byte[] CELL_ADDRESS_GEO_LOCATION_LONGITUDE = Bytes.toBytes("address.geoLocation.longitude");
  private static final List<byte[]> ADDRESS_CELL_LIST = Arrays.asList(CELL_ADDRESS_STREET, CELL_ADDRESS_CITY,
                                                                      CELL_ADDRESS_STATE, CELL_ADDRESS_ZIP,
                                                                      CELL_ADDRESS_COUNTRY,
                                                                      CELL_ADDRESS_GEO_LOCATION_LATITUDE,
                                                                      CELL_ADDRESS_GEO_LOCATION_LONGITUDE);

  @Override
  protected String[] getTablesToAttainLock() {
    return new String[]{getInfoProvider().getMainTableName()};
  }

  @Override
  protected void getPutForTable(Organization instance, ExecutorService service, Put put) {
    if (StringUtils.isNotBlank(instance.getName())) {
      put.add(FAMILY_SELF, CELL_NAME, Bytes.toBytes(instance.getName()));
    }
    put.add(FAMILY_SELF, CELL_CREATION_DATE, Utils.toBytes(instance.getCreationDate()));
    put.add(FAMILY_SELF, CELL_LAST_MODIFIED_DATE, Utils.toBytes(instance.getLastModifiedDate()));
    if (instance.getAddress() != null) {
      ConvertionUtils.fillWithAddress(instance.getAddress(), ADDRESS_CELL_LIST, FAMILY_SELF, put);
    }
  }

  @Override
  protected void getDeleteForTable(Organization instance, ExecutorService service, Delete put) {
    // Nothing is needed
  }

  @Override
  public Organization rowsToObject(Result startRow, ExecutorService executorService) {
    try {
      Organization organization = new Organization();
      organization.setId(getInfoProvider().getIdFromRowId(startRow.getRow()));
      organization.setAddress(ConvertionUtils.formAddress(startRow, FAMILY_SELF, ADDRESS_CELL_LIST));
      if (startRow.getValue(FAMILY_SELF, CELL_NAME) != null) {
        organization.setName(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_NAME)));
      }
      organization.setCreationDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_CREATION_DATE)));
      organization.setLastModifiedDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_LAST_MODIFIED_DATE)));
      return organization;
    }
    catch (Exception ex) {
      logger.error("Could not form organization", ex);
      return null;
    }
  }
}
