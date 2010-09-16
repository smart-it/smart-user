/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl.domain;

/**
 *
 * @author russel
 */
public class GeoLocation extends AbstractClientDomain implements com.smartitengineering.smartuser.client.api.GeoLocation{

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
    if(latitude==null)
      return;
    this.latitude = latitude;
  }

  public void setLongitude(Double longitude) {
    if(longitude==null)
      return;
    this.longitude = longitude;
  }
}
