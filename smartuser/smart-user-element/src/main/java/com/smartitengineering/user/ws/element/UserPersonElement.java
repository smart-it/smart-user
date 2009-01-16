/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.element;

import com.smartitengineering.user.domain.UserPerson;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author modhu7
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class UserPersonElement {
    private UserPerson userPerson;

    public UserPerson getUserPerson() {
        return userPerson;
    }

    public void setUserPerson(UserPerson userPerson) {
        this.userPerson = userPerson;
    }
    
}
