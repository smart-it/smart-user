/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.element;

import com.smartitengineering.user.filter.UserPersonFilter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author modhu7
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class UserPersonFilterElement {
    private UserPersonFilter userPersonFilter;

    public UserPersonFilter getUserPersonFilter() {
        return userPersonFilter;
    }

    public void setUserPersonFilter(UserPersonFilter userPersonFilter) {
        this.userPersonFilter = userPersonFilter;
    }
    
}
