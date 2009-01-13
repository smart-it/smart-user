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
public class BasicPerson extends AbstractPersistentDTO<BasicPerson>{
    
    private String nationalID;
    private Name name;

    public Name getName() {
        if(name == null){
            return new Name();
        }
        return name;
    }

    public void setName(Name name) {
        if(name == null){
            return;
        }
        this.name = name;
    }

    public String getNationalID() {
        if(nationalID==null){
            return "";
        }
        return nationalID;
    }

    public void setNationalID(String nationalID) {
        if(nationalID==null){
            return;
        }
        this.nationalID = nationalID;
    }
    
    public boolean isValid() {
        return true;
    }

}
