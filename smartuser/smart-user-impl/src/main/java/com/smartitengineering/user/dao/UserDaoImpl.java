/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.dao;

import com.smartitengineering.user.domain.User;

/**
 *
 * @author modhu7
 */
public class UserDaoImpl extends AbstractDao<User> {

    public UserDaoImpl() {
        setEntityClass(User.class);
    }
}
