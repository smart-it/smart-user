/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.User;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author modhu7
 */
@Path("/orgs/sn/{organizationUniqueShortName}/un/{username}/privs/name/{privilegeName}")
public class UserPrivilegeResource extends AbstractResource {

  static final UriBuilder USER_PRIVILEGE_URI_BUILDER = UriBuilder.fromResource(UserPrivilegeResource.class);
  static final UriBuilder PRIVILEGE_URI_BUILDER = OrganizationPrivilegeResource.PRIVILEGE_URI_BUILDER.clone();
  private String organizationUniqueShortName;
  private String username;
  private String privilegeName;
  private Privilege privilege;
  private Organization organization;
  private User user;
  private static String REL_PRIV = "privilege";

  public UserPrivilegeResource(@PathParam("organizationUniqueShortName") String organizationUniqueShortName, @PathParam(
      "username") String username, @PathParam("privilegeName") String privilegeName) {
    this.organizationUniqueShortName = organizationUniqueShortName;
    this.username = username;
    this.privilegeName = privilegeName;
    privilege = getPrivilege();
    organization = getOrganization();
    user = getUser();
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    ResponseBuilder responseBuilder;
    if(organization==null || user==null || privilege==null){
      responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    try {
      responseBuilder = Response.status(Status.OK);
      Feed privilegeFeed = getUserPrivilegeFeed();
      responseBuilder = Response.ok(privilegeFeed);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  @DELETE
  public Response delete() {
    ResponseBuilder responseBuilder;
    if(organization==null || user==null || privilege==null){
      responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    try {
      responseBuilder = Response.status(Status.OK);
      User user = Services.getInstance().getUserService().getUserByOrganizationAndUserName(organizationUniqueShortName,
                                                                                           username);
      user.getPrivileges().remove(privilege);
      Services.getInstance().getUserService().update(user);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Feed getUserPrivilegeFeed() {

    Feed privilegeFeed = abderaFactory.newFeed();

    privilegeFeed.setId(privilegeName);
    privilegeFeed.setTitle(privilegeName);
    privilegeFeed.addLink(getSelfLink());

    Link altLink = abderaFactory.newLink();
    altLink.setHref(USER_PRIVILEGE_URI_BUILDER.clone().build(organizationUniqueShortName, username, privilegeName).
        toString());
    altLink.setRel(Link.REL_ALTERNATE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);
    privilegeFeed.addLink(altLink);
    
    Link privLink = abderaFactory.newLink();
    privLink.setHref(PRIVILEGE_URI_BUILDER.clone().build(organizationUniqueShortName, privilegeName).toString());
    privLink.setRel(REL_PRIV);
    privLink.setMimeType(MediaType.APPLICATION_JSON);
    privilegeFeed.addLink(privLink);

    return privilegeFeed;
  }

  private Privilege getPrivilege() {
    return Services.getInstance().getPrivilegeService().getPrivilegeByOrganizationAndPrivilegeName(
        organizationUniqueShortName, privilegeName);
  }

  private Organization getOrganization() {
    return Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(organizationUniqueShortName);
  }

  private User getUser() {
    return Services.getInstance().getUserService().getUserByOrganizationAndUserName(organizationUniqueShortName,
                                                                                    username);
  }
}
