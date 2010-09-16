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
public class GeoLocation extends AbstractClientDomain implements com.smartitengineering.user.client.api.GeoLocation {

  private Double longitude;
  private Double latitude;
  private Date lastModifiedDate;

  @Override
  public Double getLatitude() {
    return longitude;
  }

  @Override
  public Double getLongitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    if (latitude == null) {
      return;
    }
    this.latitude = latitude;
  }

  public void setLongitude(Double longitude) {
    if (longitude == null) {
      return;
    }
    this.longitude = longitude;
  }
}
