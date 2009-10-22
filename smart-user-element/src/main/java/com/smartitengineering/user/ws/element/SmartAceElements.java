/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.element;

import com.smartitengineering.user.security.domain.SmartAce;
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
public class SmartAceElements {
    Collection<SmartAce> smartAces;

    public Collection<SmartAce> getSmartAces() {
        return smartAces;
    }

    public void setSmartAces(Collection<SmartAce> smartAces) {
        this.smartAces = smartAces;
    }
}
