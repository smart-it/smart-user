/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Role;
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilderException;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author russel
 */
@Path("/roles/name/{roleName}")
public class RoleResource extends AbstractResource {

  
  static final Method ROLE_CONTENT__METHOD;

  static {    
    try {
      ROLE_CONTENT__METHOD = RoleResource.class.getMethod("getRole");
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }
  }
  private Role role;

  public RoleResource(@PathParam("roleName") String roleName) {
    role = Services.getInstance().getRoleService().getRoleByName(roleName);
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    if (role == null) {
      ResponseBuilder responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    Feed roleFeed = getRoleFeed();
    ResponseBuilder responseBuilder = Response.ok(roleFeed);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/content")
  public Response getRole() {
    if (role == null) {
      ResponseBuilder responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    ResponseBuilder responseBuilder = Response.ok(role);
    return responseBuilder.build();
  }

  @PUT
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(Role newRole) {
    if (role == null) {
      ResponseBuilder responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
    try {

      Services.getInstance().getRoleService().update(role);
      responseBuilder = Response.ok(getRoleFeed());
    }
    catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Feed getRoleFeed() throws UriBuilderException, IllegalArgumentException {

    Feed roleFeed = getFeed(role.getDisplayName(), role.getLastModifiedDate());
    roleFeed.setTitle(role.getDisplayName());

    // add a selflink
    roleFeed.addLink(getSelfLink());

    // add a edit link
    Link editLink = getAbderaFactory().newLink();
    editLink.setHref(getUriInfo().getRequestUri().toString());
    editLink.setRel(Link.REL_EDIT);
    editLink.setMimeType(MediaType.APPLICATION_JSON);
    roleFeed.addLink(editLink);

    // add alternate link
    Link altLink = getAbderaFactory().newLink();
    altLink.setHref(getRelativeURIBuilder().path(RoleResource.class).path(ROLE_CONTENT__METHOD).build(role.getName()).toString());
    altLink.setRel(Link.REL_ALTERNATE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);
    roleFeed.addLink(altLink);

    return roleFeed;
  }

  @DELETE
  public Response delete() {
    if (role == null) {
      ResponseBuilder responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    Services.getInstance().getRoleService().delete(role);
    ResponseBuilder responseBuilder = Response.ok();
    return responseBuilder.build();
  }

  @POST
  public Response updatePost(@HeaderParam("Content-type") String contentType, String message) {
    if (role == null) {
      ResponseBuilder responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
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
      Role newRole = getRoleFromContent(message);
      try {
        Role oldRole = Services.getInstance().getRoleService().getRoleByName(role.getName());

        oldRole.setDisplayName(newRole.getDisplayName());
        oldRole.setShortDescription(newRole.getShortDescription());
        oldRole.setLastModifiedDate(new Date());
        Services.getInstance().getRoleService().update(oldRole);
      }
      catch (Exception ex) {
        responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
      }
    }
    return responseBuilder.build();
  }

  @POST
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response postDelete() {
    if(role==null){
      ResponseBuilder responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    ResponseBuilder responseBuilder = Response.ok();
    try {
      Services.getInstance().getRoleService().delete(role);
    }
    catch (Exception ex) {
    }
    return responseBuilder.build();
  }

  private Role getRoleFromContent(String message) {
    Role newRole = new Role();
    Map<String, String> keyValueMap = new HashMap<String, String>();

    String[] keyValuePairs = message.split("&");

    for (int i = 0; i < keyValuePairs.length; i++) {

      String[] keyValuePair = keyValuePairs[i].split("=");
      keyValueMap.put(keyValuePair[0], keyValuePair[1]);
    }
    if (keyValueMap.get("shortDescription") != null) {
      newRole.setShortDescription(keyValueMap.get("shortDescription"));
    }
    if (keyValueMap.get("displayName") != null) {
      newRole.setDisplayName(keyValueMap.get("displayName"));
    }
    return newRole;
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
