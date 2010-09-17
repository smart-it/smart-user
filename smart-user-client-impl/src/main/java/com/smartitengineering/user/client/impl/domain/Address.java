/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl.domain;

/**
 *
 * @author russel
 */
public class Address extends AbstractClientDomain implements com.smartitengineering.user.client.api.Address {

  private String streetAddress;
  private String city;
  private String state;
  private String country;
  private String zip;
  private com.smartitengineering.user.client.api.GeoLocation geoLocation;

  @Override
  public com.smartitengineering.user.client.api.GeoLocation getGeoLocation() {
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

  @Override
  public void setCity(String city) {
    if (city == null) {
      return;
    }
    this.city = city;
  }

  @Override
  public void setCountry(String country) {
    if (country == null) {
      return;
    }
    this.country = country;
  }

  @Override
  public void setGeoLocation(com.smartitengineering.user.client.api.GeoLocation geoLocation) {
    if (geoLocation == null) {
      return;
    }
    this.geoLocation = geoLocation;
  }

  @Override
  public void setState(String state) {
    if (state == null) {
      return;
    }
    this.state = state;
  }

  @Override
  public void setStreetAddress(String streetAddress) {
    if (streetAddress == null) {
      return;
    }
    this.streetAddress = streetAddress;
  }

  @Override
  public void setZip(String zip) {
    if (zip == null) {
      return;
    }
    this.zip = zip;
  }
}
