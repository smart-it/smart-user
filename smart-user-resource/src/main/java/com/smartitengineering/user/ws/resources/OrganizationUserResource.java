/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Address;
import com.smartitengineering.user.domain.BasicIdentity;
import com.smartitengineering.user.domain.GeoLocation;
import com.smartitengineering.user.domain.Name;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.sun.jersey.api.view.Viewable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;

import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
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
@Path("/orgs/{organizationShortName}/users/username/{userName}")
public class OrganizationUserResource extends AbstractResource {

  private User user;
  static final UriBuilder USER_URI_BUILDER = UriBuilder.fromResource(OrganizationUserResource.class);
  static final UriBuilder USER_CONTENT_URI_BUILDER;

  @Context
  private HttpServletRequest servletRequest;

  static {
    USER_CONTENT_URI_BUILDER = USER_URI_BUILDER.clone();
    try {
      USER_CONTENT_URI_BUILDER.path(OrganizationUserResource.class.getMethod("getUser"));
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new InstantiationError();

    }
  }

  public OrganizationUserResource(@PathParam("organizationShortName") String organizationShortName, @PathParam("userName") String userName) {
    //userPerson = Services.getInstance().getUserPersonService().getUserPersonByUsernameAndOrgName(userName, organizationShortName);
    user = Services.getInstance().getUserService().getUserByOrganizationAndUserName(organizationShortName, userName);
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    Feed userFeed = getUserFeed();
    ResponseBuilder responseBuilder = Response.ok(userFeed);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/content")
  public Response getUser() {
    ResponseBuilder responseBuilder = Response.ok(user);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getHtml() {
    ResponseBuilder responseBuilder = Response.ok();

    servletRequest.setAttribute("templateContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationUserResource/OrganizationUserDetails.jsp");
    Viewable view = new Viewable("/template/template.jsp", user);
    
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @PUT
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(User newUser) {

    ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
    try {
      if (newUser.getRoleIDs() != null) {
        Services.getInstance().getRoleService().populateRole(newUser);
      }
      if (newUser.getPrivilegeIDs() != null) {
        Services.getInstance().getPrivilegeService().populatePrivilege(newUser);
      }
      if (newUser.getParentOrganizationID() == null) {
        throw new Exception("No organization found");
      }
      newUser = Services.getInstance().getUserService().getUserByUsername(newUser.getUsername());
      
      Services.getInstance().getOrganizationService().populateOrganization(newUser);
      
      Services.getInstance().getUserService().update(user);

      responseBuilder = Response.ok(getUserFeed());
    } catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
      ex.printStackTrace();
    }
    return responseBuilder.build();
  }

  private Feed getUserFeed() throws UriBuilderException, IllegalArgumentException {
    Feed userFeed = getFeed(user.getUsername(), new Date());
    userFeed.setTitle(user.getUsername());

    // add a self link
    userFeed.addLink(getSelfLink());

    // add a edit link
    Link editLink = abderaFactory.newLink();
    editLink.setHref(uriInfo.getRequestUri().toString());
    editLink.setRel(Link.REL_EDIT);
    editLink.setMimeType(MediaType.APPLICATION_JSON);
    userFeed.addLink(editLink);

    // add a alternate link
    Link altLink = abderaFactory.newLink();
    altLink.setHref(USER_CONTENT_URI_BUILDER.clone().build(user.getOrganization().getUniqueShortName(), user.getUsername()).toString());
    altLink.setRel(Link.REL_ALTERNATE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);
    userFeed.addLink(altLink);

    return userFeed;
  }

  @DELETE
  public Response delete() {
    Services.getInstance().getUserService().delete(user);
    ResponseBuilder responseBuilder = Response.ok();
    return responseBuilder.build();
  }

  @POST
  @Path("/delete")
  public Response deletePost() {
    Services.getInstance().getUserService().delete(user);
    ResponseBuilder responseBuilder = Response.ok();
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
      User newUser = getUserFromContent(message);
      try {
        Services.getInstance().getUserService().update(newUser);
        responseBuilder = Response.ok(getUserFeed());
      } catch (Exception ex) {
        responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
      }
    }
    return responseBuilder.build();
  }

  private User getUserFromContent(String message) {

    Map<String, String> keyValueMap = new HashMap<String, String>();

    String[] keyValuePairs = message.split("&");

    for (int i = 0; i < keyValuePairs.length; i++) {

      String[] keyValuePair = keyValuePairs[i].split("=");
      keyValueMap.put(keyValuePair[0], keyValuePair[1]);
    }

    User newUser = new User();

    if (keyValueMap.get("id") != null) {
      newUser.setId(Integer.valueOf(keyValueMap.get("id")));
    }

    if (keyValueMap.get("version") != null) {
      newUser.setVersion(Integer.valueOf(keyValueMap.get("version")));

    }

    if (keyValueMap.get("userName") != null) {
      newUser.setUsername(keyValueMap.get("userName"));
    }
    if (keyValueMap.get("password") != null) {
      newUser.setPassword(keyValueMap.get("password"));
    }

    if (keyValueMap.get("uniqueShortName") != null) {

      Organization parentOrg = Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(keyValueMap.
          get("uniqueShortName"));

      if (parentOrg != null) {
        newUser.setOrganization(parentOrg);
      }
    }
    return user;
  }
}
