/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.SecuredObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
@Path("/organizations/{organizationUniqueShortName}/securedObjects")
public class OrganizationSecuredObjectsResource extends AbstractResource{

    static final UriBuilder ORGANIZATION_SECURED_OBJECTS_URI_BUILDER;
    static final UriBuilder ORGANIZATION_SECURED_OBJECTS_BEFORE_OBJECTID_URI_BUILDER;
    static final UriBuilder ORGANIZATION_SECURED_OBJECTS_AFTER_OBJECTID_URI_BUILDER;

    static{
        ORGANIZATION_SECURED_OBJECTS_URI_BUILDER = UriBuilder.fromResource(OrganizationSecuredObjectsResource.class);

        ORGANIZATION_SECURED_OBJECTS_AFTER_OBJECTID_URI_BUILDER = UriBuilder.fromResource(OrganizationSecuredObjectsResource.class);
        try{
            ORGANIZATION_SECURED_OBJECTS_AFTER_OBJECTID_URI_BUILDER.path(OrganizationSecuredObjectsResource.class.getMethod("getAfter", String.class));
        }catch(Exception ex){
            ex.printStackTrace();
        }

        ORGANIZATION_SECURED_OBJECTS_BEFORE_OBJECTID_URI_BUILDER = UriBuilder.fromResource(OrganizationSecuredObjectsResource.class);
        try{
            ORGANIZATION_SECURED_OBJECTS_BEFORE_OBJECTID_URI_BUILDER.path(OrganizationSecuredObjectsResource.class.getMethod("getBefore", String.class));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @PathParam("count")
    private Integer count;
    @PathParam("uniqueShortName")
    private String organizationUniqueShortName;

    public OrganizationSecuredObjectsResource(@PathParam("organizationUniqueShortName")String organizationUniqueShortName){
        this.organizationUniqueShortName = organizationUniqueShortName;
    }





    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/before/{beforeObjectID}")
    public Response getBefore(@PathParam("beforeObjectID") String beforeObjectID) {
        return get(organizationUniqueShortName, beforeObjectID, true);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/after/{afterObjectID}")
    public Response getAfter(@PathParam("afterObjectID") String afterObjectID) {
      return get(organizationUniqueShortName,afterObjectID, false);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get() {
      return get(organizationUniqueShortName, null, true);
    }

    private Response get(String uniqueOrganizationName, String userName, boolean isBefore){

        if(count == null){
            count = 10;
        }
        ResponseBuilder responseBuilder = Response.ok();
        Feed atomFeed = getFeed(userName, new Date());

        Link parentLink = abderaFactory.newLink();
        parentLink.setHref(UriBuilder.fromResource(OrganizationResource.class).build().toString());
        parentLink.setRel("parent");
        atomFeed.addLink(parentLink);


        Collection<SecuredObject> securedObjects = Services.getInstance().getSecuredObjectService().getByOrganization(uniqueOrganizationName);


        if(securedObjects != null && !securedObjects.isEmpty()){

            MultivaluedMap<String, String> queryParam = uriInfo.getQueryParameters();
            List<SecuredObject> securedObjectList = new ArrayList<SecuredObject>(securedObjects);

            // uri builder for next and previous organizations according to count
            final UriBuilder nextUri = ORGANIZATION_SECURED_OBJECTS_AFTER_OBJECTID_URI_BUILDER.clone();
            final UriBuilder previousUri = ORGANIZATION_SECURED_OBJECTS_BEFORE_OBJECTID_URI_BUILDER.clone();

            // link to the next organizations based on count
            Link nextLink = abderaFactory.newLink();
            nextLink.setRel(Link.REL_NEXT);
            SecuredObject lastSecuredObject = securedObjectList.get(0);


            for(String key:queryParam.keySet()){
                final Object[] values = queryParam.get(key).toArray();
                nextUri.queryParam(key, values);
                previousUri.queryParam(key, values);
            }
            nextLink.setHref(nextUri.build(lastSecuredObject.getObjectID()).toString());


            atomFeed.addLink(nextLink);

            /* link to the previous organizations based on count */
            Link prevLink = abderaFactory.newLink();
            prevLink.setRel(Link.REL_PREVIOUS);
            SecuredObject firstSecuredObject = securedObjectList.get(securedObjects.size() - 1);

            prevLink.setHref(previousUri.build(firstSecuredObject.getObjectID()).toString());
            atomFeed.addLink(prevLink);

            for(SecuredObject securedObject: securedObjects){

                Entry userEntry = abderaFactory.newEntry();

                userEntry.setId(securedObject.getObjectID());
                userEntry.setTitle(securedObject.getObjectID());
                userEntry.setSummary(securedObject.getObjectID());
                userEntry.setUpdated("Not available");

                // setting link to the each individual user
                Link userLink = abderaFactory.newLink();
                userLink.setHref(OrganizationSecuredObjectResource.ORGANIZATION_SECURED_OBJECT_URI_BUILDER.clone().build(securedObject.getObjectID()).toString());
                userLink.setRel(Link.REL_ALTERNATE);
                userLink.setMimeType(MediaType.APPLICATION_ATOM_XML);

                userEntry.addLink(userLink);

                atomFeed.addEntry(userEntry);
            }
        }
        responseBuilder.entity(atomFeed);
        return responseBuilder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(SecuredObject securedObject){

        ResponseBuilder responseBuilder;
        try{
            if(securedObject.getParentOrganizationID() == null){
                throw new Exception("No parent Organization");
            }
            Services.getInstance().getOrganizationService().populateOrganization(securedObject);
            Services.getInstance().getSecuredObjectService().save(securedObject);
            responseBuilder = Response.status(Status.CREATED);
        }
        catch(Exception ex){
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
            ex.printStackTrace();
        }
        return responseBuilder.build();
    }
}
