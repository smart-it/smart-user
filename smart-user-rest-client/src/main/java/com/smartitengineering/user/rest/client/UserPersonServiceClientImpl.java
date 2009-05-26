/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.filter.UserPersonFilter;
import com.smartitengineering.user.rest.client.exception.SmartException;
import com.smartitengineering.user.service.UserPersonService;
import com.smartitengineering.user.ws.element.ExceptionElement;
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
public class UserPersonServiceClientImpl extends AbstractClientImpl implements
        UserPersonService {

    public void create(UserPerson userPerson) {
        UserPersonElement userPersonElement = new UserPersonElement();
        userPersonElement.setUserPerson(userPerson);
        final Builder type =
                getWebResource().path("userperson").type("application/xml");
        try {
            type.post(userPersonElement);
        } catch (UniformInterfaceException ex) {
            ExceptionElement message =
                    ex.getResponse().
                    getEntity(ExceptionElement.class);
            int status = ex.getResponse().getStatus();
            throw new SmartException(message, status, ex);
        }
    }

    public void delete(UserPerson userPerson) {
        UserPersonElement userPersonElement = new UserPersonElement();
        userPersonElement.setUserPerson(userPerson);
        getWebResource().
                path("userperson/" + userPerson.getUser().getUsername()).
                delete();
    }

    public void deleteByPerson(Person person) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deleteByUser(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<UserPerson> getAllUserPerson() {
        WebResource resource = getWebResource().path("userperson/alluserperson");
        final UserPersonElements userPersonElements =
                resource.get(UserPersonElements.class);
        return userPersonElements.getUserPersons();
    }

    public UserPerson getUserPersonByUsername(String username) {
        WebResource resource = getWebResource().path("userperson/" + username);
        final UserPersonElement userPersonElement =
                resource.get(UserPersonElement.class);
        return userPersonElement.getUserPerson();
    }

    public Collection<UserPerson> search(UserPersonFilter filter) {
        MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.add("username", filter.getUsername());
        WebResource resource =
                getWebResource().path("userperson").queryParams(map);
        final UserPersonElements userPersonElements =
                resource.get(UserPersonElements.class);
        return userPersonElements.getUserPersons();
    }

    public void update(UserPerson userPerson) {
        UserPersonElement userPersonElement = new UserPersonElement();
        userPersonElement.setUserPerson(userPerson);
        final Builder type =
                getWebResource().path("userperson").type("application/xml");
        try {
            type.put(userPersonElement);
        } catch (UniformInterfaceException ex) {
            ExceptionElement message =
                    ex.getResponse().
                    getEntity(ExceptionElement.class);
            int status = ex.getResponse().getStatus();
            throw new SmartException(message, status, ex);
        }
    }
}
