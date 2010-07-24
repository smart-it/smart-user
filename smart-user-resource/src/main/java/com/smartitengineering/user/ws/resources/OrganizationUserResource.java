/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;


import com.smartitengineering.user.domain.User;
import com.sun.jersey.api.view.Viewable;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;

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


/**
 *
 * @author russel
 */
@Path("/organizations/{organizationShortName}/users/username/{userName}")
public class OrganizationUserResource extends AbstractResource{

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

    public OrganizationUserResource(@PathParam("organizationShortName") String organizationShortName,@PathParam("userName") String userName){
        user = Services.getInstance().getUserService().getUserByOrganizationAndUserName(organizationShortName, userName);
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

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml(){
        ResponseBuilder responseBuilder = Response.ok();

        Viewable view = new Viewable("OrganizationUserDetails", user, OrganizationResource.class);
        responseBuilder.entity(view);
        return responseBuilder.build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(User newUser) {
        ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
        try {
            if(user.getRoleIDs() != null){
                Services.getInstance().getRoleService().populateRole(user);
            }
            if(user.getPrivilegeIDs() != null){
                Services.getInstance().getPrivilegeService().populatePrivilege(user);
            }
            if(user.getParentOrganizationID() == null){
                throw new Exception("No organization found");
            }
            Services.getInstance().getOrganizationService().populateOrganization(user);
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
        userFeed.setTitle(user.getUsername());

        // add a self link
        userFeed.addLink(getSelfLink());

        // add a edit link
        Link editLink = abderaFactory.newLink();
        editLink.setHref(uriInfo.getRequestUri().toString());
        editLink.setRel(Link.REL_EDIT);
        editLink.setMimeType(MediaType.APPLICATION_JSON);

        // add a alternate link
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
