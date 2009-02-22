/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.filter.UserFilter;

import com.smartitengineering.user.rest.client.exception.SmartException;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.ws.element.ExceptionElement;
import com.smartitengineering.user.ws.element.UserElement;
import com.smartitengineering.user.ws.element.UserElements;
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
public class UserServiceClientImpl extends AbstractClientImpl implements UserService {

    public void update(User user) {
        UserElement userElement = new UserElement();
        userElement.setUser(user);
        final Builder type = getWebResource().path("user").type(
                "application/xml");
        try {
            type.put(userElement);
        } catch (UniformInterfaceException ex) {
            ExceptionElement message = ex.getResponse().getEntity(
                    ExceptionElement.class);
            int status = ex.getResponse().getStatus();
            throw new SmartException(message, status, ex);
        }
    }

    public void delete(User user) {
        UserElement userElement = new UserElement();
        userElement.setUser(user);
        try {
            getWebResource().path("user/" + user.getUsername()).delete();
        } catch (UniformInterfaceException ex) {
            ExceptionElement message = ex.getResponse().getEntity(
                    ExceptionElement.class);
            int status = ex.getResponse().getStatus();
            throw new SmartException(message, status, ex);
        }
    }

    public Collection<User> search(UserFilter filter) {
        MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.add("username", filter.getUsername());
        WebResource resource = getWebResource().path("user").queryParams(map);

        final UserElements userElements = resource.get(UserElements.class);
        return userElements.getUsers();
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

    public void validateUser(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
