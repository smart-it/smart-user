/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.dao;

import com.smartitengineering.user.security.domain.SmartAcl;

/**
 *
 * @author modhu7
 */
public class SmartAclDaoImpl extends AbstractDao<SmartAcl>{

    public SmartAclDaoImpl() {
        setEntityClass(SmartAcl.class);
    }

}
