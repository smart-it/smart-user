/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Privilege;
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
@Path("/organizations/{organizationName}/users/{userName}/privileges")
public class UserPrivilegesResource extends AbstractResource{

    private String organizationName;
    private String userName;

    public UserPrivilegesResource(@PathParam("organizationName")String organizationName, @PathParam("userName")String userName){
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

            atomFeed.setId(UriBuilder.fromResource(UserPrivilegesResource.class).build().toString());
            atomFeed.setTitle(organizationName + ":"+userName+":privileges");

            Link newLink = abderaFactory.newLink();
            newLink.setHref(UriBuilder.fromResource(UserPrivilegesResource.class).build().toString());
            newLink.setRel(Link.REL_ALTERNATE);

            
            Collection<Privilege> privileges = Services.getInstance().getPrivilegeService().getPrivilegesByOrganizationAndUser(organizationName, userName);

            for(Privilege privilege:privileges){
                Entry roleEntry = abderaFactory.newEntry();

                roleEntry.setId(privilege.getName());
                roleEntry.setTitle(privilege.getName());
                //roleEntry.setUpdated(privilege.getLastModifiedDate());

                Link roleLink = abderaFactory.newLink();

                roleLink.setHref(UriBuilder.fromResource(PrivilegesResource.class).build(privilege.getName()).toString());
                roleLink.setRel(Link.REL_ALTERNATE);
                roleLink.setMimeType(MediaType.APPLICATION_ATOM_XML);

                roleEntry.addLink(roleLink);
                atomFeed.addEntry(roleEntry);
            }
            responseBuilder.entity(atomFeed);

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
            responseBuilder = Response.status(Status.CREATED);
            Services.getInstance().getPrivilegeService().create(privilege);

        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }

}
