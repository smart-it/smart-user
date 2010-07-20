/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Privilege;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
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
@Path("/organizations/{organizationUniqueShortName}/privileges")
public class OrganizationPrivilegesResource extends AbstractResource{

    private String organizationUniqueShortName;

    static UriBuilder ORGANIZATION_PRIVILEGE_URIBUILDER;
    static UriBuilder ORGANIZATION_PRIVILEGE_AFTER_NAME_URIBUILDER;
    static UriBuilder ORGANIZATION_PRIVILEGE_BEFORE_NAME_URIBUILDER;

    static{
        ORGANIZATION_PRIVILEGE_URIBUILDER = UriBuilder.fromResource(OrganizationPrivilegesResource.class);
        ORGANIZATION_PRIVILEGE_BEFORE_NAME_URIBUILDER = UriBuilder.fromResource(OrganizationPrivilegesResource.class);

        try{
            ORGANIZATION_PRIVILEGE_BEFORE_NAME_URIBUILDER.path(OrganizationPrivilegesResource.class.getMethod("getBefore", String.class));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        ORGANIZATION_PRIVILEGE_AFTER_NAME_URIBUILDER = UriBuilder.fromResource(OrganizationPrivilegesResource.class);
        try{
            ORGANIZATION_PRIVILEGE_AFTER_NAME_URIBUILDER.path(OrganizationPrivilegesResource.class.getMethod("getAfter", String.class));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public OrganizationPrivilegesResource(@PathParam("organizationUniqueShortName") String organizationUniqueShortName){

        this.organizationUniqueShortName = organizationUniqueShortName;
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/before/{privilegeName}")
    public Response getBefore(@PathParam("privilegeName") String beforePrivilegeName) {
        return get(beforePrivilegeName, true);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/after/{privilegeName}")
    public Response getAfter(@PathParam("privilegeName") String afterPrivilegeName) {
      return get(afterPrivilegeName, false);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get(){
        return get(null, true);
    }

    public Response get(String privilegeName, boolean isBefore)
    {
        ResponseBuilder responseBuilder = Response.ok();

        // create a new atom feed
        Feed atomFeed = abderaFactory.newFeed();

        // create a link to parent resource, in this case now it is linked to root resource
        Link parentResourceLink = abderaFactory.newLink();
        parentResourceLink.setHref(UriBuilder.fromResource(OrganizationResource.class).build().toString());
        parentResourceLink.setRel("organization");
        atomFeed.addLink(parentResourceLink);

        // get the organizations accoring to the query
        Collection<Privilege> privileges = Services.getInstance().getPrivilegeService().getPrivilegesByOrganization(organizationUniqueShortName);        

        if(privileges != null && !privileges.isEmpty()){

            MultivaluedMap<String, String> queryParam = uriInfo.getQueryParameters();
            List<Privilege> privilegeList = new ArrayList<Privilege>(privileges);

            // uri builder for next and previous organizations according to count
            final UriBuilder nextUri = ORGANIZATION_PRIVILEGE_AFTER_NAME_URIBUILDER.clone();
            final UriBuilder previousUri = ORGANIZATION_PRIVILEGE_BEFORE_NAME_URIBUILDER.clone();

            // link to the next organizations based on count
            Link nextLink = abderaFactory.newLink();
            nextLink.setRel(Link.REL_NEXT);
            Privilege lastPrivilege = privilegeList.get(0);


            for(String key:queryParam.keySet()){
                final Object[] values = queryParam.get(key).toArray();
                nextUri.queryParam(key, values);
                previousUri.queryParam(key, values);
            }
            nextLink.setHref(nextUri.build(lastPrivilege.getName()).toString());
            //nextLink.setHref(UriBuilder.fromResource(OrganizationsResource.class).build(lastOrganization.getUniqueShortName()).toString());

            atomFeed.addLink(nextLink);

            /* link to the previous organizations based on count */
            Link prevLink = abderaFactory.newLink();
            prevLink.setRel(Link.REL_PREVIOUS);
            Privilege firstPrivilege = privilegeList.get(privileges.size() - 1);

            prevLink.setHref(previousUri.build(firstPrivilege.getName()).toString());
            //prevLink.setHref(nameLike)
            atomFeed.addLink(prevLink);

            // add entry of individual organization
            for (Privilege privilege : privileges) {
              Entry organizationEntry = abderaFactory.newEntry();

              organizationEntry.setId(privilege.getName().toString());
              organizationEntry.setTitle(privilege.getDisplayName());
              organizationEntry.setSummary(privilege.getShortDescription());
              //organizationEntry.setUpdated(privilege.);

              /* setting link to the individual organization resource*/

              Link organizationLink = abderaFactory.newLink();
              organizationLink.setHref(OrganizationResource.ORGANIZATION_URI_BUILDER.clone().build(privilege.getName()).toString());
              organizationLink.setRel(Link.REL_ALTERNATE);
              organizationLink.setMimeType(MediaType.APPLICATION_ATOM_XML);
              organizationEntry.addLink(organizationLink);

              atomFeed.addEntry(organizationEntry);
            }
        }
        responseBuilder.entity(atomFeed);
        return responseBuilder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(Privilege privilege){
        ResponseBuilder responseBuilder;
        try{
            responseBuilder = Response.status(Status.CREATED);
            if(privilege.getSecuredObjectID() != null){
                Services.getInstance().getSecuredObjectService().populateSecuredObject(privilege);
            }
            if(privilege.getParentOrganizationID() == null){
                throw new Exception("No parent Organization");
            }
            Services.getInstance().getOrganizationService().populateOrganization(privilege);
            Services.getInstance().getPrivilegeService().create(privilege);
        }catch(Exception ex){
            ex.printStackTrace();
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
        }
        return responseBuilder.build();
    }
}
