/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.domain;

import com.smartitengineering.domain.AbstractPersistentDTO;

/**
 *
 * @author modhu7
 */
class GeoLocation extends AbstractPersistentDTO<GeoLocation>{

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


    public boolean isValid() {
        if(longitude==null || latitude== null)
            return false;
        else
            return true;
    }

}
