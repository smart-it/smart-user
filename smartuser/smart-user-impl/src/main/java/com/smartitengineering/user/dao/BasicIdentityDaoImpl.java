/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.dao;

import com.smartitengineering.user.domain.BasicIdentity;

/**
 *
 * @author modhu7
 */
public class BasicIdentityDaoImpl extends AbstractDao<BasicIdentity> {

    public BasicIdentityDaoImpl() {
        setEntityClass(BasicIdentity.class);
    }
}
