/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.dao;

import com.smartitengineering.user.security.domain.SmartAce;

/**
 *
 * @author modhu7
 */
public class SmartAceDaoImpl extends AbstractDao<SmartAce>{

    public SmartAceDaoImpl() {
        setEntityClass(SmartAce.class);
    }

}
