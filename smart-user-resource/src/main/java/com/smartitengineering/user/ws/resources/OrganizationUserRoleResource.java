/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Role;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
@Path("/orgs/{organizationName}/users/{userName}/roles/name/{roleName}")
public class OrganizationUserRoleResource extends AbstractResource {

    static final UriBuilder ORGANIZATION_USER_ROLE_URI_BUILDER = UriBuilder.fromResource(RoleResource.class);
    static final UriBuilder ORGANIZATION_USER_ROLE_CONTENT_URI_BUILDER;

    static{
        ORGANIZATION_USER_ROLE_CONTENT_URI_BUILDER = ORGANIZATION_USER_ROLE_URI_BUILDER.clone();
        try {
            ORGANIZATION_USER_ROLE_CONTENT_URI_BUILDER.path(RoleResource.class.getMethod("getRole"));
        }
        catch (Exception ex) {
            throw new InstantiationError();
        }
    }    
    

    private Role role;

    public OrganizationUserRoleResource(@PathParam("organizationName")String organizationName, @PathParam("userName")String userName, @PathParam("roleName")String roleName){
        role = Services.getInstance().getRoleService().getRoleByOrganizationAndUserAndRole(organizationName, userName, roleName);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get(){

        Feed roleFeed = getRoleFeed();
        ResponseBuilder responseBuilder = Response.ok(roleFeed);
        return responseBuilder.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/content")
    public Response getRole(){
        ResponseBuilder responseBuilder = Response.ok(role);
        return responseBuilder.build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Role newRole) {
        ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
        try {

          Services.getInstance().getRoleService().update(role);
          responseBuilder = Response.ok(getRoleFeed());
        }
        catch (Exception ex) {
          responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }

    private Feed getRoleFeed() throws UriBuilderException, IllegalArgumentException{

        Feed roleFeed = getFeed(role.getDisplayName(), role.getLastModifiedDate());
        roleFeed.setTitle(role.getDisplayName());

        // add a selflink
        roleFeed.addLink(getSelfLink());

        // add a edit link
        Link editLink = abderaFactory.newLink();
        editLink.setHref(uriInfo.getRequestUri().toString());
        editLink.setRel(Link.REL_EDIT);
        editLink.setMimeType(MediaType.APPLICATION_JSON);

        // add alternate link
        Link altLink = abderaFactory.newLink();
        altLink.setHref(ORGANIZATION_USER_ROLE_CONTENT_URI_BUILDER.clone().build(role.getName()).toString());
        altLink.setRel(Link.REL_ALTERNATE);
        altLink.setMimeType(MediaType.APPLICATION_JSON);
        roleFeed.addLink(altLink);

        return roleFeed;
    }
    @DELETE
    public Response delete() {
        Services.getInstance().getRoleService().delete(role);
        ResponseBuilder responseBuilder = Response.ok();
        return responseBuilder.build();
    }

}
