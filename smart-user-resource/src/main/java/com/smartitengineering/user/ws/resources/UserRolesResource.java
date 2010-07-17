/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.impl.Services;
import java.util.Collection;
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
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
@Path("/notapplicable/organizations/{organizationName}/users/{userName}/roles")
public class UserRolesResource extends AbstractResource{

    private String organizationName;
    private String userName;

    public UserRolesResource(@PathParam("organizationName") String organizationName, @PathParam("userName") String userName){
        this.organizationName = organizationName;
        this.userName = userName;
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get(){
        ResponseBuilder responseBuilder;

        try{
            responseBuilder = Response.status(Status.OK);
            
            Feed atomFeed = abderaFactory.newFeed();
            atomFeed.setId(UriBuilder.fromResource(OrganizationRolesResource.class).build().toString());
            atomFeed.setTitle(organizationName + " Roles");
            Link newLink = abderaFactory.newLink();
            newLink.setHref(UriBuilder.fromResource(OrganizationRolesResource.class).build().toString());
            newLink.setRel(Link.REL_ALTERNATE);


            Collection<Role> roles = Services.getInstance().getRoleService().getRolesByOrganizationAndUser(organizationName, userName);

            for(Role role:roles){
                Entry roleEntry = abderaFactory.newEntry();

                roleEntry.setId(role.getName());
                roleEntry.setTitle(role.getDisplayName());
                roleEntry.setUpdated(role.getLastModifiedDate());

                Link roleLink = abderaFactory.newLink();

                roleLink.setHref(UriBuilder.fromResource(RoleResource.class).build(role.getName()).toString());
                roleLink.setRel(Link.REL_ALTERNATE);
                roleLink.setMimeType(MediaType.APPLICATION_ATOM_XML);

                roleEntry.addLink(roleLink);
                atomFeed.addEntry(roleEntry);
            }
            responseBuilder.entity(atomFeed);

        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.BAD_REQUEST);
        }
        return responseBuilder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(Role role){
        ResponseBuilder responseBuilder;
        try{
            responseBuilder = Response.status(Status.OK);
            Services.getInstance().getRoleService().create(role);
        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }
}
