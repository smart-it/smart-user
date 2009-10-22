/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.element;

import com.smartitengineering.user.security.domain.SmartAcl;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author modhu7
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SmartAclElement {
    SmartAcl smartAcl;

    public SmartAcl getSmartAcl() {
        return smartAcl;
    }

    public void setSmartAcl(SmartAcl smartAcl) {
        this.smartAcl = smartAcl;
    }


}
