/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;


import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.ws.element.UserElement;
import com.smartitengineering.user.ws.element.UserElements;
import com.smartitengineering.user.ws.element.UserFilterElement;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author modhu7
 */
@Path("user")
@Component
@Scope(value = "singleton")
public class UserResource {
    @Resource(name = "userService")
    private UserService userService;
    

    @PUT    
    @Consumes("application/xml")
    public void updateUser(UserElement userElement) {
        try {
            userService.update(userElement.getUser());
        } catch (Exception e) {
        }
    }

    @DELETE
    @Consumes("application/xml")
    public void deleteUser(@PathParam("id") Integer id) {
        try {
            userService.delete(userService.getUserByID(id));
        } catch (Exception e) {
        }
    }

    @POST
    @Path("search")
    @Consumes("application/xml")
    @Produces("application/xml")
    public UserElements searchUser(
            UserFilterElement userFilterElement) {
        UserElements userElements = new UserElements();
        UserFilter userFilter;
        if (userFilterElement != null && userFilterElement.getUserFilter() != null) {
            userFilter = userFilterElement.getUserFilter();
        } else {
            userFilter = new UserFilter();
        }        
        try {
            userElements.setUsers(userService.search(userFilter));
        } catch (Exception e) {
        }
        return userElements;
    }
    
    @GET
    @Path("{id}")
    @Produces("application/xml")
    public UserElement getUserByID(
            @PathParam("id") Integer id) {
        UserElement userElement = new UserElement();
        try {
            userElement.setUser(userService.getUserByID(id));
        } catch (Exception e) {
        }
        return userElement;
    }    
        
    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
