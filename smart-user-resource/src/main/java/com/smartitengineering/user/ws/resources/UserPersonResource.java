/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.filter.UserPersonFilter;
import com.smartitengineering.user.service.UserPersonService;
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
import javax.ws.rs.core.Response.Status;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author modhu7
 */
@Path("userperson")
@Component
@Scope(value = "singleton")
public class UserPersonResource {
/*
    @Resource(name = "userPersonService")
    private UserPersonService userPersonService;

    @POST
    @Consumes("application/xml")
    public Response create(UserPersonElement userPersonElement) {
        try {
            userPersonService.create(userPersonElement.getUserPerson());
            return Response.ok().build();
        } catch (Exception e) {
            String group = e.getMessage().split("-")[0];
            String field = e.getMessage().split("-")[1];
            ExceptionElement exceptionElement = new ExceptionElement();
            exceptionElement.setGroup(group);
            exceptionElement.setFieldCausedBy(field);
            return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).
                    entity(exceptionElement).build();
        }
    }

    @PUT
    @Consumes("application/xml")
    public Response updateUserPerson(UserPersonElement userPersonElement) {
        try {
            userPersonService.update(userPersonElement.getUserPerson());
            return Response.ok().build();
        } catch (RuntimeException e) {
            String group = e.getMessage().split("-")[0];
            String field = e.getMessage().split("-")[1];
            ExceptionElement exceptionElement = new ExceptionElement();
            exceptionElement.setGroup(group);
            exceptionElement.setFieldCausedBy(field);
            return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).
                    entity(exceptionElement).build();
        }
    }

    @DELETE
    @Path("{username}")
    @Consumes("application/xml")
    public void deleteUserPerson(@PathParam("username") String username) {
        try {
            userPersonService.delete(userPersonService.getUserPersonByUsername(
                    username));
        } catch (Exception e) {
        }
    }

    @POST
    @Path("search")
    @Consumes("application/xml")
    @Produces("application/xml")
    public UserPersonElements searchUserPerson(
            UserPersonFilterElement userPersonFilterElement) {
        UserPersonElements userPersonElements = new UserPersonElements();
        UserPersonFilter userPersonFilter;
        if (userPersonFilterElement != null && userPersonFilterElement.
                getUserPersonFilter() != null) {
            userPersonFilter = userPersonFilterElement.getUserPersonFilter();
        } else {
            userPersonFilter = new UserPersonFilter();
        }
        try {
            userPersonElements.setUserPersons(userPersonService.search(
                    userPersonFilter));
        } catch (Exception e) {
        }
        return userPersonElements;
    }

    @GET
    @Path("{username}")
    @Produces("application/xml")
    public UserPersonElement getUserPersonByID(
            @PathParam("username") String username) {
        UserPersonElement userPersonElement = new UserPersonElement();
        try {
            userPersonElement.setUserPerson(userPersonService.
                    getUserPersonByUsername(username));
        } catch (Exception e) {
        }
        return userPersonElement;
    }

    @GET
    @Path("alluserperson")
    @Produces("application/xml")
    public UserPersonElements getAllUser() {
        UserPersonElements userPersonElements = new UserPersonElements();
        try {
            userPersonElements.setUserPersons(
                    userPersonService.getAllUserPerson());
        } catch (Exception e) {
        }
        return userPersonElements;
    }

    @GET
    @Produces("application/xml")
    public UserPersonElements searchUserByGet(
            @DefaultValue(value = "NO USERNAME") @QueryParam(value = "username") final String username) {
        UserPersonFilter filter = new UserPersonFilter();
        filter.setUsername(username);
        UserPersonElements userPersonElements = new UserPersonElements();
        try {
            userPersonElements.setUserPersons(userPersonService.search(filter));
        } catch (Exception e) {
        }
        return userPersonElements;
    }

    public UserPersonService getUserPersonService() {
        return userPersonService;
    }

    public void setUserPersonService(UserPersonService userPersonService) {
        this.userPersonService = userPersonService;
    }
 * 
 */
}
