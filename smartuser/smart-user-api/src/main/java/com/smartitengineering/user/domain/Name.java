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
public class Name extends AbstractPersistentDTO<Name>{
    
    private String firstName;
    private String lastName;
    private String middleInitial;

    public String getFirstName() {
        if(firstName==null){
            return "";
        }
        return firstName;
    }

    public void setFirstName(String firstName) {
        if(firstName==null){
            return;
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        if(lastName==null){
            return "";
        }
        return lastName;
    }

    public void setLastName(String lastName) {
        if(lastName==null){
            return;
        }
        this.lastName = lastName;
    }

    public String getMiddleInitial() {
        if(middleInitial==null){
            return "";
        }
        return middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        if(middleInitial==null){
            return;
        }
        this.middleInitial = middleInitial;
    }
    
    public boolean isValid() {
        return true;
    }

}
