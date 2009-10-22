/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.filter;

/**
 *
 * @author modhu7
 */
public class SmartAceFilter {
    private String oid;
    private String sidUsername;

    public String getOid() {
        if(oid==null){
            return "";
        }
        return oid;
    }

    public void setOid(String oid) {
        if(oid==null){
            return;
        }
        this.oid = oid;
    }

    public String getSidUsername() {
        if(sidUsername==null){
            return "";
        }
        return sidUsername;
    }

    public void setSidUsername(String sidUsername) {
        if(sidUsername==null){
            return;
        }
        this.sidUsername = sidUsername;
    }

}
