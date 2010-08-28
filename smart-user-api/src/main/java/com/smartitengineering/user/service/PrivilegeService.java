/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service;

import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.User;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public interface PrivilegeService {

    void create(Privilege privilege);

    void delete(Privilege privilege);

    Privilege getPrivilegeByName(String name);

    Collection<Privilege> getPrivilegesByOrganizationNameAndObjectID(String organizationName,String objectID);
    //Privilege getPrivilegesByObjectID(String objectID);

    Privilege getPrivilegeByOrganizationAndPrivilegeName(String organizationName, String privilegename);

    Collection<Privilege> getPrivilegesByOrganizationAndUser(String organizationName, String userName);

    Collection<Privilege> getPrivilegesByOrganization(String organization);

    void update(Privilege privilege);

    void populatePrivilege(User user) throws Exception;

    void populatePrivilege(Role role) throws Exception;
}
