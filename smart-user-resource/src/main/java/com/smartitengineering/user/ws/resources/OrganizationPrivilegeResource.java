/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.util.rest.atom.server.AbstractResource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
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

/**
 *
 * @author russel
 */
@Path("/orgs/sn/{organizationUniqueShortName}/privs/name/{privilegeName}")
public class OrganizationPrivilegeResource extends AbstractResource {

  static final Method CONTENT_METHOD;

  static {
    try {
      CONTENT_METHOD = (OrganizationPrivilegeResource.class.getMethod("getContent"));
    }
    catch (Exception ex) {
      throw new InstantiationError();

    }
  }
  private String organizationUniqueShortName;
  private String privilegeName;
  private Organization organization;
  private Privilege privilege;

  public OrganizationPrivilegeResource(@PathParam("organizationUniqueShortName") String organizationUniqueShortName, @PathParam(
      "privilegeName") String privilegeName) {
    this.organizationUniqueShortName = organizationUniqueShortName;
    this.privilegeName = privilegeName;
    organization = getOrganization();
    privilege = getPrivilege();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/content")
  public Response getContent() {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null || privilege == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    responseBuilder = Response.ok(getPrivilege());
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null || privilege == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    try {
      responseBuilder = Response.status(Status.OK);
      Feed privilegeFeed = getPrivilegeFeed();
      responseBuilder = Response.ok(privilegeFeed);
    }
    catch (Exception ex) {

      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  @DELETE
  public Response delete() {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null || privilege == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    try {
      responseBuilder = Response.status(Status.OK);
      Services.getInstance().getPrivilegeService().delete(getPrivilege());
    }
    catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  @PUT
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(Privilege newPrivilege) {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null || privilege == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    try {
      responseBuilder = Response.status(Status.OK);
      newPrivilege.setParentOrganization(organization);
      Services.getInstance().getPrivilegeService().update(newPrivilege);
    }
    catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Feed getPrivilegeFeed() throws UriBuilderException, IllegalArgumentException {

    Feed privilegeFeed = getAbderaFactory().newFeed();

    privilegeFeed.setId(privilegeName);
    privilegeFeed.setTitle(privilegeName);
    privilegeFeed.addLink(getSelfLink());

    Link editLink = getAbderaFactory().newLink();
    editLink.setHref(getUriInfo().getRequestUri().toString());
    editLink.setRel(Link.REL_EDIT);
    editLink.setMimeType(MediaType.APPLICATION_JSON);


    Link altLink = getAbderaFactory().newLink();
    altLink.setHref(getRelativeURIBuilder().path(OrganizationPrivilegeResource.class).path(CONTENT_METHOD).build(
        organizationUniqueShortName, privilegeName).toString());
    altLink.setRel(Link.REL_ALTERNATE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);
    privilegeFeed.addLink(altLink);

    privilegeFeed.addLink(editLink);

    return privilegeFeed;
  }

  @POST
  @Path("/delete")
  public Response deletePost() {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null || privilege == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    try {
      Services.getInstance().getPrivilegeService().delete(getPrivilege());
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
    if (organization == null || privilege == null) {
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
      Privilege newPrivilege = getPrivilegeFromContent(message);
      try {
        Services.getInstance().getPrivilegeService().update(newPrivilege);
        responseBuilder = Response.ok(getPrivilegeFeed());
      }
      catch (Exception ex) {
        responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
      }
    }
    return responseBuilder.build();
  }

  private Privilege getPrivilegeFromContent(String message) {
    Map<String, String> keyValueMap = new HashMap<String, String>();

    String[] keyValuePairs = message.split("&");

    for (int i = 0; i < keyValuePairs.length; i++) {
      String[] keyValuePair = keyValuePairs[i].split("=");
      keyValueMap.put(keyValuePair[0], keyValuePair[1]);
    }

    Privilege newPrivilege = new Privilege();
    if (keyValueMap.get("id") != null) {
      newPrivilege.setId(Integer.valueOf(keyValueMap.get("id")));
    }
    if (keyValueMap.get("name") != null) {
      newPrivilege.setName(keyValueMap.get("name"));
    }
    if (keyValueMap.get("displayName") != null) {
      newPrivilege.setDisplayName(keyValueMap.get("displayName"));
    }
    if (keyValueMap.get("shortDescription") != null) {
      newPrivilege.setShortDescription(keyValueMap.get("shortDescription"));
    }
    if (keyValueMap.get("permissionMask") != null) {
      newPrivilege.setPermissionMask(Integer.valueOf(keyValueMap.get("permissionMask")));
    }

    if (keyValueMap.get("orgName") != null) {
      Organization parentOrganization = Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(keyValueMap.
          get("orgName"));
      newPrivilege.setParentOrganization(parentOrganization);

    }
    if (keyValueMap.get("securedObjectID") != null) {
      SecuredObject securedObject = Services.getInstance().getSecuredObjectService().getByOrganizationAndObjectID(
          organizationUniqueShortName, keyValueMap.get("securedObjectID"));
      newPrivilege.setSecuredObject(securedObject);
    }
    return newPrivilege;
  }

  private Privilege getPrivilege() {
    return Services.getInstance().getPrivilegeService().getPrivilegeByOrganizationAndPrivilegeName(
        organizationUniqueShortName, privilegeName);
  }

  private Organization getOrganization() {
    return Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(organizationUniqueShortName);
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
