/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractPersistentDTO;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author modhu7
 */
public class Address extends AbstractPersistentDTO<Address> {

  private String streetAddress;
  private String city;
  private String state;
  private String country;
  private String zip;
  private GeoLocation geoLocation;

  public GeoLocation getGeoLocation() {
    return geoLocation;
  }

  public void setGeoLocation(GeoLocation geoLocation) {
    this.geoLocation = geoLocation;
  }

  public String getCountry() {
    if (country == null) {
      country = "";
    }
    return country;
  }

  public void setCountry(String country) {
    if (country == null) {
      return;
    }
    this.country = country;
  }

  public String getCity() {
    if (city == null) {
      city = "";
    }
    return city;
  }

  public void setCity(String province) {
    if (province == null) {
      return;
    }
    this.city = province;
  }

  public String getState() {
    if (state == null) {
      state = "";
    }
    return state;
  }

  public void setState(String state) {
    if (state == null) {
      return;
    }
    this.state = state;
  }

  public String getStreetAddress() {
    if (streetAddress == null) {
      streetAddress = "";
    }
    return streetAddress;
  }

  public void setStreetAddress(String streetAddress) {
    if (streetAddress == null) {
      return;
    }
    this.streetAddress = streetAddress;
  }

  public String getZip() {
    if (zip == null) {
      zip = "";
    }
    return zip;
  }

  public void setZip(String zip) {
    if (zip == null) {
      return;
    }
    this.zip = zip;
  }

  @Override
  @JsonIgnore
  public boolean isValid() {
    if (streetAddress == null || zip == null || country == null ||
        city == null || state == null) {
      return false;
    }
    return true;
  }
}
