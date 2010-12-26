/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.util.rest.atom.server.AbstractResource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilderException;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author russel
 */
@Path("/orgs/sn/{organizationUniqueShortName}/so/name/{name}")
public class OrganizationSecuredObjectResource extends AbstractResource {

  private String name;
  private String organizationUniqueName;
  private Organization organization;
  private SecuredObject securedObject;
  static final Method ORGANIZATION_SECURED_OBJECT_CONTENT_METHOD;

  static {
    try {
      ORGANIZATION_SECURED_OBJECT_CONTENT_METHOD = OrganizationSecuredObjectResource.class.getMethod("getContent");
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }
  }

  public OrganizationSecuredObjectResource(@PathParam("organizationUniqueShortName") String organizationUniqueShortName, @PathParam(
      "name") String name) {
    this.organizationUniqueName = organizationUniqueShortName;
    this.name = name;
    organization = getOrganization();
    securedObject = getSecuredObject();
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null || securedObject == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    Feed securedObjectFeed = getSecuredObjectFeed();
    responseBuilder = Response.ok(securedObjectFeed);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/content")
  public Response getContent() {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null || securedObject == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    responseBuilder = Response.ok(securedObject);
    return responseBuilder.build();
  }

  @PUT
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(SecuredObject newSecuredObject) {
    ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
    if (organization == null || securedObject == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    try {
      newSecuredObject.setOrganization(organization);
      Services.getInstance().getSecuredObjectService().save(newSecuredObject);
      responseBuilder = Response.ok(getSecuredObjectFeed());
    }
    catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  @DELETE
  public Response delete() {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null || securedObject == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    try {
      Services.getInstance().getSecuredObjectService().delete(securedObject);
    }
    catch (Exception ex) {
      responseBuilder = Response.ok(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Feed getSecuredObjectFeed() throws UriBuilderException, IllegalArgumentException {

    Feed securedObjectFeed = getFeed(name, new Date());
    securedObjectFeed.setTitle(name);

    // add a self link
    securedObjectFeed.addLink(getSelfLink());

    // add a edit link
    Link editLink = getAbderaFactory().newLink();
    editLink.setHref(getUriInfo().getRequestUri().toString());
    editLink.setRel(Link.REL_EDIT);
    editLink.setMimeType(MediaType.APPLICATION_JSON);

    // add a alternate link
    Link altLink = getAbderaFactory().newLink();
    altLink.setHref(getRelativeURIBuilder().path(OrganizationSecuredObjectResource.class).path(
        ORGANIZATION_SECURED_OBJECT_CONTENT_METHOD).build(organizationUniqueName, name).toString());
    altLink.setRel(Link.REL_ALTERNATE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);
    securedObjectFeed.addLink(altLink);

    return securedObjectFeed;
  }

  @POST
  @Path("/delete")
  public Response deletePost() {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null || securedObject == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    try {
      Services.getInstance().getSecuredObjectService().delete(securedObject);
    }
    catch (Exception ex) {
      responseBuilder = Response.ok(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  @POST
  @Path("/update")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updatePost(@HeaderParam("Content-type") String contentType, String message) {
    ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
    if (organization == null || securedObject == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
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
      SecuredObject newSecuredObject = getSecuredObjectFromContent(message);
      newSecuredObject.setOrganization(organization);
      try {
        Services.getInstance().getSecuredObjectService().update(newSecuredObject);
        responseBuilder = Response.ok(getSecuredObjectFeed());
      }
      catch (Exception ex) {
        responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
      }
    }
    return responseBuilder.build();
  }

  private SecuredObject getSecuredObjectFromContent(String message) {
    Map<String, String> keyValueMap = new HashMap<String, String>();

    String[] keyValuePairs = message.split("&");

    for (int i = 0; i < keyValuePairs.length; i++) {
      String[] keyValuePair = keyValuePairs[i].split("=");
      keyValueMap.put(keyValuePair[0], keyValuePair[1]);
    }

    SecuredObject newSecuredObject = new SecuredObject();
    if (keyValueMap.get("id") != null) {
      newSecuredObject.setId(NumberUtils.toLong(keyValueMap.get("id")));
    }
    if (keyValueMap.get("name") != null) {
      newSecuredObject.setName(keyValueMap.get("name"));
    }
    if (keyValueMap.get("objectID") != null) {
      newSecuredObject.setObjectID(keyValueMap.get("objectID"));
    }
    if (keyValueMap.get("parentObjectID") != null) {
      newSecuredObject.setParentObjectID(keyValueMap.get("parentObjectID"));
    }
    return newSecuredObject;
  }

  private Organization getOrganization() {
    return Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(organizationUniqueName);
  }

  private SecuredObject getSecuredObject() {
    return Services.getInstance().getSecuredObjectService().getByOrganizationAndName(organizationUniqueName, name);
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
