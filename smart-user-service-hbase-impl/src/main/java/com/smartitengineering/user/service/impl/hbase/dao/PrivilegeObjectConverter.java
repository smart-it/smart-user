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
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.SecuredObjectService;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author imyousuf
 */
public class PrivilegeObjectConverter extends AbstractObjectRowConverter<Privilege, Long> {

  private static final byte[] FAMILY_SELF = Bytes.toBytes("self");
  private static final byte[] CELL_PARENT_ORG = Bytes.toBytes("parentOrg");
  private static final byte[] CELL_NAME = Bytes.toBytes("name");
  private static final byte[] CELL_DISPLAY_NAME = Bytes.toBytes("displayName");
  private static final byte[] CELL_SHORT_DESC = Bytes.toBytes("shortDescription");
  private static final byte[] CELL_SEC_OBJ = Bytes.toBytes("securedObject");
  private static final byte[] CELL_PERMISSION_MASK = Bytes.toBytes("permissionMask");
  private static final byte[] CELL_CREATION_DATE = Bytes.toBytes("creationDate");
  private static final byte[] CELL_LAST_MODIFIED_DATE = Bytes.toBytes("lastModifiedDate");
  @Inject
  private OrganizationService orgService;
  @Inject
  private SecuredObjectService secObjService;
  @Inject
  private SchemaInfoProvider<Organization, String> orgSchemaInfoProvider;
  @Inject
  private SchemaInfoProvider<SecuredObject, Long> secObjSchemaInfoProvider;

  @Override
  protected String[] getTablesToAttainLock() {
    return new String[]{getInfoProvider().getMainTableName()};
  }

  @Override
  protected void getPutForTable(Privilege instance, ExecutorService service, Put put) {
    put.add(FAMILY_SELF, CELL_CREATION_DATE, Utils.toBytes(instance.getCreationDate()));
    put.add(FAMILY_SELF, CELL_LAST_MODIFIED_DATE, Utils.toBytes(instance.getLastModifiedDate()));
    if (instance.getParentOrganization() != null) {
      try {
        put.add(FAMILY_SELF, CELL_PARENT_ORG,
                orgSchemaInfoProvider.getRowIdFromId(instance.getParentOrganization().getId()));
      }
      catch (Exception ex) {
        logger.warn("Could not convert parent organization of privilege", ex);
      }
    }
    if (instance.getSecuredObject() != null) {
      try {
        put.add(FAMILY_SELF, CELL_SEC_OBJ,
                secObjSchemaInfoProvider.getRowIdFromId(instance.getSecuredObject().getId()));
      }
      catch (Exception ex) {
        logger.warn("Could not convert secured objct of privilege", ex);
      }
    }
    if (StringUtils.isNotBlank(instance.getName())) {
      put.add(FAMILY_SELF, CELL_NAME, Bytes.toBytes(instance.getName()));
    }
    if (StringUtils.isNotBlank(instance.getDisplayName())) {
      put.add(FAMILY_SELF, CELL_DISPLAY_NAME, Bytes.toBytes(instance.getDisplayName()));
    }
    if (StringUtils.isNotBlank(instance.getShortDescription())) {
      put.add(FAMILY_SELF, CELL_SHORT_DESC, Bytes.toBytes(instance.getShortDescription()));
    }
    if (instance.getPermissionMask() != null) {
      put.add(FAMILY_SELF, CELL_PERMISSION_MASK, Bytes.toBytes(instance.getPermissionMask()));
    }
  }

  @Override
  protected void getDeleteForTable(Privilege instance, ExecutorService service, Delete put) {
    // Nothing is needed
  }

  @Override
  public Privilege rowsToObject(Result startRow, ExecutorService executorService) {
    try {
      Privilege privilege = new Privilege();
      privilege.setCreationDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_CREATION_DATE)));
      privilege.setLastModifiedDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_LAST_MODIFIED_DATE)));
      if (startRow.getValue(FAMILY_SELF, CELL_NAME) != null) {
        privilege.setName(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_NAME)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_DISPLAY_NAME) != null) {
        privilege.setDisplayName(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_DISPLAY_NAME)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_SHORT_DESC) != null) {
        privilege.setShortDescription(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_SHORT_DESC)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_PERMISSION_MASK) != null) {
        privilege.setPermissionMask(Bytes.toInt(startRow.getValue(FAMILY_SELF, CELL_PERMISSION_MASK)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_PARENT_ORG) != null) {
        String orgId = orgSchemaInfoProvider.getIdFromRowId(startRow.getValue(FAMILY_SELF, CELL_PARENT_ORG));
        privilege.setParentOrganization(orgService.getOrganizationByUniqueShortName(orgId));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_SEC_OBJ) != null) {
        Long secObjId = secObjSchemaInfoProvider.getIdFromRowId(startRow.getValue(FAMILY_SELF, CELL_SEC_OBJ));
        privilege.setSecuredObject(secObjService.getById(secObjId));
      }
      return privilege;
    }
    catch (Exception ex) {
      logger.error("Could not form Privilege", ex);
      return null;
    }
  }
}
