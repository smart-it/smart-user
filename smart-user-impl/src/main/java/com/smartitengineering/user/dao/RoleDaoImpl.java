/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.dao;

import com.smartitengineering.user.domain.Role;

/**
 *
 * @author modhu7
 */
public class RoleDaoImpl extends AbstractDao<Role> {

    public RoleDaoImpl() {
        setEntityClass(Role.class);
    }
}
