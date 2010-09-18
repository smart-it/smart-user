/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.UserGroup;
import com.sun.jersey.api.view.Viewable;
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
@Path("/orgs/sn/{uniqueShortName}/usergroups/name/{name}")
public class OrganizationUserGroupResource extends AbstractResource {

  
  static final UriBuilder ORGANIZATION_USER_GROUP_URI_BUILDER = UriBuilder.fromResource(OrganizationUserGroupResource.class);
  static final UriBuilder ORGANIZATION_USER_GROUP_CONTENT_URI_BUILDER;

  static {
    ORGANIZATION_USER_GROUP_CONTENT_URI_BUILDER = ORGANIZATION_USER_GROUP_URI_BUILDER.clone();
    try {
      ORGANIZATION_USER_GROUP_CONTENT_URI_BUILDER.path(OrganizationUserGroupResource.class.getMethod("getUserGroup"));
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new InstantiationError();
    }
  }

  @PathParam("uniqueShortName")
  private String orgShortName;
  @PathParam("name")
  private String name;
  public OrganizationUserGroupResource() {    
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    Feed userFeed = getUserGroupFeed();
    ResponseBuilder responseBuilder = Response.ok(userFeed);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/content")
  public Response getContent() {
    ResponseBuilder responseBuilder = Response.ok(getUserGroup());
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getHtml() {
    ResponseBuilder responseBuilder = Response.ok();

    Viewable view = new Viewable("OrganizationUserDetails", getUserGroup(), OrganizationResource.class);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @PUT
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(UserGroup newUserGroup) {
    ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
    try {      
      newUserGroup.setOrganization(getOrganization());
      Services.getInstance().getUserGroupService().update(newUserGroup);
      responseBuilder = Response.ok(getUserGroupFeed());
    } catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
      ex.printStackTrace();
    }
    return responseBuilder.build();
  }

  private Feed getUserGroupFeed() throws UriBuilderException, IllegalArgumentException {
    UserGroup userGroup = getUserGroup();
    Feed userFeed = getFeed(userGroup.getName(), new Date());
    userFeed.setTitle(userGroup.getName());

    // add a self link
    userFeed.addLink(getSelfLink());

    // add a edit link
    Link editLink = abderaFactory.newLink();
    editLink.setHref(uriInfo.getRequestUri().toString());
    editLink.setRel(Link.REL_EDIT);
    editLink.setMimeType(MediaType.APPLICATION_JSON);

    // add a alternate link
    Link altLink = abderaFactory.newLink();
    altLink.setHref(ORGANIZATION_USER_GROUP_CONTENT_URI_BUILDER.clone().build(userGroup.getName()).toString());
    altLink.setRel(Link.REL_ALTERNATE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);
    userFeed.addLink(altLink);

    return userFeed;
  }

  @DELETE
  public Response delete() {
    Services.getInstance().getUserGroupService().delete(getUserGroup());
    ResponseBuilder responseBuilder = Response.ok();
    return responseBuilder.build();
  }

  @POST
  @Path("/delete")
  public Response deletePost() {
    ResponseBuilder responseBuilder = Response.ok();
    try {
      Services.getInstance().getUserGroupService().delete(getUserGroup());
    } catch (Exception ex) {
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
    } else if (contentType.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
      contentType = MediaType.APPLICATION_OCTET_STREAM;
      isHtmlPost = true;
      try {
        //Will search for the first '=' if not found will take the whole string
        final int startIndex = 0;//message.indexOf("=") + 1;
        //Consider the first '=' as the start of a value point and take rest as value
        final String realMsg = message.substring(startIndex);
        //Decode the message to ignore the form encodings and make them human readable
        message = URLDecoder.decode(realMsg, "UTF-8");
      } catch (UnsupportedEncodingException ex) {
        ex.printStackTrace();
      }
    } else {
      contentType = contentType;
      isHtmlPost = false;
    }

    if (isHtmlPost) {
      UserGroup newUserGroup = getUserGroupFromContent(message);
      try {
        newUserGroup.setOrganization(getOrganization());
        Services.getInstance().getUserGroupService().update(newUserGroup);
        responseBuilder = Response.ok(getUserGroupFeed());
      } catch (Exception ex) {
        responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
      }
    }
    return responseBuilder.build();
  }

  private UserGroup getUserGroupFromContent(String message) {
    Map<String, String> keyValueMap = new HashMap<String, String>();

    String[] keyValuePairs = message.split("&");

    for (int i = 0; i < keyValuePairs.length; i++) {
      String[] keyValuePair = keyValuePairs[i].split("=");
      keyValueMap.put(keyValuePair[0], keyValuePair[1]);
    }

    UserGroup newUserGroup = new UserGroup();
    if (keyValueMap.get("id")!=null) {
      newUserGroup.setId(Integer.valueOf(keyValueMap.get("id")));
    }
    if (keyValueMap.get("name")!=null) {
      newUserGroup.setName(keyValueMap.get("name"));
    }    
    return newUserGroup;
  }

  private UserGroup getUserGroup() {
    return Services.getInstance().getUserGroupService().getByOrganizationAndUserGroupName(orgShortName, name);
  }

  private Organization getOrganization() {
    return Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(orgShortName);
  }
}
