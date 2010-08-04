/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.UserGroup;
import com.sun.jersey.api.view.Viewable;
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
@Path("/organizations/{uniqueShortName}/usergroups/groupname/{name}")
public class OrganizationUserGroupResource extends AbstractResource{

    private UserGroup userGroup;

    static final UriBuilder ORGANIZATION_USER_GROUP_URI_BUILDER = UriBuilder.fromResource(OrganizationUserGroupResource.class);
    static final UriBuilder ORGANIZATION_USER_GROUP_CONTENT_URI_BUILDER;

    static{
        ORGANIZATION_USER_GROUP_CONTENT_URI_BUILDER = ORGANIZATION_USER_GROUP_URI_BUILDER.clone();
        try{
            ORGANIZATION_USER_GROUP_CONTENT_URI_BUILDER.path(OrganizationUserGroupResource.class.getMethod("getUserGroup"));
        }catch(Exception ex){
            ex.printStackTrace();
            throw new InstantiationError();
        }
    }

    public OrganizationUserGroupResource(@PathParam("uniqueShortName") String uniqueShortname, @PathParam("name") String name){
        userGroup = Services.getInstance().getUserGroupService().getByOrganizationAndUserGroupName(uniqueShortname, name);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get() {
        Feed userFeed = getUserGroupFeed();
        ResponseBuilder responseBuilder = Response.ok(userFeed);
        return responseBuilder.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/content")
    public Response getOrganization() {
        ResponseBuilder responseBuilder = Response.ok(userGroup);
        return responseBuilder.build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml() {
        ResponseBuilder responseBuilder = Response.ok();

        Viewable view = new Viewable("OrganizationUserDetails", userGroup, OrganizationResource.class);
        responseBuilder.entity(view);
        return responseBuilder.build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(UserGroup newUserGroup) {
        ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
        try {
//            if (userGroup.getRoleIDs() != null) {
//                Services.getInstance().getRoleService().populateRole(user);
//            }
//            if (user.getPrivilegeIDs() != null) {
//                Services.getInstance().getPrivilegeService().populatePrivilege(user);
//            }
            if (userGroup.getParentOrganizationID() == null) {
                throw new Exception("No organization found");
            }
            Services.getInstance().getOrganizationService().populateOrganization(userGroup);
            Services.getInstance().getUserGroupService().update(userGroup);
            userGroup = Services.getInstance().getUserGroupService().getByOrganizationAndUserGroupName(newUserGroup.getOrganization().getUniqueShortName(), newUserGroup.getName()); //.getUserByUsername(newUser.getUsername());
            responseBuilder = Response.ok(getUserGroupFeed());
        } catch (Exception ex) {
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
            ex.printStackTrace();
        }
        return responseBuilder.build();
    }

    private Feed getUserGroupFeed() throws UriBuilderException, IllegalArgumentException {
        Feed userFeed = getFeed(userGroup.getName(), new Date());
        userFeed.setTitle(userGroup.getName());

        // add a self link
        userFeed.addLink(getSelfLink());

        // add a edit link
        Link editLink = abderaFactory.newLink();
        editLink.setHref(uriInfo.getRequestUri().toString());
        editLink.setRel(Link.REL_EDIT);
        editLink.setMimeType(MediaType.APPLICATION_JSON);

        // add a alternate link
        Link altLink = abderaFactory.newLink();
        altLink.setHref(ORGANIZATION_USER_GROUP_CONTENT_URI_BUILDER.clone().build(userGroup.getName()).toString());
        altLink.setRel(Link.REL_ALTERNATE);
        altLink.setMimeType(MediaType.APPLICATION_JSON);
        userFeed.addLink(altLink);

        return userFeed;
    }

    @DELETE
    public Response delete() {
        Services.getInstance().getUserGroupService().delete(userGroup);
        ResponseBuilder responseBuilder = Response.ok();
        return responseBuilder.build();
    }

}
