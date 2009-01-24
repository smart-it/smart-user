/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.filter.UserPersonFilter;
import com.smartitengineering.user.rest.client.exception.SmartException;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.ws.element.PrivilegeElement;
import com.smartitengineering.user.ws.element.PrivilegeElements;
import com.smartitengineering.user.ws.element.RoleElement;
import com.smartitengineering.user.ws.element.RoleElements;
import com.smartitengineering.user.ws.element.UserElement;
import com.smartitengineering.user.ws.element.UserElements;
import com.smartitengineering.user.ws.element.UserPersonElement;
import com.smartitengineering.user.ws.element.UserPersonElements;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.Collection;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author modhu7
 */
public class UserServiceClientImpl extends AbstractClientImpl implements UserService{

    public void create(UserPerson userPerson) {
        UserPersonElement userPersonElement = new UserPersonElement();
        userPersonElement.setUserPerson(userPerson);
        final Builder type = getWebResource().path("userperson").type("application/xml");
        try {
            type.post(userPersonElement);
        }
        catch(UniformInterfaceException ex) {
            String message = ex.getResponse().getEntity(String.class);
            int status = ex.getResponse().getStatus();
            throw new SmartException(message, status, ex);
        }
    }

    public void update(User user) {
        UserElement userElement = new UserElement();
        userElement.setUser(user);
        final Builder type = getWebResource().path("user").type("application/xml");
        type.put(userElement);
    }

    public void delete(User user) {
        UserElement userElement = new UserElement();
        userElement.setUser(user);
        final Builder type = getWebResource().path("user").type("application/xml");
        type.post(userElement);
    }

    public void update(UserPerson userPerson) {
        UserPersonElement userPersonElement = new UserPersonElement();
        userPersonElement.setUserPerson(userPerson);
        final Builder type = getWebResource().path("userperson").type("application/xml");
        type.put(userPersonElement);
    }

    public void delete(UserPerson userPerson) {
        UserPersonElement userPersonElement = new UserPersonElement();
        userPersonElement.setUserPerson(userPerson);
        final Builder type = getWebResource().path("userperson").type("application/xml");
        type.delete(userPersonElement);
    }

    public Collection<User> search(UserFilter filter) {
        MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.add("username", filter.getUsername());
        WebResource resource = getWebResource().path("user").queryParams(map);
        
        final UserElements userElements = resource.get(UserElements.class);
        return userElements.getUsers();
    }

    public Collection<UserPerson> search(UserPersonFilter filter) {
        MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.add("username", filter.getUsername());
        WebResource resource = getWebResource().path("userperson").queryParams(map);
        
        final UserPersonElements userPersonElements = resource.get(UserPersonElements.class);
        return userPersonElements.getUserPersons(); 
    }    

    public Collection<UserPerson> getAllUserPerson() {
        WebResource resource = getWebResource().path("userperson/alluserperson");        
        final UserPersonElements userPersonElements = resource.get(UserPersonElements.class);
        return userPersonElements.getUserPersons(); 
    }

    public Collection<User> getAllUser() {
        WebResource resource = getWebResource().path("user/alluser");        
        final UserElements userElements = resource.get(UserElements.class);
        return userElements.getUsers();
    }

    public User getUserByUsername(String username) {
        WebResource resource = getWebResource().path("user/" + username);        
        final UserElement userElement = resource.get(UserElement.class);
        return userElement.getUser();
    }

    public UserPerson getUserPersonByUsername(String username) {
        WebResource resource = getWebResource().path("userperson/" + username);        
        final UserPersonElement userPersonElement = resource.get(UserPersonElement.class);
        return userPersonElement.getUserPerson(); 
    }

    public void create(Role role) {
        RoleElement roleElement = new RoleElement();
        roleElement.setRole(role);
        final Builder type = getWebResource().path("role").type("application/xml");
        type.post(roleElement);
    }

    public void update(Role role) {
        RoleElement roleElement = new RoleElement();
        roleElement.setRole(role);
        final Builder type = getWebResource().path("role").type("application/xml");
        type.put(roleElement);
    }

    public void delete(Role role) {
        RoleElement roleElement = new RoleElement();
        roleElement.setRole(role);
        final Builder type = getWebResource().path("role").type("application/xml");
        type.delete(roleElement);
    }

    public Role getRoleByName(String name) {
        WebResource resource = getWebResource().path("role/" + name);        
        final RoleElement roleElement = resource.get(RoleElement.class);
        return roleElement.getRole();
    }

    public void create(Privilege privilege) {
        PrivilegeElement privilegeElement = new PrivilegeElement();
        privilegeElement.setPrivilege(privilege);
        final Builder type = getWebResource().path("privilege").type("application/xml");
        type.post(privilegeElement);
    }

    public void update(Privilege privilege) {
        PrivilegeElement privilegeElement = new PrivilegeElement();
        privilegeElement.setPrivilege(privilege);
        final Builder type = getWebResource().path("privilege").type("application/xml");
        type.put(privilegeElement);
    }

    public void delete(Privilege privilege) {
        PrivilegeElement privilegeElement = new PrivilegeElement();
        privilegeElement.setPrivilege(privilege);
        final Builder type = getWebResource().path("privilege").type("application/xml");
        type.delete(privilegeElement);
    }

    public Privilege getPrivilegeByName(String name) {
        System.out.println("Client " + name);
        WebResource resource = getWebResource().path("privilege/" + name);        
        final PrivilegeElement privilegeElement = resource.get(PrivilegeElement.class);
        return privilegeElement.getPrivilege();
    }

    public Collection<Role> getRolesByName(String name) {
        WebResource resource = getWebResource().path("role/"+"search/"+name);
        final RoleElements roleElements = resource.get(RoleElements.class);
        return roleElements.getRoles();
        
        
    }

    public Collection<Privilege> getPrivilegesByName(String name) {
        WebResource resource = getWebResource().path("privilege/"+"search/"+name);
        final PrivilegeElements privilegeElements = resource.get(PrivilegeElements.class);
        return privilegeElements.getPrivileges();
    }

}
