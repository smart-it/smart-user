/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.impl.Services;
import com.smartitengineering.user.service.UserService;
import java.util.Calendar;
import java.util.Date;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author modhu7
 */

@Path("/organizations/{organizationName}/users/{username}")
@Component
@Scope(value = "singleton")
public class UserResource extends AbstractResource{

    private User user;

    static final UriBuilder USER_URI_BUILDER = UriBuilder.fromResource(UserResource.class);
    static final UriBuilder USER_CONTENT_URI_BUILDER;

    static{
        USER_CONTENT_URI_BUILDER = USER_URI_BUILDER.clone();
        try{
            USER_CONTENT_URI_BUILDER.path(UserResource.class.getMethod("getUser"));
        }catch(Exception ex){
            ex.printStackTrace();
            throw new InstantiationError();
        }
    }

    public UserResource(){
        
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get(){
        Feed userFeed = getUserFeed();
        ResponseBuilder responseBuilder = Response.ok(userFeed);
        return responseBuilder.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/content")
    public Response getOrganization() {
        ResponseBuilder responseBuilder = Response.ok(user);
        return responseBuilder.build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(User newUser) {
        ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
        try {            
            Services.getInstance().getUserService().save(newUser);
            user = Services.getInstance().getUserService().getUserByUsername(newUser.getUsername());
            responseBuilder = Response.ok(getUserFeed());
        }
        catch (Exception ex) {
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
            ex.printStackTrace();
        }
        return responseBuilder.build();
    }

    private Feed getUserFeed() throws UriBuilderException, IllegalArgumentException{
        Feed userFeed = getFeed(user.getUsername(), new Date());
        userFeed.addLink(getSelfLink());
        Link editLink = abderaFactory.newLink();
        editLink.setHref(uriInfo.getRequestUri().toString());
        editLink.setRel(Link.REL_EDIT);
        editLink.setMimeType(MediaType.APPLICATION_JSON);
        Link altLink = abderaFactory.newLink();
        altLink.setHref(USER_CONTENT_URI_BUILDER.clone().build(user.getUsername()).toString());
        altLink.setRel(Link.REL_ALTERNATE);
        altLink.setMimeType(MediaType.APPLICATION_JSON);
        userFeed.addLink(altLink);

        return userFeed;
    }

    @DELETE
    public Response delete() {
        Services.getInstance().getUserService().delete(user);
        ResponseBuilder responseBuilder = Response.ok();
        return responseBuilder.build();
    }
  
 
}
