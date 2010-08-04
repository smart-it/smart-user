/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;


import com.smartitengineering.user.domain.Organization;
import com.sun.jersey.api.view.Viewable;
import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
@Path("/organizations/shortname/{uniqueShortName}")
public class OrganizationResource extends AbstractResource {

    static final UriBuilder ORGANIZATION_URI_BUILDER = UriBuilder.fromResource(OrganizationResource.class);
    static final UriBuilder ORGANIZATION_CONTENT_URI_BUILDER;

    static {
        ORGANIZATION_CONTENT_URI_BUILDER = ORGANIZATION_URI_BUILDER.clone();
        try {
            ORGANIZATION_CONTENT_URI_BUILDER.path(OrganizationResource.class.getMethod("getOrganization"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new InstantiationError();
        }
    }
    private Organization organization;

    public OrganizationResource(@PathParam("uniqueShortName") String uniqueShortName) {
        organization = Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(uniqueShortName);
        
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get() {
        Feed organizationFeed = getOrganizationFeed();
        ResponseBuilder responseBuilder = Response.ok(organizationFeed);
        return responseBuilder.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/content")
    public Response getOrganization() {
        ResponseBuilder responseBuilder = Response.ok(organization);
        return responseBuilder.build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml(){
        ResponseBuilder responseBuilder = Response.ok();
        
        Viewable view = new Viewable("OrganizationDetails", organization, OrganizationResource.class);
        responseBuilder.entity(view);
        return responseBuilder.build();
    }

//    @PUT
    @POST

    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Organization newOrganization) {
        ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
        try {            
            Services.getInstance().getOrganizationService().update(newOrganization);
            organization = Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(newOrganization.getUniqueShortName());
            responseBuilder = Response.ok(getOrganizationFeed());
        }        
        catch (Exception ex) {
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }    

//    @DELETE
    @POST
    public Response delete() {
        Services.getInstance().getOrganizationService().delete(organization);
        ResponseBuilder responseBuilder = Response.ok();
        return responseBuilder.build();
    }

    private Feed getOrganizationFeed() throws UriBuilderException, IllegalArgumentException {
        
        Feed organizationFeed = getFeed(organization.getName(), organization.getLastModifiedDate());
        organizationFeed.setTitle(organization.getName());

        // add a self link
        organizationFeed.addLink(getSelfLink());

        // add a edit link
        Link editLink = abderaFactory.newLink();
        editLink.setHref(uriInfo.getRequestUri().toString());
        editLink.setRel(Link.REL_EDIT);
        editLink.setMimeType(MediaType.APPLICATION_JSON);

        // add a alternate link
        Link altLink = abderaFactory.newLink();
        altLink.setHref(ORGANIZATION_CONTENT_URI_BUILDER.clone().build(organization.getUniqueShortName()).toString());
        altLink.setRel(Link.REL_ALTERNATE);
        altLink.setMimeType(MediaType.APPLICATION_JSON);
        organizationFeed.addLink(altLink);
        
        return organizationFeed;
    }
}
