/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
@Path("/orgs/{organizationName}/roles")
public class OrganizationRolesResource extends AbstractResource{

    static final UriBuilder ORGANIZATION_ROLE_URI_BUILDER;
    static final UriBuilder ORGANIZATION_ROLE_AFTER_ROLE_NAME_URI_BUILDER;
    static final UriBuilder ORGANIZATION_ROLE_BEFORE_ROLE_NAME_URI_BUILDER;

    static{
        ORGANIZATION_ROLE_URI_BUILDER = UriBuilder.fromResource(OrganizationRolesResource.class);

        ORGANIZATION_ROLE_AFTER_ROLE_NAME_URI_BUILDER = UriBuilder.fromResource(OrganizationRolesResource.class);
        try{
            ORGANIZATION_ROLE_AFTER_ROLE_NAME_URI_BUILDER.path(RolesResource.class.getMethod("getAfter", String.class));
        }catch(Exception ex){
            ex.printStackTrace();
        }

        ORGANIZATION_ROLE_BEFORE_ROLE_NAME_URI_BUILDER = UriBuilder.fromResource(OrganizationRolesResource.class);
        try{
            ORGANIZATION_ROLE_BEFORE_ROLE_NAME_URI_BUILDER.path(RolesResource.class.getMethod("getBefore", String.class));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private String organizationName;

    @QueryParam("count")
    private Integer count;

    public OrganizationRolesResource(@PathParam("organizationName") String organizationName){
        this.organizationName = organizationName;
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/after/{roleName}")
    public Response getAfter(@PathParam("roleName") String roleName){
        return get(roleName, false);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/before/{roleName}")
    public Response getBefore(@PathParam("roleName") String roleName){
        return get(roleName, true);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get(){
        return get(null, true);
    }
   
    private Response get(String roleName, boolean isBefore){

        if(count == null){
            count = 10;
        }
        ResponseBuilder responseBuilder = Response.status(Status.OK);;
        
                        
        Feed atomFeed = abderaFactory.newFeed();
        atomFeed.setId(UriBuilder.fromResource(OrganizationRolesResource.class).build().toString());
        atomFeed.setTitle(organizationName + " Roles");
        Link newLink = abderaFactory.newLink();
        newLink.setHref(UriBuilder.fromResource(OrganizationRolesResource.class).build().toString());
        newLink.setRel(Link.REL_ALTERNATE);


        Collection<Role> roles = Services.getInstance().getRoleService().getRolesByOrganization(organizationName);

        if(roles != null && !roles.isEmpty()){

            // uri builder for next and previous uri according to the count value
            UriBuilder nextRoleUri = ORGANIZATION_ROLE_AFTER_ROLE_NAME_URI_BUILDER.clone();
            UriBuilder previousRoleUri = ORGANIZATION_ROLE_BEFORE_ROLE_NAME_URI_BUILDER.clone();

            List<Role> roleList = new ArrayList<Role>(roles);

            // link to the next uri according to the count value
            Link nextLink = abderaFactory.newLink();
            nextLink.setRel(Link.REL_NEXT);

            Role firstRole = roleList.get(0);
            nextLink.setHref(nextRoleUri.build(organizationName, firstRole.getName()).toString());
            atomFeed.addLink(nextLink);

            Role lastRole = roleList.get(roleList.size() -1);

            // link to the previous uri according to the count value
            Link previousLink = abderaFactory.newLink();
            previousLink.setRel(Link.REL_PREVIOUS);
            previousLink.setHref(previousRoleUri.build(organizationName, lastRole.getName()).toString());

            atomFeed.addLink(previousLink);

            for(Role role:roles){
                Entry roleEntry = abderaFactory.newEntry();

                roleEntry.setId(role.getName());
                roleEntry.setTitle(role.getDisplayName());
                roleEntry.setUpdated(role.getLastModifiedDate());

                Link roleLink = abderaFactory.newLink();

                roleLink.setHref(UriBuilder.fromResource(OrganizationRoleResource.class).build(organizationName, role.getName()).toString());
                roleLink.setRel(Link.REL_ALTERNATE);
                roleLink.setMimeType(MediaType.APPLICATION_ATOM_XML);

                roleEntry.addLink(roleLink);
                atomFeed.addEntry(roleEntry);
            }            
        }
        responseBuilder.entity(atomFeed);
        return responseBuilder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)

    public Response post(Role role){
        ResponseBuilder responseBuilder;
        try{
            responseBuilder = Response.status(Status.CREATED);
            if(role.getParentOrganizationID() == null){
                throw new Exception("No parent organization found");
            }
            Services.getInstance().getOrganizationService().populateOrganization(role);

            if(role.getPrivilegeIDs() != null && !role.getPrivilegeIDs().isEmpty()){
                Services.getInstance().getPrivilegeService().populatePrivilege(role);
            }
            if(role.getRoleIDs() != null && ! role.getRoleIDs().isEmpty()){
                Services.getInstance().getRoleService().populateRole(role);
            }
            Services.getInstance().getRoleService().create(role);
        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }

}
