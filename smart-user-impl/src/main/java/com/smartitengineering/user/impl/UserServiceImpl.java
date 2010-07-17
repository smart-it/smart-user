/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.impl;

import com.smartitengineering.dao.impl.hibernate.AbstractCommonDaoImpl;
import com.smartitengineering.domain.PersistentDTO;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.service.UserService;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public class UserServiceImpl extends AbstractCommonDaoImpl<User> implements UserService{

    @Override
    public void save(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<User> search(UserFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<User> getAllUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User getUserByUsername(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void validateUser(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public User getUserByOrganizationAndUserName(String organizationName, String userName){
        return new User();
    }

}
