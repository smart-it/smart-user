/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl.domain;



/**
 *
 * @author russel
 */
public class Address implements com.smartitengineering.smartuser.client.api.Address {

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
    this.city = city;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setGeoLocation(GeoLocation geoLocation) {
    this.geoLocation = geoLocation;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setStreetAddress(String streetAddress) {
    this.streetAddress = streetAddress;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  

}
