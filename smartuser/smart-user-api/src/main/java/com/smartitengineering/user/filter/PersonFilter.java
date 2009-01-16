/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.filter;

import com.smartitengineering.user.domain.Name;



/**
 *
 * @author modhu7
 */
public class PersonFilter {
    private Name name;
    private String email;

    public String getEmail() {
        if(email==null){
            return "";
        }
        return email;
    }

    public Name getName() {
        if(name==null){
            return new Name();
        }
        return name;
    }
    
    

    public void setEmail(String email) {
        if(email==null){
            return;
        }
        this.email = email;
    }

    public void setName(Name name) {
        if(name==null){
            return;
        }
        this.name = name;
    }
    
    
}
