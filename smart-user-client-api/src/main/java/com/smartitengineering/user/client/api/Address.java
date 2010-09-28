/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.api;

/**
 *
 * @author modhu7
 */
public interface Address {

  public GeoLocation getGeoLocation();

  public String getCountry();

  public String getCity();

  public void setCity(String city);

  public String getState();

  public String getStreetAddress();

  public String getZip();

  public void setGeoLocation(GeoLocation geoLocation);

  public void setCountry(String country);

  public void setState(String state);

  public void setStreetAddress(String streetAddress);

  public void setZip(String zip);
}
