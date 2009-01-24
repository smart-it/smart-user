/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.dao;

import com.smartitengineering.user.domain.Privilege;

/**
 *
 * @author modhu7
 */
public class PrivilegeDaoImpl extends AbstractDao<Privilege>{

    public PrivilegeDaoImpl() {
        setEntityClass(Privilege.class);
    }
    
}
