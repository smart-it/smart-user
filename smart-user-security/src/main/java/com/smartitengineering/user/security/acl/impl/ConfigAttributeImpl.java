/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security.acl.impl;

import org.springframework.security.ConfigAttribute;

/**
 *
 * @author modhu7
 */
public class ConfigAttributeImpl implements ConfigAttribute {

    private String attribute;

    public ConfigAttributeImpl(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        if(attribute==null)
            return "";
        return attribute;
    }

    public void setAttribute(String s) {
        if (s != null) {
            attribute = s;
        }
    }
}
