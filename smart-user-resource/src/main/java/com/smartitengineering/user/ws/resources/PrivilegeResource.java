/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.impl.Services;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
@Path("/privileges/{privilegeName}")
public class PrivilegeResource extends AbstractResource{

    private Privilege privilege;

    public PrivilegeResource(@PathParam("privilegeName") String privilegeName){
        
        privilege = Services.getInstance().getPrivilegeService().getPrivilegesByObjectID(privilegeName);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get(){
        ResponseBuilder responseBuilder;
        try{
            responseBuilder = Response.status(Status.OK);
            Feed privilegeFeed = getPrivilegeFeed();
            responseBuilder = Response.ok(privilegeFeed);
        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)

    public Response post(Privilege privilege){
        ResponseBuilder responseBuilder;
        try{
            responseBuilder = Response.status(Status.CREATED);
            Services.getInstance().getPrivilegeService().create(privilege);
        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }

    @DELETE
    public Response delete(){
        ResponseBuilder responseBuilder;
        try{
            responseBuilder = Response.status(Status.OK);
            Services.getInstance().getPrivilegeService().delete(privilege);
        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Privilege newPrivilege){
        ResponseBuilder responseBuilder;
        try{
            responseBuilder = Response.status(Status.OK);
            Services.getInstance().getPrivilegeService().delete(newPrivilege);
        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }


    private Feed getPrivilegeFeed() throws UriBuilderException, IllegalArgumentException{

        Feed privilegeFeed = abderaFactory.newFeed();

        privilegeFeed.setId(privilege.getObjectID());
        privilegeFeed.setTitle(privilege.getName());
        privilegeFeed.addLink(getSelfLink());

        Link editLink = abderaFactory.newLink();
        editLink.setHref(uriInfo.getRequestUri().toString());
        editLink.setRel(Link.REL_EDIT);
        editLink.setMimeType(MediaType.APPLICATION_JSON);


        Link altLink = abderaFactory.newLink();
        altLink.setHref(UriBuilder.fromResource(PrivilegeResource.class).build(privilege.getObjectID()).toString());
        altLink.setRel(Link.REL_ALTERNATE);
        altLink.setMimeType(MediaType.APPLICATION_JSON);

        privilegeFeed.addLink(editLink);
        privilegeFeed.addLink(altLink);

        return privilegeFeed;
    }
}
