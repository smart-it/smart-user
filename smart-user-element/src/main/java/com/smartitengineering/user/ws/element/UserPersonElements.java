/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.element;

import com.smartitengineering.user.domain.UserPerson;
import java.util.Collection;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author modhu7
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class UserPersonElements {

    private Collection<UserPerson> userPersons;

    public Collection<UserPerson> getUserPersons() {
        return userPersons;
    }

    public void setUserPersons(Collection<UserPerson> userPersons) {
        this.userPersons = userPersons;
    }
}
