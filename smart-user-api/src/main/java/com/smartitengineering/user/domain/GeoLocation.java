/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractGenericPersistentDTO;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author modhu7
 */
public class GeoLocation extends AbstractGenericPersistentDTO<GeoLocation, Long, Integer>{

  private Double longitude;
  private Double latitude;

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  @Override
  @JsonIgnore
  public boolean isValid() {
    if (longitude == null || latitude == null) {
      return false;
    }
    else {
      return true;
    }
  }
}
