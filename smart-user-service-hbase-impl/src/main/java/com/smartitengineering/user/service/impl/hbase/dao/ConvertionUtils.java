/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.hbase.dao;

import com.smartitengineering.user.domain.Address;
import com.smartitengineering.user.domain.BasicIdentity;
import com.smartitengineering.user.domain.GeoLocation;
import com.smartitengineering.user.domain.Name;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author imyousuf
 */
public final class ConvertionUtils {

  public static final int ADDRESS_QUALIFIER_COUNT = 7;
  public static final int BASIC_ID_QUALIFIER_COUNT = 4;

  private ConvertionUtils() {
  }

  public static void fillWithBasicIdentity(BasicIdentity identity, List<byte[]> cellQualifiers, byte[] familyQualifier,
                                           Put put) {
    if (cellQualifiers == null || cellQualifiers.size() != BASIC_ID_QUALIFIER_COUNT) {
      throw new IllegalArgumentException("Not all qualifiers specified");
    }
    if (StringUtils.isNotBlank(identity.getNationalID())) {
      put.add(familyQualifier, cellQualifiers.get(0), Bytes.toBytes(identity.getNationalID()));
    }
    if (identity.getName() != null) {
      if (StringUtils.isNotBlank(identity.getName().getFirstName())) {
        put.add(familyQualifier, cellQualifiers.get(1), Bytes.toBytes(identity.getName().getFirstName()));
      }
      if (StringUtils.isNotBlank(identity.getName().getLastName())) {
        put.add(familyQualifier, cellQualifiers.get(2), Bytes.toBytes(identity.getName().getLastName()));
      }
      if (StringUtils.isNotBlank(identity.getName().getMiddleInitial())) {
        put.add(familyQualifier, cellQualifiers.get(3), Bytes.toBytes(identity.getName().getMiddleInitial()));
      }
    }
  }

  public static BasicIdentity formBasicIdentity(Result startRow, byte[] family, List<byte[]> cellQualifiers) {
    if (cellQualifiers == null || cellQualifiers.size() != BASIC_ID_QUALIFIER_COUNT) {
      throw new IllegalArgumentException("Not all qualifiers specified");
    }
    boolean atleastOneFieldIsPresent = false;
    BasicIdentity identity = new BasicIdentity();
    if (startRow.getValue(family, cellQualifiers.get(0)) != null) {
      identity.setNationalID(Bytes.toString(startRow.getValue(family, cellQualifiers.get(0))));
      atleastOneFieldIsPresent = true;
    }
    if (startRow.getValue(family, cellQualifiers.get(1)) != null) {
      Name name = new Name();
      identity.setName(name);
      identity.getName().setFirstName(Bytes.toString(startRow.getValue(family, cellQualifiers.get(1))));
      atleastOneFieldIsPresent = true;
    }
    if (startRow.getValue(family, cellQualifiers.get(2)) != null) {
      if (!atleastOneFieldIsPresent) {
        Name name = new Name();
        identity.setName(name);
      }
      identity.getName().setLastName(Bytes.toString(startRow.getValue(family, cellQualifiers.get(2))));
      atleastOneFieldIsPresent = true;
    }
    if (startRow.getValue(family, cellQualifiers.get(3)) != null) {
      if (!atleastOneFieldIsPresent) {
        Name name = new Name();
        identity.setName(name);
      }
      identity.getName().setMiddleInitial(Bytes.toString(startRow.getValue(family, cellQualifiers.get(3))));
      atleastOneFieldIsPresent = true;
    }
    return atleastOneFieldIsPresent ? identity : null;
  }

  public static void fillWithAddress(Address address, List<byte[]> cellQualifiers, byte[] familyQualifier, Put put) {
    if (cellQualifiers == null || cellQualifiers.size() != ADDRESS_QUALIFIER_COUNT) {
      throw new IllegalArgumentException("Not all qualifiers specified");
    }
    if (StringUtils.isNotBlank(address.getStreetAddress())) {
      put.add(familyQualifier, cellQualifiers.get(0), Bytes.toBytes(address.getStreetAddress()));
    }
    if (StringUtils.isNotBlank(address.getCity())) {
      put.add(familyQualifier, cellQualifiers.get(1), Bytes.toBytes(address.getCity()));
    }
    if (StringUtils.isNotBlank(address.getState())) {
      put.add(familyQualifier, cellQualifiers.get(2), Bytes.toBytes(address.getState()));
    }
    if (StringUtils.isNotBlank(address.getZip())) {
      put.add(familyQualifier, cellQualifiers.get(3), Bytes.toBytes(address.getZip()));
    }
    if (StringUtils.isNotBlank(address.getCountry())) {
      put.add(familyQualifier, cellQualifiers.get(4), Bytes.toBytes(address.getCountry()));
    }
    if (address.getGeoLocation() != null && address.getGeoLocation().getLatitude() != null) {
      put.add(familyQualifier, cellQualifiers.get(5), Bytes.toBytes(address.getGeoLocation().getLatitude()));
    }
    if (address.getGeoLocation() != null && address.getGeoLocation().getLongitude() != null) {
      put.add(familyQualifier, cellQualifiers.get(6), Bytes.toBytes(address.getGeoLocation().getLongitude()));
    }
  }

  public static Address formAddress(Result startRow, byte[] family, List<byte[]> cellQualifiers) {
    if (cellQualifiers == null || cellQualifiers.size() != ADDRESS_QUALIFIER_COUNT) {
      throw new IllegalArgumentException("Not all qualifiers specified");
    }
    boolean atleastOneFieldIsPresent = false;
    Address address = new Address();
    if (startRow.getValue(family, cellQualifiers.get(0)) != null) {
      address.setStreetAddress(Bytes.toString(startRow.getValue(family, cellQualifiers.get(0))));
      atleastOneFieldIsPresent = true;
    }
    if (startRow.getValue(family, cellQualifiers.get(1)) != null) {
      address.setCity(Bytes.toString(startRow.getValue(family, cellQualifiers.get(1))));
      atleastOneFieldIsPresent = true;
    }
    if (startRow.getValue(family, cellQualifiers.get(2)) != null) {
      address.setState(Bytes.toString(startRow.getValue(family, cellQualifiers.get(2))));
      atleastOneFieldIsPresent = true;
    }
    if (startRow.getValue(family, cellQualifiers.get(3)) != null) {
      address.setZip(Bytes.toString(startRow.getValue(family, cellQualifiers.get(3))));
      atleastOneFieldIsPresent = true;
    }
    if (startRow.getValue(family, cellQualifiers.get(4)) != null) {
      address.setCountry(Bytes.toString(startRow.getValue(family, cellQualifiers.get(4))));
      atleastOneFieldIsPresent = true;
    }
    if (startRow.getValue(family, cellQualifiers.get(5)) != null) {
      GeoLocation location = new GeoLocation();
      address.setGeoLocation(location);
      location.setLatitude(Bytes.toDouble(startRow.getValue(family, cellQualifiers.get(5))));
      atleastOneFieldIsPresent = true;
    }
    if (startRow.getValue(family, cellQualifiers.get(6)) != null) {
      if (!atleastOneFieldIsPresent) {
        GeoLocation location = new GeoLocation();
        address.setGeoLocation(location);
      }
      address.getGeoLocation().setLongitude(Bytes.toDouble(startRow.getValue(family, cellQualifiers.get(6))));
      atleastOneFieldIsPresent = true;
    }
    return atleastOneFieldIsPresent ? address : null;
  }
}
