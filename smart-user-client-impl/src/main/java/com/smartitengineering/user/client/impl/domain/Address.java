/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl.domain;

import java.util.Date;

/**
 *
 * @author russel
 */
public class Address extends AbstractClientDomain implements com.smartitengineering.smartuser.client.api.Address {

  private String streetAddress;
  private String city;
  private String state;
  private String country;
  private String zip;
  private GeoLocation geoLocation;  

  @Override
  public GeoLocation getGeoLocation() {
    return geoLocation;
  }

  @Override
  public String getCountry() {
    return country;
  }

  @Override
  public String getCity() {
    return city;
  }

  @Override
  public String getState() {
    return state;
  }

  @Override
  public String getStreetAddress() {
    return streetAddress;
  }

  @Override
  public String getZip() {
    return zip;
  }

  public void setCity(String city) {
    if (city == null) {
      return;
    }
    this.city = city;
  }

  public void setCountry(String country) {
    if (country == null) {
      return;
    }
    this.country = country;
  }

  public void setGeoLocation(GeoLocation geoLocation) {
    if (geoLocation == null) {
      return;
    }
    this.geoLocation = geoLocation;
  }

  public void setState(String state) {
    if (state == null) {
      return;
    }
    this.state = state;
  }

  public void setStreetAddress(String streetAddress) {
    if (streetAddress == null) {
      return;
    }
    this.streetAddress = streetAddress;
  }

  public void setZip(String zip) {
    if (zip == null) {
      return;
    }
    this.zip = zip;
  }
}
