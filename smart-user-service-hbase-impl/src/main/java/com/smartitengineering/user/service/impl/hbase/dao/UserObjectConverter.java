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
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.RoleService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author imyousuf
 */
public class UserObjectConverter extends AbstractObjectRowConverter<User, Long> {

  private static final byte[] FAMILY_SELF = Bytes.toBytes("self");
  private static final byte[] FAMILY_ROLES = Bytes.toBytes("roles");
  private static final byte[] FAMILY_PRIVILEGES = Bytes.toBytes("privileges");
  private static final byte[] CELL_PARENT_ORG = Bytes.toBytes("parentOrg");
  private static final byte[] CELL_CREATION_DATE = Bytes.toBytes("creationDate");
  private static final byte[] CELL_LAST_MODIFIED_DATE = Bytes.toBytes("lastModifiedDate");
  private static final byte[] CELL_USERNAME = Bytes.toBytes("username");
  private static final byte[] CELL_PASSWORD = Bytes.toBytes("password");
  @Inject
  private OrganizationService orgService;
  @Inject
  private SchemaInfoProvider<Organization, String> orgSchemaInfoProvider;
  @Inject
  private RoleService roleService;
  @Inject
  private SchemaInfoProvider<Role, Long> roleSchemaInfoProvider;
  @Inject
  private PrivilegeService privilegeService;
  @Inject
  private SchemaInfoProvider<Privilege, Long> privSchemaInfoProvider;

  @Override
  protected String[] getTablesToAttainLock() {
    return new String[]{getInfoProvider().getMainTableName()};
  }

  @Override
  protected void getPutForTable(User instance, ExecutorService service, Put put) {
    put.add(FAMILY_SELF, CELL_CREATION_DATE, Utils.toBytes(instance.getCreationDate()));
    put.add(FAMILY_SELF, CELL_LAST_MODIFIED_DATE, Utils.toBytes(instance.getLastModifiedDate()));
    if (instance.getOrganization() != null && StringUtils.isNotBlank(instance.getOrganization().getId())) {
      try {
        put.add(FAMILY_SELF, CELL_PARENT_ORG,
                orgSchemaInfoProvider.getRowIdFromId(instance.getOrganization().getId()));
      }
      catch (Exception ex) {
        logger.warn("Could not convert organization of user", ex);
      }
    }
    if (StringUtils.isNotBlank(instance.getUsername())) {
      put.add(FAMILY_SELF, CELL_USERNAME, Bytes.toBytes(instance.getUsername()));
    }
    if (StringUtils.isNotBlank(instance.getPassword())) {
      put.add(FAMILY_SELF, CELL_PASSWORD, Bytes.toBytes(instance.getPassword()));
    }
    if (instance.getRoles() != null && !instance.getRoles().isEmpty()) {
      for (Role role : instance.getRoles()) {
        if (role.getId() == null) {
          logger.warn("Skipping role as id is null - " + role.getName());
          continue;
        }
        final String name;
        if (StringUtils.isNotBlank(role.getName())) {
          name = role.getName();
        }
        else {
          name = "";
        }
        try {
          put.add(FAMILY_ROLES, roleSchemaInfoProvider.getRowIdFromId(role.getId()), Bytes.toBytes(name));
        }
        catch (Exception ex) {
          logger.warn("Could not add role to user", ex);
        }
      }
    }
    if (instance.getPrivileges() != null && !instance.getPrivileges().isEmpty()) {
      for (Privilege privilege : instance.getPrivileges()) {
        if (privilege.getId() == null) {
          logger.warn("Skipping privilege as id is null - " + privilege.getName());
          continue;
        }
        final String name;
        if (StringUtils.isNotBlank(privilege.getName())) {
          name = privilege.getName();
        }
        else {
          name = "";
        }
        try {
          put.add(FAMILY_PRIVILEGES, privSchemaInfoProvider.getRowIdFromId(privilege.getId()), Bytes.toBytes(name));
        }
        catch (Exception ex) {
          logger.warn("Could not add privilege to user", ex);
        }
      }
    }
  }

  @Override
  protected void getDeleteForTable(User instance, ExecutorService service, Delete put) {
    // Nothing is needed
  }

  @Override
  public User rowsToObject(Result startRow, ExecutorService executorService) {
    try {
      User user = new User();
      user.setId(getInfoProvider().getIdFromRowId(startRow.getRow()));
      user.setCreationDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_CREATION_DATE)));
      user.setLastModifiedDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_LAST_MODIFIED_DATE)));
      if (startRow.getValue(FAMILY_SELF, CELL_PARENT_ORG) != null) {
        String orgId = orgSchemaInfoProvider.getIdFromRowId(startRow.getValue(FAMILY_SELF, CELL_PARENT_ORG));
        user.setOrganization(orgService.getOrganizationByUniqueShortName(orgId));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_USERNAME) != null) {
        user.setUsername(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_USERNAME)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_PASSWORD) != null) {
        user.setPassword(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_PASSWORD)));
      }
      {
        Map<byte[], byte[]> roleMap = startRow.getFamilyMap(FAMILY_ROLES);
        if (roleMap != null && !roleMap.isEmpty()) {
          List<Long> roleIds = new ArrayList<Long>(roleMap.size());
          for (byte[] roleIdBytes : roleMap.keySet()) {
            roleIds.add(roleSchemaInfoProvider.getIdFromRowId(roleIdBytes));
          }
          user.setRoles(roleService.getRolesByIds(roleIds));
        }
      }
      {
        Map<byte[], byte[]> privMap = startRow.getFamilyMap(FAMILY_PRIVILEGES);
        if (privMap != null && !privMap.isEmpty()) {
          List<Long> privIds = new ArrayList<Long>(privMap.size());
          for (byte[] privIdBytes : privMap.keySet()) {
            privIds.add(privSchemaInfoProvider.getIdFromRowId(privIdBytes));
          }
          user.setPrivileges(privilegeService.getPrivilegesByIds(privIds));
        }
      }
      return user;
    }
    catch (Exception ex) {
      logger.error("Could not form User", ex);
      return null;
    }

  }
}
