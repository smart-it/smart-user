/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.impl.Services;
import com.smartitengineering.user.domain.Organization;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author russel
 */
@Path("/organizations/{name}")
public class OrganizationResource {

    private Organization organization;

    public OrganizationResource(@PathParam("name") String name){

        organization = Services.getInstance().getOrganizationService().getOrganizationByOrganizationName(name);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(){

        ResponseBuilder responseBuilder = Response.ok(organization);
        return responseBuilder.build();
    }

    @PUT
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(){
        ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
        return responseBuilder.build();
    }

}
