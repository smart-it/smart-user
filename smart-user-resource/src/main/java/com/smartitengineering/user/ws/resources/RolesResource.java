/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;


import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.impl.Services;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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

//@Path("/organizations/{organizationName}")
@Path("/test")
public class RolesResource extends AbstractResource{

    static final UriBuilder ROLE_URI_BUILDER;
    
    static{
        ROLE_URI_BUILDER = UriBuilder.fromResource(RoleResource.class);
    }

    
    @QueryParam("count")
    private Integer count;

    public RolesResource(){
        
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/organizations/{organizationName}/roles")
    public Response getForSuperAdmin(@PathParam("organizationName") String organizationName){
        return get(organizationName, null);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/organizations/{organizationName}/users/{userName}/roles")
    public Response getForAdmin(@PathParam("organizationName") String organizationName, @PathParam("userName") String userName){
        return get(organizationName, userName);
    }

    public Response get(String organizationName, String userName){
        boolean forSuperAdmin = false;
        if(count == null)
            count = 10;
        if(userName == null){
            forSuperAdmin = true;
        }


        ResponseBuilder responseBuilder = Response.status(Status.OK);
        Feed atomFeed = getFeed("roles", new Date());
        Link rolesLink = abderaFactory.newLink();

        rolesLink.setHref(UriBuilder.fromResource(OrganizationsResource.class).build().toString());
        rolesLink.setRel("organizations");
        atomFeed.addLink(rolesLink);

        //Collection<Role> roles = Services.getInstance().getRoleService().getRolesByOrganizationAndUser(organizationName, userName);
        List<Role> testList = new ArrayList<Role>();
        testList.add(new Role("Role 1","D Role 1","S Role 1"));
        testList.add(new Role("Role 2","D Role 2","S Role 2"));
        testList.add(new Role("Role 3","D Role 3","S Role 3"));

        Collection<Role> roles = testList;

        if(roles != null && !roles.isEmpty()){

            Link nextLink = abderaFactory.newLink();
            nextLink.setRel(Link.REL_NEXT);

            UriBuilder nextRoleUri = null;
            UriBuilder previousRoleUri = null;

            try{
                if(forSuperAdmin){
                    nextRoleUri = UriBuilder.fromResource(RolesResource.class).path(RolesResource.class.getMethod("getForSuperAdmin", String.class));
                    previousRoleUri = UriBuilder.fromResource(RolesResource.class).path(RolesResource.class.getMethod("getForSuperAdmin", String.class));
                }else{
                    nextRoleUri = UriBuilder.fromResource(RolesResource.class).path(RolesResource.class.getMethod("getForAdmin", String.class));
                    previousRoleUri = UriBuilder.fromResource(RolesResource.class).path(RolesResource.class.getMethod("getForAdmin", String.class));
                }
            }catch(Exception ex){
                ex.printStackTrace();
                responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
            }

            List<Role> roleList = new ArrayList<Role>();

            Role firstRole = roleList.get(0);
            nextLink.setHref(nextRoleUri.build(firstRole.getName()).toString());
            atomFeed.addLink(nextLink);

            Role lastRole = roleList.get(roleList.size() -1);

            Link previousLink = abderaFactory.newLink();
            previousLink.setRel(Link.REL_PREVIOUS);
            previousLink.setHref(previousRoleUri.build(lastRole.getName()).toString());

            atomFeed.addLink(previousLink);

            for(Role role:roles){
                Entry roleEntry = abderaFactory.newEntry();

                roleEntry.setId(role.getName());
                roleEntry.setTitle(role.getDisplayName());
                roleEntry.setSummary(role.getShortDescription());
                roleEntry.setUpdated(role.getLastModifiedDate());

                Link roleLink = abderaFactory.newLink();
                roleLink.setRel(Link.REL_ALTERNATE);
                roleLink.setHref(RolesResource.ROLE_URI_BUILDER.build(role.getName()).toString());
                roleLink.setMimeType(MediaType.APPLICATION_ATOM_XML);
                roleEntry.addLink(roleLink)                        ;
                atomFeed.addEntry(roleEntry);
            }
            
        }
        return responseBuilder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(Role role){
        ResponseBuilder responseBuilder;

        try{
            Services.getInstance().getRoleService().create(role);
            responseBuilder = Response.status(Status.CREATED);
        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.BAD_REQUEST);
        }
        return responseBuilder.build();
    }


}
