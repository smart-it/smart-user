/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl.domain;

import com.smartitengineering.domain.AbstractPersistentDTO;
import com.smartitengineering.domain.PersistentDTO;

/**
 *
 * @author russel
 */
public class GeoLocation extends AbstractPersistentDTO<GeoLocation> implements com.smartitengineering.smartuser.client.api.GeoLocation{

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

  public boolean isValid(){
    return true;
  }

}
