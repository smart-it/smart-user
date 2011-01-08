/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.dao;

import com.google.inject.Inject;
import com.smartitengineering.dao.impl.hbase.spi.ExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.SchemaInfoProvider;
import com.smartitengineering.dao.impl.hbase.spi.impl.AbstractObjectRowConverter;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.UserService;
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
  @Inject
  private UserService userService;
  @Inject
  private PersonService personService;
  @Inject
  private SchemaInfoProvider<User, Long> userSchemaInfoProvider;
  @Inject
  private SchemaInfoProvider<Person, Long> personSchemaInfoProvider;

  @Override
  protected String[] getTablesToAttainLock() {
    return new String[]{getInfoProvider().getMainTableName()};
  }

  @Override
  protected void getPutForTable(UserPerson instance, ExecutorService executorService, Put put) {
    if (instance.getUser() != null && instance.getUser().getId() != null) {
      try {
        put.add(FAMILY_SELF, CELL_USER_ID, userSchemaInfoProvider.getRowIdFromId(instance.getUser().getId()));
      }
      catch (Exception ex) {
        logger.error("Could not convert user id to byte in user person!", ex);
      }
    }
    if (instance.getPerson() != null && instance.getPerson().getId() != null) {
      try {
        put.add(FAMILY_SELF, CELL_PERSON_ID, personSchemaInfoProvider.getRowIdFromId(instance.getPerson().getId()));
      }
      catch (Exception ex) {
        logger.error("Could not convert person id to byte in user person!", ex);
      }
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
    try {
      UserPerson userPerson = new UserPerson();
      userPerson.setCreationDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_CREATION_DATE)));
      userPerson.setLastModifiedDate(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_LAST_MODIFIED_DATE)));
      if (startRow.getValue(FAMILY_SELF, CELL_USER_ID) != null) {
        Long userId = userSchemaInfoProvider.getIdFromRowId(startRow.getValue(FAMILY_SELF, CELL_USER_ID));
        userPerson.setUser(userService.getById(userId));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_PERSON_ID) != null) {
        Long personId = personSchemaInfoProvider.getIdFromRowId(startRow.getValue(FAMILY_SELF, CELL_PERSON_ID));
        userPerson.setPerson(personService.getById(personId));
      }
      return userPerson;
    }
    catch (Exception ex) {
      logger.error("Could not form UserPerson", ex);
      return null;
    }
  }
}
