/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.SecuredObject;
import java.util.Date;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
@Path("/orgs/{organizationUniqueShortName}/securedObjects/{old}")
public class OrganizationSecuredObjectResource extends AbstractResource{

    private SecuredObject securedObject;
    private String organizationUniqueName;

    static final UriBuilder ORGANIZATION_SECURED_OBJECT_URI_BUILDER = UriBuilder.fromResource(OrganizationSecuredObjectResource.class);
    static final UriBuilder ORGANIZATION_SECURED_OBJECT_CONTENT_URI_BUILDER;
    static{
        ORGANIZATION_SECURED_OBJECT_CONTENT_URI_BUILDER = ORGANIZATION_SECURED_OBJECT_URI_BUILDER.clone();
        try{
            ORGANIZATION_SECURED_OBJECT_CONTENT_URI_BUILDER.path(OrganizationSecuredObjectResource.class.getMethod("getSecuredObject"));
        }catch(Exception ex){
            ex.printStackTrace();
            throw new InstantiationError();
        }
    }
    
    public OrganizationSecuredObjectResource(@PathParam("organizationUniqueShortName") String organizationUniqueShortName, @PathParam("old") String name){
        this.organizationUniqueName = organizationUniqueShortName;
        this.securedObject = Services.getInstance().getSecuredObjectService().getByOrganizationAndObjectID(organizationUniqueShortName, name);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get() {
        Feed securedObjectFeed = getSecuredObjectFeed();
        ResponseBuilder responseBuilder = Response.ok(securedObjectFeed);
        return responseBuilder.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/content")
    public Response getSecuredObject() {
        ResponseBuilder responseBuilder = Response.ok(securedObject);
        return responseBuilder.build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(SecuredObject newSecuredObject) {
        ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
        try {
            if(securedObject.getParentOrganizationID() == null){
                throw new Exception("No parent Organization");
            }
            Services.getInstance().getOrganizationService().populateOrganization(securedObject);
            Services.getInstance().getSecuredObjectService().save(newSecuredObject);
            //newSecuredObject = Services.getInstance().getSecuredObjectService().getByObjectID(newSecuredObject.getObjectID());
            responseBuilder = Response.ok(getSecuredObjectFeed());
        }
        catch (Exception ex) {
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }

    @DELETE
    public Response delete() {
        Services.getInstance().getSecuredObjectService().delete(securedObject);
        ResponseBuilder responseBuilder = Response.ok();
        return responseBuilder.build();
    }

    private Feed getSecuredObjectFeed() throws UriBuilderException, IllegalArgumentException {

        Feed securedObjectFeed = getFeed(securedObject.getObjectID(), new Date());
        securedObjectFeed.setTitle(securedObject.getObjectID());

        // add a self link
        securedObjectFeed.addLink(getSelfLink());

        // add a edit link
        Link editLink = abderaFactory.newLink();
        editLink.setHref(uriInfo.getRequestUri().toString());
        editLink.setRel(Link.REL_EDIT);
        editLink.setMimeType(MediaType.APPLICATION_JSON);

        // add a alternate link
        Link altLink = abderaFactory.newLink();
        altLink.setHref(ORGANIZATION_SECURED_OBJECT_CONTENT_URI_BUILDER.clone().build(organizationUniqueName,securedObject.getObjectID()).toString());
        altLink.setRel(Link.REL_ALTERNATE);
        altLink.setMimeType(MediaType.APPLICATION_JSON);
        securedObjectFeed.addLink(altLink);

        return securedObjectFeed;
    }
}
