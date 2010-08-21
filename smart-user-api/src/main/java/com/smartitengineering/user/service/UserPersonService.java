/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.filter.UserPersonFilter;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public interface UserPersonService {

    void create(UserPerson userPerson);

    void delete(UserPerson userPerson);

    void deleteByPerson(Person person);

    void deleteByUser(User user);

    Collection<UserPerson> getAllUserPerson();

    UserPerson getUserPersonByUsernameAndOrgName(String username, String orgName);

    Collection<UserPerson> search(UserPersonFilter filter);

    void update(UserPerson userPerson);
}
