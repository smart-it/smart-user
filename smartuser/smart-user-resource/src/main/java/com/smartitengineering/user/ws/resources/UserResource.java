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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
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
    public Response updateUser(UserElement userElement) {
        try {
            userService.update(userElement.getUser());
            return Response.ok().build();
        } catch (Exception e) {
            return Response.ok().build();
        }
    }

    @DELETE
    @Path("{username}")
    @Consumes("application/xml")
    public Response deleteUser(@PathParam("username") String username) {
        try {
            userService.delete(userService.getUserByUsername(username));
            return Response.ok().build();
        } catch (Exception e) {
            return Response.ok().build();
        }
    }

    @POST
    @Path("search")
    @Consumes("application/xml")
    @Produces("application/xml")
    public UserElements searchUserByPost(
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
    @Path("{username}")
    @Produces("application/xml")
    public UserElement getUserByID(
            @PathParam("username") String username) {
        UserElement userElement = new UserElement();
        try {
            userElement.setUser(userService.getUserByUsername(username));
        } catch (Exception e) {
        }
        return userElement;
    }

    @GET
    @Path("alluser")
    @Produces("application/xml")
    public UserElements getAllUser() {
        UserElements userElements = new UserElements();
        try {
            userElements.setUsers(userService.getAllUser());
        } catch (Exception e) {
        }
        return userElements;
    }

    @GET
    @Produces("application/xml")
    public UserElements searchUserByGet(
            @DefaultValue(value = "NO USERNAME") @QueryParam(value = "username") final String username) {
        UserFilter filter = new UserFilter();
        filter.setUsername(username);
        UserElements userElements = new UserElements();
        try {
            userElements.setUsers(userService.search(filter));
        } catch (Exception e) {
        }
        return userElements;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
