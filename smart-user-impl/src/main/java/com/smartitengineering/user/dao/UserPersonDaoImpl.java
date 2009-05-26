/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.dao;

import com.smartitengineering.user.domain.UserPerson;

/**
 *
 * @author modhu7
 */
public class UserPersonDaoImpl extends AbstractDao<UserPerson> {

    public UserPersonDaoImpl() {
        setEntityClass(UserPerson.class);
    }
}
