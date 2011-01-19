/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.dao;

import com.google.inject.Inject;
import com.smartitengineering.dao.impl.hbase.spi.ExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.SchemaInfoProvider;
import com.smartitengineering.dao.impl.hbase.spi.impl.AbstractObjectRowConverter;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.service.OrganizationService;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author imyousuf
 */
public class SecuredObjectConverter extends AbstractObjectRowConverter<SecuredObject, Long> {

  private static final byte[] FAMILY_SELF = Bytes.toBytes("self");
  private static final byte[] CELL_PARENT_ORG = Bytes.toBytes("parentOrg");
  private static final byte[] CELL_NAME = Bytes.toBytes("name");
  private static final byte[] CELL_OBJ_ID = Bytes.toBytes("objId");
  private static final byte[] CELL_PARENT_OBJ_ID = Bytes.toBytes("parentObjId");
  private static final byte[] CELL_CREATION_DATE = Bytes.toBytes("creationDate");
  private static final byte[] CELL_LAST_MODIFIED_DATE = Bytes.toBytes("lastModifiedDate");
  @Inject
  private SchemaInfoProvider<Organization, String> orgSchemaInfoProvider;
  @Inject
  private OrganizationService organizationService;

  @Override
  protected String[] getTablesToAttainLock() {
    return new String[]{getInfoProvider().getMainTableName()};
  }

  @Override
  protected void getPutForTable(SecuredObject instance, ExecutorService service, Put put) {
    put.add(FAMILY_SELF, CELL_CREATION_DATE, Utils.toBytes(instance.getCreationDate()));
    put.add(FAMILY_SELF, CELL_LAST_MODIFIED_DATE, Utils.toBytes(instance.getLastModifiedDate()));
    if (StringUtils.isNotBlank(instance.getName())) {
      put.add(FAMILY_SELF, CELL_NAME, Bytes.toBytes(instance.getName()));
    }
    if (StringUtils.isNotBlank(instance.getObjectID())) {
      put.add(FAMILY_SELF, CELL_OBJ_ID, Bytes.toBytes(instance.getObjectID()));
    }
    if (StringUtils.isNotBlank(instance.getParentObjectID())) {
      put.add(FAMILY_SELF, CELL_PARENT_OBJ_ID, Bytes.toBytes(instance.getParentObjectID()));
    }
    final String orgId;
    if (instance.getOrganization() != null && StringUtils.isNotBlank(instance.getOrganization().getId())) {
      orgId = instance.getOrganization().getId();
    }
    else {
      orgId = instance.getParentOrganizationID();
    }
    if (StringUtils.isNotBlank(orgId)) {
      try {
        put.add(FAMILY_SELF, CELL_PARENT_ORG, orgSchemaInfoProvider.getRowIdFromId(orgId));
      }
      catch (Exception ex) {
        logger.warn("Could not convert parent org of secured object", ex);
      }
    }
  }

  @Override
  protected void getDeleteForTable(SecuredObject instance, ExecutorService service, Delete put) {
    // Nothing is needed
  }

  @Override
  public SecuredObject rowsToObject(Result startRow, ExecutorService executorService) {
    try {
      SecuredObject securedObject = new SecuredObject();
      securedObject.setId(getInfoProvider().getIdFromRowId(startRow.getRow()));
      securedObject.setCreationDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_CREATION_DATE)));
      securedObject.setLastModifiedDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_LAST_MODIFIED_DATE)));
      if (startRow.getValue(FAMILY_SELF, CELL_NAME) != null) {
        securedObject.setName(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_NAME)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_OBJ_ID) != null) {
        securedObject.setObjectID(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_OBJ_ID)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_PARENT_OBJ_ID) != null) {
        securedObject.setParentObjectID(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_PARENT_OBJ_ID)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_PARENT_ORG) != null) {
        securedObject.setParentOrganizationID(orgSchemaInfoProvider.getIdFromRowId(startRow.getValue(FAMILY_SELF,
                                                                                                     CELL_PARENT_ORG)));
        if (StringUtils.isNotBlank(securedObject.getParentOrganizationID())) {
          securedObject.setOrganization(organizationService.getOrganizationByUniqueShortName(securedObject.
              getParentOrganizationID()));
        }
      }
      return securedObject;
    }
    catch (Exception ex) {
      logger.error("Could not form SecuredObject", ex);
      return null;
    }
  }
}
