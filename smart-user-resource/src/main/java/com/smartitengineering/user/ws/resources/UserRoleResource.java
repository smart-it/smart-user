/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.impl.Services;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.model.Feed;

/**
 *
 * @author russel
 */
@Path("/notapplicable/organizations/{organizationName}/users/{userName}/roles/{roleName}")
public class UserRoleResource extends AbstractResource{

    private String organizationName;
    private String userName;
    private String roleName;

    private Role role;

    public UserRoleResource(@PathParam("organizationName") String organizationName, @PathParam("userName") String userName, @PathParam("roleName") String roleName){

        this.organizationName = organizationName;
        this.userName = userName;
        this.roleName = roleName;

        role = Services.getInstance().getRoleService().getRoleByOrganizationAndUserAndUserID(organizationName, userName, roleName);        
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get(){
        ResponseBuilder responseBuilder;
        try{
            Feed roleFeed = getRoleFeed();
            responseBuilder=  Response.ok(roleFeed);
        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(){
        ResponseBuilder responseBuilder;
        try{
            responseBuilder = Response.status(Status.CREATED);
            Services.getInstance().getRoleService().update(role);

        }catch(Exception ex){
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }

    private Feed getRoleFeed(){
        
        Feed atomFeed = abderaFactory.newFeed();
        
        atomFeed.setId(UriBuilder.fromResource(UserRoleResource.class).build().toString());
        atomFeed.setTitle(role.getDisplayName());
        atomFeed.setUpdated(role.getLastModifiedDate());
        return atomFeed;
    }
}
