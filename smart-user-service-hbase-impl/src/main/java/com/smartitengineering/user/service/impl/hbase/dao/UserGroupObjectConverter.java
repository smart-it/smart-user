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
import com.smartitengineering.user.domain.UserGroup;
import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.service.UserService;
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
public class UserGroupObjectConverter extends AbstractObjectRowConverter<UserGroup, Long> {

  private static final byte[] FAMILY_SELF = Bytes.toBytes("self");
  private static final byte[] FAMILY_ROLES = Bytes.toBytes("roles");
  private static final byte[] FAMILY_PRIVILEGES = Bytes.toBytes("privileges");
  private static final byte[] FAMILY_USERS = Bytes.toBytes("users");
  private static final byte[] CELL_PARENT_ORG = Bytes.toBytes("parentOrg");
  private static final byte[] CELL_CREATION_DATE = Bytes.toBytes("creationDate");
  private static final byte[] CELL_LAST_MODIFIED_DATE = Bytes.toBytes("lastModifiedDate");
  private static final byte[] CELL_NAME = Bytes.toBytes("name");
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
  @Inject
  private UserService userService;
  @Inject
  private SchemaInfoProvider<User, Long> userSchemaInfoProvider;

  @Override
  protected String[] getTablesToAttainLock() {
    return new String[]{getInfoProvider().getMainTableName()};
  }

  @Override
  protected void getPutForTable(UserGroup instance, ExecutorService service, Put put) {
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
    if (StringUtils.isNotBlank(instance.getName())) {
      put.add(FAMILY_SELF, CELL_NAME, Bytes.toBytes(instance.getName()));
    }
    {
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
    }
    {
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
    {
      if (instance.getUsers() != null && !instance.getUsers().isEmpty()) {
        for (User user : instance.getUsers()) {
          if (user.getId() == null) {
            logger.warn("Skipping privilege as id is null - " + user.getUsername());
            continue;
          }
          final String name;
          if (StringUtils.isNotBlank(user.getUsername())) {
            name = user.getUsername();
          }
          else {
            name = "";
          }
          try {
            put.add(FAMILY_USERS, userSchemaInfoProvider.getRowIdFromId(user.getId()), Bytes.toBytes(name));
          }
          catch (Exception ex) {
            logger.warn("Could not add privilege to user", ex);
          }
        }
      }
    }
  }

  @Override
  protected void getDeleteForTable(UserGroup instance, ExecutorService service, Delete put) {
    // Nothing is needed
  }

  @Override
  public UserGroup rowsToObject(Result startRow, ExecutorService executorService) {
    try {
      UserGroup userGroup = new UserGroup();
      userGroup.setId(getInfoProvider().getIdFromRowId(startRow.getRow()));
      userGroup.setCreationDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_CREATION_DATE)));
      userGroup.setLastModifiedDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_LAST_MODIFIED_DATE)));
      if (startRow.getValue(FAMILY_SELF, CELL_PARENT_ORG) != null) {
        String orgId = orgSchemaInfoProvider.getIdFromRowId(startRow.getValue(FAMILY_SELF, CELL_PARENT_ORG));
        userGroup.setOrganization(orgService.getOrganizationByUniqueShortName(orgId));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_NAME) != null) {
        userGroup.setName(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_NAME)));
      }
      {
        Map<byte[], byte[]> roleMap = startRow.getFamilyMap(FAMILY_ROLES);
        if (roleMap != null && !roleMap.isEmpty()) {
          List<Long> roleIds = new ArrayList<Long>(roleMap.size());
          for (byte[] roleIdBytes : roleMap.keySet()) {
            roleIds.add(roleSchemaInfoProvider.getIdFromRowId(roleIdBytes));
          }
          userGroup.setRoles(roleService.getRolesByIds(roleIds));
        }
      }
      {
        Map<byte[], byte[]> privMap = startRow.getFamilyMap(FAMILY_PRIVILEGES);
        if (privMap != null && !privMap.isEmpty()) {
          List<Long> privIds = new ArrayList<Long>(privMap.size());
          for (byte[] privIdBytes : privMap.keySet()) {
            privIds.add(privSchemaInfoProvider.getIdFromRowId(privIdBytes));
          }
          userGroup.setPrivileges(privilegeService.getPrivilegesByIds(privIds));
        }
      }
      {
        Map<byte[], byte[]> userMap = startRow.getFamilyMap(FAMILY_USERS);
        if (userMap != null && !userMap.isEmpty()) {
          List<Long> userIds = new ArrayList<Long>(userMap.size());
          for (byte[] userIdBytes : userMap.keySet()) {
            userIds.add(userSchemaInfoProvider.getIdFromRowId(userIdBytes));
          }
          if(logger.isInfoEnabled()) {
            logger.info("Loading users: " + userIds);
          }
          userGroup.setUsers(userService.getUsersByIds(userIds));
        }
      }
      return userGroup;
    }
    catch (Exception ex) {
      logger.error("Could not form UserGroup", ex);
      return null;
    }

  }
}
