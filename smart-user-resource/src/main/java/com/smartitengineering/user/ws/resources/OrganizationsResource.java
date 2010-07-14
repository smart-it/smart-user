/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;


/**
 *
 * @author russel
 */

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.impl.Services;
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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;


@Path("/organizations")
public class OrganizationsResource extends AbstractResource{

    static final UriBuilder ORGANIZATION_URI_BUILDER;
    static final UriBuilder ORGANIZATION_AFTER_ID_BUILDER;
    static final UriBuilder ORGANIZATION_BEFORE_ID_BUILDER;

    static{
        ORGANIZATION_URI_BUILDER = UriBuilder.fromResource(OrganizationResource.class);
        ORGANIZATION_BEFORE_ID_BUILDER = UriBuilder.fromResource(OrganizationResource.class);

        try{
            ORGANIZATION_BEFORE_ID_BUILDER.path(OrganizationResource.class.getMethod("getBefore", String.class));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        ORGANIZATION_AFTER_ID_BUILDER = UriBuilder.fromResource(OrganizationResource.class);
        try{
            ORGANIZATION_AFTER_ID_BUILDER.path(OrganizationResource.class.getMethod("getAfter", String.class));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    @QueryParam("name")
    private String nameLike;
    @QueryParam("author_nick")
    private String authorNickName;
    @QueryParam("count")
    private Integer count;

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/before/{beforeOrganization}")
    public Response getBefore(@PathParam("beforeOrganization") String beforeOrganization) {
        return get(beforeOrganization, true);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/after/{afterOrganization}")
    public Response getAfter(@PathParam("afterOrganization") String afterOrganization) {
      return get(afterOrganization, false);        
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get() {
      return get(null, true);
    }


    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get(String organizationName, boolean isBefore)
    {
        ResponseBuilder responseBuilder = Response.ok();
        Feed atomFeed = Abdera.getNewFactory().newFeed();
        Link organizationsLink = Abdera.getNewFactory().newLink();
        //organizationsLink.setHref(UriBuilder.fromResource());
        organizationsLink.setRel("root");
        atomFeed.addLink(organizationsLink);

        Collection<Organization> organizations = Services.getInstance().getOrganizationService().getAllOrganization();

        if(organizations != null && !organizations.isEmpty()){

            MultivaluedMap<String, String> queryParam = uriInfo.getQueryParameters();
            List<Organization> organizationList = new ArrayList<Organization>(organizations);

            Link nextLink = abderaFactory.newLink();
            nextLink.setRel(Link.REL_PREVIOUS);
            Organization lastOrganization = organizationList.get(0);
            
            final UriBuilder nextUri = ORGANIZATION_AFTER_ID_BUILDER.clone();
            final UriBuilder previousUri = ORGANIZATION_BEFORE_ID_BUILDER.clone();

            for(String key:queryParam.keySet()){
                final Object[] values = queryParam.get(key).toArray();
                nextUri.queryParam(key, values);
                previousUri.queryParam(key, values);
            }
            nextLink.setHref(nextUri.build(lastOrganization.getOrganizationName()).toString());
            atomFeed.addLink(nextLink);
            Link prevLink = abderaFactory.newLink();
            prevLink.setRel(Link.REL_NEXT);
            Organization firstOrganization = organizationList.get(organizations.size() - 1);
            prevLink.setHref(previousUri.build(firstOrganization.getOrganizationName()).toString());
            atomFeed.addLink(prevLink);
            for (Organization organization : organizations) {
              Entry organizationEntry = abderaFactory.newEntry();
              organizationEntry.setId(organization.getId().toString());
              organizationEntry.setTitle(organization.getOrganizationName());
              organizationEntry.setSummary(organization.getOrganizationName());
              organizationEntry.setUpdated(organization.getLastModifiedDate());
              Link organizationLink = abderaFactory.newLink();
              organizationLink.setHref(OrganizationsResource.ORGANIZATION_URI_BUILDER.clone().build(organization.getOrganizationName()).toString());
              organizationLink.setRel(Link.REL_ALTERNATE);
              organizationLink.setMimeType(MediaType.APPLICATION_ATOM_XML);
              organizationEntry.addLink(organizationLink);
              atomFeed.addEntry(organizationEntry);
            }
        }

        return responseBuilder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(Organization organization) {
    ResponseBuilder responseBuilder;
    try {
      Services.getInstance().getOrganizationService().populateAuthor(organization);
      Services.getInstance().getOrganizationService().save(organization);
      responseBuilder = Response.status(Response.Status.CREATED);
      responseBuilder.location(OrganizationResource.ORGANIZATION_URI_BUILDER.clone().build(organization.getOrganizationName()));
    }    
    catch (Exception ex) {
      responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
      ex.printStackTrace();
    }
    return responseBuilder.build();
  }

}
