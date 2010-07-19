/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.SecuredObject;
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
@Path("/securedObjects")
public class SecuredObjectsResource {

    public SecuredObjectsResource(){
        
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
    public Response post(SecuredObject object){
        ResponseBuilder responseBuilder;
        try{
            responseBuilder =  Response.status(Status.CREATED);
            Services.getInstance().getSecuredObjectService().save(object);
        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder =  Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }
}
