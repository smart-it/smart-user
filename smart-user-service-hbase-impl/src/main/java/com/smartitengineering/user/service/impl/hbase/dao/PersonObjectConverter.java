/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.dao;

import com.smartitengineering.dao.impl.hbase.spi.ExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.impl.AbstractObjectRowConverter;
import com.smartitengineering.user.domain.Person;
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
public class PersonObjectConverter extends AbstractObjectRowConverter<Person, Long> {

  private static final byte[] FAMILY_SELF = Bytes.toBytes("self");
  private static final byte[] CELL_PRIMARY_EMAIL = Bytes.toBytes("primaryEmail");
  private static final byte[] CELL_SECONDARY_EMAIL = Bytes.toBytes("secondaryEmail");
  private static final byte[] CELL_PHONE_NUMBER = Bytes.toBytes("phoneNumber");
  private static final byte[] CELL_CELL_PHONE_NUMBER = Bytes.toBytes("cellPhoneNumber");
  private static final byte[] CELL_FAX_NUMBER = Bytes.toBytes("faxNumber");
  private static final byte[] CELL_DOB = Bytes.toBytes("dob");
  private static final byte[] CELL_ADDRESS_STREET = Bytes.toBytes("address.street");
  private static final byte[] CELL_ADDRESS_CITY = Bytes.toBytes("address.city");
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
  private static final byte[] CELL_SELF_IDENTITY_NATIONAL_ID = Bytes.toBytes("SELF.natId");
  private static final byte[] CELL_SELF_IDENTITY_FIRST_NAME = Bytes.toBytes("SELF.fname");
  private static final byte[] CELL_SELF_IDENTITY_LAST_NAME = Bytes.toBytes("SELF.lname");
  private static final byte[] CELL_SELF_IDENTITY_MIDDLE_INITIAL = Bytes.toBytes("SELF.mi");
  private static final List<byte[]> SELF_CELL_LIST = Arrays.asList(CELL_SELF_IDENTITY_NATIONAL_ID,
                                                                   CELL_SELF_IDENTITY_FIRST_NAME,
                                                                   CELL_SELF_IDENTITY_LAST_NAME,
                                                                   CELL_SELF_IDENTITY_MIDDLE_INITIAL);
  private static final byte[] CELL_FATHER_IDENTITY_NATIONAL_ID = Bytes.toBytes("FATHER.natId");
  private static final byte[] CELL_FATHER_IDENTITY_FIRST_NAME = Bytes.toBytes("FATHER.fname");
  private static final byte[] CELL_FATHER_IDENTITY_LAST_NAME = Bytes.toBytes("FATHER.lname");
  private static final byte[] CELL_FATHER_IDENTITY_MIDDLE_INITIAL = Bytes.toBytes("FATHER.mi");
  private static final List<byte[]> FATHER_CELL_LIST = Arrays.asList(CELL_FATHER_IDENTITY_NATIONAL_ID,
                                                                     CELL_FATHER_IDENTITY_FIRST_NAME,
                                                                     CELL_FATHER_IDENTITY_LAST_NAME,
                                                                     CELL_FATHER_IDENTITY_MIDDLE_INITIAL);
  private static final byte[] CELL_MOTHER_IDENTITY_NATIONAL_ID = Bytes.toBytes("MOTHER.natId");
  private static final byte[] CELL_MOTHER_IDENTITY_FIRST_NAME = Bytes.toBytes("MOTHER.fname");
  private static final byte[] CELL_MOTHER_IDENTITY_LAST_NAME = Bytes.toBytes("MOTHER.lname");
  private static final byte[] CELL_MOTHER_IDENTITY_MIDDLE_INITIAL = Bytes.toBytes("MOTHER.mi");
  private static final List<byte[]> MOTHER_CELL_LIST = Arrays.asList(CELL_MOTHER_IDENTITY_NATIONAL_ID,
                                                                     CELL_MOTHER_IDENTITY_FIRST_NAME,
                                                                     CELL_MOTHER_IDENTITY_LAST_NAME,
                                                                     CELL_MOTHER_IDENTITY_MIDDLE_INITIAL);
  private static final byte[] CELL_SPOUSE_IDENTITY_NATIONAL_ID = Bytes.toBytes("SPOUSE.natId");
  private static final byte[] CELL_SPOUSE_IDENTITY_FIRST_NAME = Bytes.toBytes("SPOUSE.fname");
  private static final byte[] CELL_SPOUSE_IDENTITY_LAST_NAME = Bytes.toBytes("SPOUSE.lname");
  private static final byte[] CELL_SPOUSE_IDENTITY_MIDDLE_INITIAL = Bytes.toBytes("SPOUSE.mi");
  private static final List<byte[]> SPOUSE_CELL_LIST = Arrays.asList(CELL_SPOUSE_IDENTITY_NATIONAL_ID,
                                                                     CELL_SPOUSE_IDENTITY_FIRST_NAME,
                                                                     CELL_SPOUSE_IDENTITY_LAST_NAME,
                                                                     CELL_SPOUSE_IDENTITY_MIDDLE_INITIAL);

