/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author modhu7
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ExceptionElement {

    private String group;
    private String fieldCausedBy;

    public String getFieldCausedBy() {
        return fieldCausedBy;
    }

    public void setFieldCausedBy(String fieldCausedBy) {
        this.fieldCausedBy = fieldCausedBy;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
