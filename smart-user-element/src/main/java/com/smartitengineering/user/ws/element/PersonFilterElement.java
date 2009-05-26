/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.element;

import com.smartitengineering.user.filter.PersonFilter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author modhu7
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PersonFilterElement {

    private PersonFilter personFilter;

    public PersonFilter getPersonFilter() {
        return personFilter;
    }

    public void setPersonFilter(PersonFilter personFilter) {
        this.personFilter = personFilter;
    }
}
