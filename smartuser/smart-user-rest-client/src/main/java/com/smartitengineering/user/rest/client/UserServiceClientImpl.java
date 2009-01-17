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
import com.smartitengineering.user.ws.element.UserPersonElement;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import java.util.Collection;

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<UserPerson> search(UserPersonFilter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public User getUserByID(Integer id) {
        WebResource resource = getWebResource().path("user/" +
                id);
        final UserElement element = resource.get(UserElement.class);
        final User user = element.getUser();
        return user;
    }

    public UserPerson getUserPersonByID(Integer id) {
        WebResource resource = getWebResource().path("userperson/" +
                id);
        final UserPersonElement element = resource.get(UserPersonElement.class);
        final UserPerson userPerson = element.getUserPerson();
        return userPerson;
    }

}
