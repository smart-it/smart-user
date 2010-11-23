/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.util.rest.atom.server.AbstractResource;
import com.sun.jersey.api.view.Viewable;
import java.lang.reflect.Method;
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
@Path("/orgs/sn/{organizationUniqueShortName}/so")
public class OrganizationSecuredObjectsResource extends AbstractResource {

  static final Method ORGANIZATION_SECURED_OBJECTS_BEFORE_OBJECTID_METHOD;
  static final Method ORGANIZATION_SECURED_OBJECTS_AFTER_OBJECTID_METHOD;

  static {
    try {
      ORGANIZATION_SECURED_OBJECTS_AFTER_OBJECTID_METHOD =
      OrganizationSecuredObjectsResource.class.getMethod("getAfter", String.class);
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }
    try {
      ORGANIZATION_SECURED_OBJECTS_BEFORE_OBJECTID_METHOD = OrganizationSecuredObjectsResource.class.getMethod(
          "getBefore", String.class);
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }
  }
  @PathParam("count")
  private Integer count;
  private String organizationUniqueShortName;
  private Organization organization;

  public OrganizationSecuredObjectsResource(@PathParam("organizationUniqueShortName") String orgName) {
    this.organizationUniqueShortName = orgName;
    organization = getOrganization();
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
    return get(organizationUniqueShortName, afterObjectID, false);
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getHtml() {
    ResponseBuilder responseBuilder = Response.ok();

    Collection<SecuredObject> securedObjects = Services.getInstance().getSecuredObjectService().getByOrganization(
        organizationUniqueShortName);

    Viewable view = new Viewable("orgSecuredObjectList", securedObjects, OrganizationSecuredObjectsResource.class);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    return get(organizationUniqueShortName, null, true);
  }

  private Response get(String uniqueOrganizationName, String userName, boolean isBefore) {

    if (count == null) {
      count = 10;
    }
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    Feed atomFeed = getFeed(userName, new Date());

    Link parentLink = getAbderaFactory().newLink();
    parentLink.setHref(UriBuilder.fromResource(OrganizationResource.class).build(uniqueOrganizationName).toString());
    parentLink.setRel("parent");
    atomFeed.addLink(parentLink);


    Collection<SecuredObject> securedObjects = Services.getInstance().getSecuredObjectService().getByOrganization(
        uniqueOrganizationName);

    if (securedObjects != null && !securedObjects.isEmpty()) {

      MultivaluedMap<String, String> queryParam = getUriInfo().getQueryParameters();
      List<SecuredObject> securedObjectList = new ArrayList<SecuredObject>(securedObjects);

      // uri builder for next and previous organizations according to count
      final UriBuilder nextUri = getRelativeURIBuilder().path(OrganizationSecuredObjectsResource.class).path(
          ORGANIZATION_SECURED_OBJECTS_AFTER_OBJECTID_METHOD);
      final UriBuilder previousUri = getRelativeURIBuilder().path(OrganizationSecuredObjectsResource.class).path(
          ORGANIZATION_SECURED_OBJECTS_BEFORE_OBJECTID_METHOD);

      // link to the next organizations based on count
      Link nextLink = getAbderaFactory().newLink();
      nextLink.setRel(Link.REL_NEXT);
      SecuredObject lastSecuredObject = securedObjectList.get(0);


      for (String key : queryParam.keySet()) {
        final Object[] values = queryParam.get(key).toArray();
        nextUri.queryParam(key, values);
        previousUri.queryParam(key, values);
      }
      nextLink.setHref(nextUri.build(uniqueOrganizationName, lastSecuredObject.getObjectID()).toString());


      atomFeed.addLink(nextLink);

      /* link to the previous organizations based on count */
      Link prevLink = getAbderaFactory().newLink();
      prevLink.setRel(Link.REL_PREVIOUS);
      SecuredObject firstSecuredObject = securedObjectList.get(securedObjects.size() - 1);

      prevLink.setHref(previousUri.build(uniqueOrganizationName, firstSecuredObject.getObjectID()).toString());
      atomFeed.addLink(prevLink);

      for (SecuredObject securedObject : securedObjects) {

        Entry securedObjectEntry = getAbderaFactory().newEntry();

        securedObjectEntry.setId(securedObject.getObjectID());
        securedObjectEntry.setTitle(securedObject.getObjectID());
        securedObjectEntry.setSummary(securedObject.getObjectID());
        //userEntry.setUpdated("Not available");

        // setting link to the each individual user
        Link securedObjectLink = getAbderaFactory().newLink();
        securedObjectLink.setHref(getRelativeURIBuilder().path(OrganizationSecuredObjectResource.class).build(
            uniqueOrganizationName, securedObject.getName()).toString());
        securedObjectLink.setRel(Link.REL_ALTERNATE);
        securedObjectLink.setMimeType(MediaType.APPLICATION_ATOM_XML);

        securedObjectEntry.addLink(securedObjectLink);

        atomFeed.addEntry(securedObjectEntry);
      }
    }
    responseBuilder.entity(atomFeed);
    return responseBuilder.build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response post(SecuredObject securedObject) {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    try {
      securedObject.setOrganization(organization);
      Services.getInstance().getSecuredObjectService().save(securedObject);
      responseBuilder = Response.status(Status.CREATED);
      responseBuilder.location(getAbsoluteURIBuilder().path(OrganizationSecuredObjectResource.class).build(organizationUniqueShortName, securedObject.getName()));
    }
    catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Organization getOrganization() {
    return Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(organizationUniqueShortName);
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
