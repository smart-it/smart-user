/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.impl.Services;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author russel
 */
@Path("/privileges")
public class PrivilegesResource {

    public PrivilegesResource(){
        
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get(){
        ResponseBuilder responseBuilder; 
        try{
            responseBuilder = Response.status(Status.OK);
            
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
            responseBuilder = Response.status(Status.OK);
            Services.getInstance().getPrivilegeService().create(privilege);
        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }
}