  @Override
  protected String[] getTablesToAttainLock() {
    return new String[]{getInfoProvider().getMainTableName()};
  }

  @Override
  protected void getPutForTable(Person instance, ExecutorService service, Put put) {
    if (instance.getAddress() != null) {
      ConvertionUtils.fillWithAddress(instance.getAddress(), ADDRESS_CELL_LIST, FAMILY_SELF, put);
    }
    if (instance.getSelf() != null) {
      ConvertionUtils.fillWithBasicIdentity(instance.getSelf(), SELF_CELL_LIST, FAMILY_SELF, put);
    }
    if (instance.getFather() != null) {
      ConvertionUtils.fillWithBasicIdentity(instance.getFather(), FATHER_CELL_LIST, FAMILY_SELF, put);
    }
    if (instance.getMother() != null) {
      ConvertionUtils.fillWithBasicIdentity(instance.getMother(), MOTHER_CELL_LIST, FAMILY_SELF, put);
    }
    if (instance.getSpouse() != null) {
      ConvertionUtils.fillWithBasicIdentity(instance.getSpouse(), SPOUSE_CELL_LIST, FAMILY_SELF, put);
    }
    if (StringUtils.isNotBlank(instance.getPrimaryEmail())) {
      put.add(FAMILY_SELF, CELL_PRIMARY_EMAIL, Bytes.toBytes(instance.getPrimaryEmail()));
    }
    if (StringUtils.isNotBlank(instance.getSecondaryEmail())) {
      put.add(FAMILY_SELF, CELL_SECONDARY_EMAIL, Bytes.toBytes(instance.getSecondaryEmail()));
    }
    if (StringUtils.isNotBlank(instance.getPhoneNumber())) {
      put.add(FAMILY_SELF, CELL_PHONE_NUMBER, Bytes.toBytes(instance.getPhoneNumber()));
    }
    if (StringUtils.isNotBlank(instance.getCellPhoneNumber())) {
      put.add(FAMILY_SELF, CELL_CELL_PHONE_NUMBER, Bytes.toBytes(instance.getCellPhoneNumber()));
    }
    if (StringUtils.isNotBlank(instance.getFaxNumber())) {
      put.add(FAMILY_SELF, CELL_FAX_NUMBER, Bytes.toBytes(instance.getFaxNumber()));
    }
    if (instance.getBirthDay() != null) {
      put.add(FAMILY_SELF, CELL_DOB, Utils.toBytes(instance.getBirthDay()));
    }
  }

  @Override
  protected void getDeleteForTable(Person instance, ExecutorService service, Delete put) {
    // No need to do anything
  }

  @Override
  public Person rowsToObject(Result startRow, ExecutorService executorService) {
    try {
      Person person = new Person();
      person.setId(getInfoProvider().getIdFromRowId(startRow.getRow()));
      person.setAddress(ConvertionUtils.formAddress(startRow, FAMILY_SELF, ADDRESS_CELL_LIST));
      person.setSelf(ConvertionUtils.formBasicIdentity(startRow, FAMILY_SELF, SELF_CELL_LIST));
      person.setFather(ConvertionUtils.formBasicIdentity(startRow, FAMILY_SELF, FATHER_CELL_LIST));
      person.setMother(ConvertionUtils.formBasicIdentity(startRow, FAMILY_SELF, MOTHER_CELL_LIST));
      person.setSpouse(ConvertionUtils.formBasicIdentity(startRow, FAMILY_SELF, SPOUSE_CELL_LIST));
      if (startRow.getValue(FAMILY_SELF, CELL_PRIMARY_EMAIL) != null) {
        person.setPrimaryEmail(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_PRIMARY_EMAIL)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_SECONDARY_EMAIL) != null) {
        person.setSecondaryEmail(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_SECONDARY_EMAIL)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_PHONE_NUMBER) != null) {
        person.setPhoneNumber(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_PHONE_NUMBER)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_CELL_PHONE_NUMBER) != null) {
        person.setCellPhoneNumber(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_CELL_PHONE_NUMBER)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_FAX_NUMBER) != null) {
        person.setFaxNumber(Bytes.toString(startRow.getValue(FAMILY_SELF, CELL_FAX_NUMBER)));
      }
      if (startRow.getValue(FAMILY_SELF, CELL_DOB) != null) {
        person.setBirthDay(Utils.toDate(startRow.getValue(FAMILY_SELF, CELL_DOB)));
      }
      return person;
    }
    catch (Exception ex) {
      logger.error("Could not form person", ex);
      return null;
    }
  }
}
