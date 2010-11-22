/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

/**
 *
 * @author russel
 */
import com.smartitengineering.user.domain.Address;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.util.rest.atom.server.AbstractResource;
import com.sun.jersey.api.view.Viewable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.commons.lang.StringUtils;

@Path("/orgs")
public class OrganizationsResource extends AbstractResource {

  static final Method ORGANIZATION_AFTER_SHORTNAME_METHOD;
  static final Method ORGANIZATION_BEFORE_SHORTNAME_METHOD;
  @Context
  private HttpServletRequest servletRequest;

  static {
    try {
      ORGANIZATION_BEFORE_SHORTNAME_METHOD = OrganizationsResource.class.getMethod("getBefore", String.class);
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }
    try {
      ORGANIZATION_AFTER_SHORTNAME_METHOD = OrganizationsResource.class.getMethod("getAfter", String.class);
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }
  }
//    @QueryParam("name")
//    private String nameLike;
  @QueryParam("shortname")
  private String uniqueShortName;
  @DefaultValue("10")
  @QueryParam("count")
  private Integer count;

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Path("/before/{beforeShortName}")
  public Response getBefore(@PathParam("beforeShortName") String beforeShortName) {
    return get(beforeShortName, true);
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/before/{beforeShortName}")
  public Response getBeforeHtml(@PathParam("beforeShortName") String beforeShortName) {
    ResponseBuilder responseBuilder = Response.ok();
    if (count == null) {
      count = 10;
    }
    Collection<Organization> organizations = Services.getInstance().getOrganizationService().getOrganizations(
        null, beforeShortName, true, count);

    servletRequest.setAttribute("templateContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationsResource/organizationList.jsp");
    Viewable view = new Viewable("/template/template.jsp", organizations);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/before/{beforeShortName}/frags")
  public Response getBeforeHtmlFrags(@PathParam("beforeShortName") String beforeShortName) {
    ResponseBuilder responseBuilder = Response.ok();
    if (count == null) {
      count = 10;
    }
    Collection<Organization> organizations = Services.getInstance().getOrganizationService().getOrganizations(
        null, beforeShortName, true, count);


    Viewable view = new Viewable("organizationFrags.jsp", organizations);
    responseBuilder.entity(view);

    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Path("/after/{afterShortName}")
  public Response getAfter(@PathParam("afterShortName") String afterShortName) {
    return get(afterShortName, false);
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/after/{afterShortName}")
  public Response getAfterHtml(@PathParam("afterShortName") String afterShortName) {

    ResponseBuilder responseBuilder = Response.ok();
    if (count == null) {
      count = 10;
    }
    Collection<Organization> organizations = Services.getInstance().getOrganizationService().getOrganizations(
        null, afterShortName, false, count);
    servletRequest.setAttribute("templateContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationsResource/organizationList.jsp");
    Viewable view = new Viewable("/template/template.jsp", organizations);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/after/{afterShortName}/frags")
  public Response getAfterHtmlFrags(@PathParam("afterShortName") String afterShortName) {

    ResponseBuilder responseBuilder = Response.ok();
    if (count == null) {
      count = 10;
    }
    Collection<Organization> organizations = Services.getInstance().getOrganizationService().getOrganizations(
        null, afterShortName, false, count);


    Viewable view = new Viewable("organizationFrags.jsp", organizations);
    responseBuilder.entity(view);

    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    return get(null, true);
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getHtml() {
    ResponseBuilder responseBuilder = Response.ok();

    if (count == null) {
      count = 10;
    }

    Collection<Organization> organizations = Services.getInstance().getOrganizationService().getOrganizations(
        uniqueShortName, uniqueShortName, false, count);
    servletRequest.setAttribute("templateHeadContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationsResource/OrganizationHeader.jsp");
    servletRequest.setAttribute("templateContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationsResource/organizationList.jsp");
    Viewable view = new Viewable("/template/template.jsp", organizations);

    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/frags")
  public Response getHtmlFrags() {
    ResponseBuilder responseBuilder = Response.ok();

    if (count == null) {
      count = 10;
    }

    Collection<Organization> organizations = Services.getInstance().getOrganizationService().getOrganizations(
        uniqueShortName, uniqueShortName, false, count);

    Viewable view = new Viewable("organizationFrags.jsp", organizations);

    responseBuilder.entity(view);
    return responseBuilder.build();
  }

//    @GET
//    @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get(String organizationName, boolean isBefore) {
    if (count == null) {
      count = 10;
    }

    ResponseBuilder responseBuilder = Response.ok();

    // create a new atom feed
    Feed atomFeed = Abdera.getNewFactory().newFeed();

    // create a link to parent resource, in this case now it is linked to root resource
    Link parentResourceLink = Abdera.getNewFactory().newLink();
    parentResourceLink.setHref(UriBuilder.fromResource(RootResource.class).build().toString());
    parentResourceLink.setRel("root");
    atomFeed.addLink(parentResourceLink);

    // get the organizations accoring to the query
    //Collection<Organization> organizations = Services.getInstance().getOrganizationService().getAllOrganization();
//        Collection<Organization> organizations = Services.getInstance().getOrganizationService().getOrganizations(
//            organizationName, uniqueShortName, isBefore, count);
    Collection<Organization> organizations = Services.getInstance().getOrganizationService().getOrganizations(
        "", organizationName, isBefore, count);

    // for testing purpose we manually add organization to the list.
//        List<Organization> serviceOrganization = new ArrayList<Organization>();
//        serviceOrganization.add(new Organization("Sitel", "1"));
//        serviceOrganization.add(new Organization("mehmood equity", "2"));
//        Collection<Organization> organizations = serviceOrganization;

    if (organizations != null && !organizations.isEmpty()) {

      MultivaluedMap<String, String> queryParam = getUriInfo().getQueryParameters();
      List<Organization> organizationList = new ArrayList<Organization>(organizations);

      // uri builder for next and previous organizations according to count
      final UriBuilder nextUri = getRelativeURIBuilder().path(OrganizationsResource.class).path(
          ORGANIZATION_AFTER_SHORTNAME_METHOD);
      final UriBuilder previousUri = getRelativeURIBuilder().path(OrganizationsResource.class).path(
          ORGANIZATION_BEFORE_SHORTNAME_METHOD);

      // link to the next organizations based on count
      Link nextLink = getAbderaFactory().newLink();
      nextLink.setRel(Link.REL_NEXT);
      Organization lastOrganization = organizationList.get(organizationList.size() - 1);



      for (String key : queryParam.keySet()) {

        final Object[] values = queryParam.get(key).toArray();
        nextUri.queryParam(key, values);
        previousUri.queryParam(key, values);
      }
      nextLink.setHref(nextUri.build(lastOrganization.getUniqueShortName()).toString());
      //nextLink.setHref(UriBuilder.fromResource(OrganizationsResource.class).build(lastOrganization.getUniqueShortName()).toString());

      atomFeed.addLink(nextLink);

      /* link to the previous organizations based on count */
      Link prevLink = getAbderaFactory().newLink();
      prevLink.setRel(Link.REL_PREVIOUS);
      Organization firstOrganization = organizationList.get(0);

      prevLink.setHref(previousUri.build(firstOrganization.getUniqueShortName()).toString());
      //prevLink.setHref(nameLike)
      atomFeed.addLink(prevLink);

      // add entry of individual organization
      for (Organization organization : organizations) {
        Entry organizationEntry = getAbderaFactory().newEntry();

        organizationEntry.setId(organization.getUniqueShortName().toString());
        organizationEntry.setTitle(organization.getName());
        organizationEntry.setSummary(organization.getName());
        organizationEntry.setUpdated(organization.getLastModifiedDate());

        /* setting link to the individual organization resource*/

        Link organizationLink = getAbderaFactory().newLink();
        organizationLink.setHref(getRelativeURIBuilder().path(OrganizationResource.class).build(organization.
            getUniqueShortName()).toString());
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
      responseBuilder.location(getAbsoluteURIBuilder().path(OrganizationResource.class).build(organization.
          getUniqueShortName()));

    }
    catch (Exception ex) {
      responseBuilder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Organization getObjectFromContent(String message) {

    Map<String, String> keyValueMap = new HashMap<String, String>();

    String[] keyValuePairs = message.split("&");

    for (int i = 0; i < keyValuePairs.length; i++) {

      String[] keyValuePair = keyValuePairs[i].split("=");
      keyValueMap.put(keyValuePair[0], keyValuePair[1]);
    }

    Organization newOrganization = new Organization();

    if (keyValueMap.get("id") != null) {
      newOrganization.setId(Integer.valueOf(keyValueMap.get("id")));
    }

    if (keyValueMap.get("name") != null) {
      newOrganization.setName(keyValueMap.get("name"));
    }
    if (keyValueMap.get("uniqueShortName") != null) {
      newOrganization.setUniqueShortName(keyValueMap.get("uniqueShortName"));
    }



    Address address = new Address();

    if (keyValueMap.get("city") != null) {
      address.setCity(keyValueMap.get("city"));
    }

    if (keyValueMap.get("country") != null) {
      address.setCountry(keyValueMap.get("country"));
    }


    if (keyValueMap.get("state") != null) {
      address.setState(keyValueMap.get("state"));
    }
    if (keyValueMap.get("zip") != null) {
      address.setZip(keyValueMap.get("zip"));
    }

    newOrganization.setAddress(address);

    return newOrganization;
  }

  @POST
  public Response post(@HeaderParam("Content-type") String contentType, String message) {
    ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);

    if (StringUtils.isBlank(message)) {
      responseBuilder = Response.status(Status.BAD_REQUEST);
      responseBuilder.build();
    }

    final boolean isHtmlPost;
    if (StringUtils.isBlank(contentType)) {
      contentType = MediaType.APPLICATION_OCTET_STREAM;
      isHtmlPost = false;
    }
    else if (contentType.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
      contentType = MediaType.APPLICATION_OCTET_STREAM;
      isHtmlPost = true;
      try {
        //Will search for the first '=' if not found will take the whole string
        final int startIndex = 0;//message.indexOf("=") + 1;
        //Consider the first '=' as the start of a value point and take rest as value
        final String realMsg = message.substring(startIndex);
        //Decode the message to ignore the form encodings and make them human readable
        message = URLDecoder.decode(realMsg, "UTF-8");
      }
      catch (UnsupportedEncodingException ex) {
      }
    }
    else {
      isHtmlPost = false;
    }

    if (isHtmlPost) {
      Organization newOrganization = new Organization();
      try {
        newOrganization = getObjectFromContent(message);
        Services.getInstance().getOrganizationService().save(newOrganization);
        responseBuilder.status(Status.SEE_OTHER);
        responseBuilder.location(getAbsoluteURIBuilder().path(OrganizationResource.class).build(newOrganization.
            getUniqueShortName()));
      }
      catch (Exception ex) {
        servletRequest.setAttribute("error", ex.getMessage());
        servletRequest.setAttribute("errorOrganization", newOrganization);
        Collection<Organization> organizations = Services.getInstance().getOrganizationService().getOrganizations(
            uniqueShortName, uniqueShortName, false, count);
        servletRequest.setAttribute("templateHeadContent",
                                    "/com/smartitengineering/user/ws/resources/OrganizationsResource/OrganizationHeader.jsp");
        servletRequest.setAttribute("templateContent",
                                    "/com/smartitengineering/user/ws/resources/OrganizationsResource/organizationList.jsp");
        Viewable view = new Viewable("/template/template.jsp", organizations);
        responseBuilder.entity(view);
      }
    }
    return responseBuilder.build();
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
