/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.smartuser.client.api;

import java.util.Date;

/**
 *
 * @author modhu7
 */
public interface Address {

  public GeoLocation getGeoLocation();

  public String getCountry();

  public String getCity();

  public String getState();

  public String getStreetAddress();

  public String getZip();
}
