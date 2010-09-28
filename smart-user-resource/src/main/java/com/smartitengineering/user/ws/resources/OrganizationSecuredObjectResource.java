/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.SecuredObject;
import java.io.UnsupportedEncodingException;
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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author russel
 */
@Path("/orgs/sn/{organizationUniqueShortName}/so/{oid}")
public class OrganizationSecuredObjectResource extends AbstractResource {

  private String oid;
  private String organizationUniqueName;
  static final UriBuilder ORGANIZATION_SECURED_OBJECT_URI_BUILDER = UriBuilder.fromResource(
      OrganizationSecuredObjectResource.class);
  static final UriBuilder ORGANIZATION_SECURED_OBJECT_CONTENT_URI_BUILDER;

  static {
    ORGANIZATION_SECURED_OBJECT_CONTENT_URI_BUILDER = ORGANIZATION_SECURED_OBJECT_URI_BUILDER.clone();
    try {
      ORGANIZATION_SECURED_OBJECT_CONTENT_URI_BUILDER.path(OrganizationSecuredObjectResource.class.getMethod(
          "getSecuredObject"));
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new InstantiationError();
    }
  }

  public OrganizationSecuredObjectResource(@PathParam("organizationUniqueShortName") String organizationUniqueShortName, @PathParam(
      "oid") String oid) {
    this.organizationUniqueName = organizationUniqueShortName;
    this.oid = oid;

  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    Feed securedObjectFeed = getSecuredObjectFeed();
    ResponseBuilder responseBuilder = Response.ok(securedObjectFeed);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/content")
  public Response getContent() {
    ResponseBuilder responseBuilder = Response.ok(getSecuredObject());
    return responseBuilder.build();
  }

  @PUT
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(SecuredObject newSecuredObject) {
    ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
    try {      
      newSecuredObject.setOrganization(getOrganization());
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
    try {
      Services.getInstance().getSecuredObjectService().delete(getSecuredObject());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      responseBuilder = Response.ok(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Feed getSecuredObjectFeed() throws UriBuilderException, IllegalArgumentException {

    Feed securedObjectFeed = getFeed(oid, new Date());
    securedObjectFeed.setTitle(oid);

    // add a self link
    securedObjectFeed.addLink(getSelfLink());

    // add a edit link
    Link editLink = abderaFactory.newLink();
    editLink.setHref(uriInfo.getRequestUri().toString());
    editLink.setRel(Link.REL_EDIT);
    editLink.setMimeType(MediaType.APPLICATION_JSON);

    // add a alternate link
    Link altLink = abderaFactory.newLink();
    altLink.setHref(
        ORGANIZATION_SECURED_OBJECT_CONTENT_URI_BUILDER.clone().build(organizationUniqueName, oid).toString());
    altLink.setRel(Link.REL_ALTERNATE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);
    securedObjectFeed.addLink(altLink);

    return securedObjectFeed;
  }

  @POST
  @Path("/delete")
  public Response deletePost() {
    ResponseBuilder responseBuilder = Response.ok();
    try {
      Services.getInstance().getSecuredObjectService().delete(getSecuredObject());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      responseBuilder = Response.ok(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  @POST
  @Path("/update")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updatePost(@HeaderParam("Content-type") String contentType, String message) {
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
        ex.printStackTrace();
      }
    }
    else {
      contentType = contentType;
      isHtmlPost = false;
    }

    if (isHtmlPost) {
      SecuredObject newSecuredObject = getSecuredObjectFromContent(message);
      newSecuredObject.setOrganization(getOrganization());
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
      newSecuredObject.setId(Integer.valueOf(keyValueMap.get("id")));
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
    return Services.getInstance().getSecuredObjectService().getByOrganizationAndObjectID(organizationUniqueName, oid);
  }
}
