/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl.domain;

/**
 *
 * @author russel
 */
public class GeoLocation implements com.smartitengineering.user.client.api.GeoLocation{

   private Double longitude;
   private Double latitude;

  @Override
  public Double getLatitude() {
    return longitude;
  }

  @Override
  public Double getLongitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

}
