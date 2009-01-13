/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.filter;

/**
 *
 * @author modhu7
 */
public class PersonFilter {
    private String name;
    private String email;

    public String getEmail() {
        if(email==null){
            return "";
        }
        return email;
    }

    public String getName() {
        if(name==null){
            return "";
        }
        return name;
    }
    
    

    public void setEmail(String email) {
        if(email==null){
            return;
        }
        this.email = email;
    }

    public void setName(String name) {
        if(name==null){
            return;
        }
        this.name = name;
    }
    
    
}
