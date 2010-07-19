/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.SecuredObject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author russel
 */
@Path("/organizations/{organizationName}/users/{userName}/securedobjects")
public class UserSecuredObjectsResource {

    private String organizationName;
    private String userName;

    public UserSecuredObjectsResource(@PathParam("organizationName") String organizationName, @PathParam("userName") String userName ){
        this.organizationName = organizationName;
        this.userName = userName;
    }

    public Response get(){
        ResponseBuilder responseBuilder;
        try{
            responseBuilder = Response.status(Status.OK);
        }catch(Exception ex){
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }

    public Response post(SecuredObject securedObject){
        ResponseBuilder responseBuilder;
        try{
            responseBuilder = Response.status(Status.CREATED);
            Services.getInstance().getSecuredObjectService().save(securedObject);
        }catch(Exception ex){
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }
}
