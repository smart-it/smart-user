/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.filter.UserPersonFilter;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.ws.element.UserElement;
import com.smartitengineering.user.ws.element.UserElements;
import com.smartitengineering.user.ws.element.UserPersonElement;
import com.smartitengineering.user.ws.element.UserPersonElements;
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
        type.post(userPersonElement);
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

    public User getUserByID(String username) {
        WebResource resource = getWebResource().path("user/" + username);        
        final UserElement userElement = resource.get(UserElement.class);
        return userElement.getUser();
    }

    public UserPerson getUserPersonByID(String username) {
        WebResource resource = getWebResource().path("userperson/" + username);        
        final UserPersonElement userPersonElement = resource.get(UserPersonElement.class);
        return userPersonElement.getUserPerson(); 
    }

}
