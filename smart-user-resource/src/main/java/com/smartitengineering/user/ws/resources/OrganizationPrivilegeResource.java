/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.SecuredObject;
import java.io.UnsupportedEncodingException;
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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author russel
 */
@Path("/orgs/{organizationUniqueShortName}/privs/{privilegeName}")
public class OrganizationPrivilegeResource extends AbstractResource {

  private Privilege privilege;
  private String organizationUniqueShortName;

  public OrganizationPrivilegeResource(@PathParam("organizationUniqueShortName") String organizationUniqueShortName, @PathParam("privilegeName") String privilegeName) {

    this.organizationUniqueShortName = organizationUniqueShortName;
    privilege = Services.getInstance().getPrivilegeService().getPrivilegeByOrganizationAndPrivilegeName(organizationUniqueShortName, privilegeName);
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    ResponseBuilder responseBuilder;
    try {
      responseBuilder = Response.status(Status.OK);
      Feed privilegeFeed = getPrivilegeFeed();
      responseBuilder = Response.ok(privilegeFeed);
    } catch (Exception ex) {
      ex.printStackTrace();
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  @DELETE
  public Response delete() {
    ResponseBuilder responseBuilder;
    try {
      responseBuilder = Response.status(Status.OK);
      Services.getInstance().getPrivilegeService().delete(privilege);
    } catch (Exception ex) {
      ex.printStackTrace();
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  @PUT
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(Privilege newPrivilege) {
    ResponseBuilder responseBuilder;
    try {
      responseBuilder = Response.status(Status.OK);
      if (privilege.getParentOrganizationID() == null) {
        throw new Exception("No parent Organization");
      }
      Services.getInstance().getOrganizationService().populateOrganization(privilege);
      if (privilege.getSecuredObjectID() != null) {
        Services.getInstance().getSecuredObjectService().populateSecuredObject(privilege);
      }
      Services.getInstance().getPrivilegeService().delete(newPrivilege);
    } catch (Exception ex) {
      ex.printStackTrace();
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Feed getPrivilegeFeed() throws UriBuilderException, IllegalArgumentException {

    Feed privilegeFeed = abderaFactory.newFeed();

    privilegeFeed.setId(privilege.getName());
    privilegeFeed.setTitle(privilege.getName());
    privilegeFeed.addLink(getSelfLink());

    Link editLink = abderaFactory.newLink();
    editLink.setHref(uriInfo.getRequestUri().toString());
    editLink.setRel(Link.REL_EDIT);
    editLink.setMimeType(MediaType.APPLICATION_JSON);


    Link altLink = abderaFactory.newLink();
    altLink.setHref(UriBuilder.fromResource(OrganizationPrivilegeResource.class).build(organizationUniqueShortName, privilege.getName()).toString());
    altLink.setRel(Link.REL_ALTERNATE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);

    privilegeFeed.addLink(editLink);
    privilegeFeed.addLink(altLink);

    return privilegeFeed;
  }

  @POST
  @Path("/delete")
  public Response deletePost() {
    ResponseBuilder responseBuilder = Response.ok();
    try {
      Services.getInstance().getPrivilegeService().delete(privilege);
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
      Privilege newPrivilege = getPrivilegeFromContent(message);
      try {
        Services.getInstance().getPrivilegeService().update(newPrivilege);
        responseBuilder = Response.ok(getPrivilegeFeed());
      } catch (Exception ex) {
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
    if (keyValueMap.get("id")!=null) {
      newPrivilege.setId(Integer.valueOf(keyValueMap.get("id")));
    }
    if (keyValueMap.get("name")!=null) {
      newPrivilege.setName(keyValueMap.get("name"));
    }
    if (keyValueMap.get("displayName")!=null) {
      newPrivilege.setDisplayName(keyValueMap.get("displayName"));
    }
    if (keyValueMap.get("shortDescription")!=null) {
      newPrivilege.setShortDescription(keyValueMap.get("shortDescription"));
    }
    if (keyValueMap.get("permissionMask")!=null) {
      newPrivilege.setPermissionMask(Integer.valueOf(keyValueMap.get("permissionMask")));
    }

    if (keyValueMap.get("orgName")!=null) {
      Organization parentOrganization = Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(keyValueMap.get("orgName"));
      newPrivilege.setParentOrganization(parentOrganization);
      if (keyValueMap.get("securedObjectID")!=null) {
        SecuredObject securedObject = Services.getInstance().getSecuredObjectService().getByOrganizationAndObjectID(parentOrganization.getUniqueShortName(), keyValueMap.get("securedObjectID"));
        newPrivilege.setSecuredObject(securedObject);
      }
    }
    return newPrivilege;
  }
}
