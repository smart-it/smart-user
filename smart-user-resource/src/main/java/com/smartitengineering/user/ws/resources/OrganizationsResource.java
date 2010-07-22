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
import javax.ws.rs.core.Response.Status;
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
    static final UriBuilder ORGANIZATION_AFTER_SHORTNAME_URI_BUILDER;
    static final UriBuilder ORGANIZATION_BEFORE_SHORTNAME_URI_BUILDER;

    static{
        ORGANIZATION_URI_BUILDER = UriBuilder.fromResource(OrganizationsResource.class);
        ORGANIZATION_BEFORE_SHORTNAME_URI_BUILDER = UriBuilder.fromResource(OrganizationsResource.class);

        try{
            ORGANIZATION_BEFORE_SHORTNAME_URI_BUILDER.path(OrganizationsResource.class.getMethod("getBefore", String.class));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        ORGANIZATION_AFTER_SHORTNAME_URI_BUILDER = UriBuilder.fromResource(OrganizationsResource.class);
        try{
            ORGANIZATION_AFTER_SHORTNAME_URI_BUILDER.path(OrganizationsResource.class.getMethod("getAfter", String.class));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    @QueryParam("name")
    private String nameLike;
//    @QueryParam("author_nick")
//    private String authorNickName;
    @QueryParam("count")
    private Integer count;

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/before/{beforeShortName}")
    public Response getBefore(@PathParam("beforeShortName") String beforeShortName) {
        return get(beforeShortName, true);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/after/{afterShortName}")
    public Response getAfter(@PathParam("afterShortName") String afterShortName) {
      return get(afterShortName, false);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get() {
      return get(null, true);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml(){
        ResponseBuilder responseBuilder = Response.ok();
       Collection<Organization> organizations = Services.getInstance().getOrganizationService().getAllOrganization();
        //Viewable view = new Viewable("organizationList.jsp", organizations);
        //responseBuilder.entity("organizations",organizations,"organizationList.jsp");
        return responseBuilder.build();
    }


//    @GET
//    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get(String organizationName, boolean isBefore)
    {
        ResponseBuilder responseBuilder = Response.ok();

        // create a new atom feed
        Feed atomFeed = Abdera.getNewFactory().newFeed();

        // create a link to parent resource, in this case now it is linked to root resource
        Link parentResourceLink = Abdera.getNewFactory().newLink();
        parentResourceLink.setHref(UriBuilder.fromResource(RootResource.class).build().toString());
        parentResourceLink.setRel("root");
        atomFeed.addLink(parentResourceLink);

        // get the organizations accoring to the query
        Collection<Organization> organizations = Services.getInstance().getOrganizationService().getAllOrganization();

        // for testing purpose we manually add organization to the list.
//        List<Organization> serviceOrganization = new ArrayList<Organization>();
//        serviceOrganization.add(new Organization("Sitel", "1"));
//        serviceOrganization.add(new Organization("mehmood equity", "2"));
//        Collection<Organization> organizations = serviceOrganization;

        if(organizations != null && !organizations.isEmpty()){

            MultivaluedMap<String, String> queryParam = uriInfo.getQueryParameters();
            List<Organization> organizationList = new ArrayList<Organization>(organizations);

            // uri builder for next and previous organizations according to count
            final UriBuilder nextUri = ORGANIZATION_AFTER_SHORTNAME_URI_BUILDER.clone();
            final UriBuilder previousUri = ORGANIZATION_BEFORE_SHORTNAME_URI_BUILDER.clone();

            // link to the next organizations based on count
            Link nextLink = abderaFactory.newLink();
            nextLink.setRel(Link.REL_NEXT);
            Organization lastOrganization = organizationList.get(0);
            
            
            for(String key:queryParam.keySet()){
                final Object[] values = queryParam.get(key).toArray();
                nextUri.queryParam(key, values);
                previousUri.queryParam(key, values);
            }
            nextLink.setHref(nextUri.build(lastOrganization.getUniqueShortName()).toString());
            //nextLink.setHref(UriBuilder.fromResource(OrganizationsResource.class).build(lastOrganization.getUniqueShortName()).toString());

            atomFeed.addLink(nextLink);

            /* link to the previous organizations based on count */
            Link prevLink = abderaFactory.newLink();
            prevLink.setRel(Link.REL_PREVIOUS);
            Organization firstOrganization = organizationList.get(organizations.size() - 1);
            
            prevLink.setHref(previousUri.build(firstOrganization.getUniqueShortName()).toString());
            //prevLink.setHref(nameLike)
            atomFeed.addLink(prevLink);
            
            // add entry of individual organization
            for (Organization organization : organizations) {
              Entry organizationEntry = abderaFactory.newEntry();
              
              organizationEntry.setId(organization.getUniqueShortName().toString());
              organizationEntry.setTitle(organization.getName());
              organizationEntry.setSummary(organization.getName());
              organizationEntry.setUpdated(organization.getLastModifiedDate());

              /* setting link to the individual organization resource*/
              
              Link organizationLink = abderaFactory.newLink();              
              organizationLink.setHref(OrganizationResource.ORGANIZATION_URI_BUILDER.clone().build(organization.getUniqueShortName()).toString());
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
    public Response post(Organization organization) {
    ResponseBuilder responseBuilder;
    try {
      //Services.getInstance().getOrganizationService().populateAuthor(organization);
      Services.getInstance().getOrganizationService().save(organization);
      responseBuilder = Response.status(Response.Status.CREATED);
      responseBuilder.location(OrganizationResource.ORGANIZATION_URI_BUILDER.clone().build(organization.getName()));
      
    }    
    catch (Exception ex) {
      responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
      ex.printStackTrace();
    }
    return responseBuilder.build();
  }
}
