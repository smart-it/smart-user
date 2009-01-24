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
public class UserPerson extends AbstractPersistentDTO<UserPerson>{
    
    User user;
    Person person;

    public Person getPerson() {
        if(person==null){
            person = new Person();
        }
        return person;
    }

    public void setPerson(Person person) {
        if(person==null){
            return;
        }
        this.person = person;
    }

    public User getUser() {
        if(user==null){
            user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        if(user==null){
            return;
        }
        this.user = user;
    }
    
    

    public boolean isValid() {
        return user.isValid();
    }

}
