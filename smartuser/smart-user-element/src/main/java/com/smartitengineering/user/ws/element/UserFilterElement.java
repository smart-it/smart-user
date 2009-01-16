/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.element;

import com.smartitengineering.user.filter.UserFilter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author modhu7
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class UserFilterElement {
         
    private UserFilter userFilter;

    public UserFilter getUserFilter() {
        return userFilter;
    }

    public void setUserFilter(UserFilter userFilter) {
        this.userFilter = userFilter;
    }
    
}
