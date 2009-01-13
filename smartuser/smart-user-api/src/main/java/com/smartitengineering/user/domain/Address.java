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
public class Address extends AbstractPersistentDTO<Address>{
    private String streetAddress;
    private String city;
    private String state;
    private String country;
    private String zip;

    public String getCountry() {
        if(country==null){
            return "";
        }
        return country;
    }

    public void setCountry(String country) {
        if(country==null){
            return;
        }
        this.country = country;
    }

    public String getCity() {
        if(city==null){
            return "";
        }
        return city;
    }

    public void setCity(String province) {
        if(province==null){
            return;
        }
        this.city = province;
    }

    public String getState() {
        if(state==null){
            return "";
        }
        return state;
    }

    public void setState(String state) {
        if(state==null){
            return;
        }
        this.state = state;
    }

    public String getStreetAddress() {
        if(streetAddress==null){
            return "";
        }
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        if(streetAddress==null){
            return;
        }
        this.streetAddress = streetAddress;
    }

    public String getZip() {
        if(zip==null){
            return "";
        }
        return zip;
    }

    public void setZip(String zip) {
        if(zip==null){
            return;
        }
        this.zip = zip;
    }   

    public boolean isValid() {
        return true;
    }
    
}
