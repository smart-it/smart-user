/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.service.RoleService;
import javax.annotation.Resource;
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author modhu7
 */


public class RoleResource {


    public RoleResource(){
        
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/organizations/{organizationName}/users/{user}/roles/{role}")
    public Response get(){
        ResponseBuilder responseBuilder;

        try{
            responseBuilder = Response.status(Status.OK);
        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.BAD_REQUEST);
        }

        return responseBuilder.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/organizations/{organizationName}/roles/{role}")
    public Response getRoleForSuperAdmin(){
        ResponseBuilder responseBuilder;

        try{
            responseBuilder = Response.status(Status.OK);
        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.BAD_REQUEST);
        }

        return responseBuilder.build();
    }    
}
