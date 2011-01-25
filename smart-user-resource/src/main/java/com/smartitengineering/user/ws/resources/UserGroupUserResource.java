/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.service.Services;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserGroup;
import com.smartitengineering.util.rest.atom.server.AbstractResource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author modhu7
 */
@Path("/orgs/sn/{organizationUniqueShortName}/usergroups/name/{groupname}/users/un/{username}")
public class UserGroupUserResource extends AbstractResource {

  private String organizationUniqueShortName;
  private String groupName;
  private String username;
  private static String REL_USER = "user";
  private Organization organization;
  private UserGroup userGroup;
  private User user;

  public UserGroupUserResource(@PathParam("organizationUniqueShortName") String organizationUniqueShortName, @PathParam(
      "groupname") String groupName, @PathParam("username") String username) {
    this.organizationUniqueShortName = organizationUniqueShortName;
    this.groupName = groupName;
    this.username = username;
    organization = getOrganization();
    userGroup = getUserGroup();
    user = getUser();
  }

  private Organization getOrganization() {
    return Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(organizationUniqueShortName);
  }

  private UserGroup getUserGroup() {
    return Services.getInstance().getUserGroupService().getByOrganizationAndUserGroupName(organizationUniqueShortName,
                                                                                          groupName);
  }

  private User getUser() {
    return Services.getInstance().getUserService().getUserByOrganizationAndUserName(organizationUniqueShortName,
                                                                                    username);
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    ResponseBuilder responseBuilder;
    if (organization == null || userGroup == null || user == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    try {
      responseBuilder = Response.status(Status.OK);
      Feed userFeed = getUserGroupUserFeed();
      responseBuilder = Response.ok(userFeed);
    }
    catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Feed getUserGroupUserFeed() {
    Feed userFeed = getAbderaFactory().newFeed();

    userFeed.setId(username);
    userFeed.setTitle(username);
    userFeed.addLink(getSelfLink());

    Link altLink = getAbderaFactory().newLink();
    altLink.setHref(getRelativeURIBuilder().path(UserGroupUserResource.class).build(organizationUniqueShortName,
                                                                                    groupName, username).toString());
    altLink.setRel(Link.REL_ALTERNATE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);
    userFeed.addLink(altLink);

    Link userLink = getAbderaFactory().newLink();
    userLink.setHref(getRelativeURIBuilder().path(OrganizationUserResource.class).build(organizationUniqueShortName, username).toString());
    userLink.setRel(REL_USER);
    userLink.setMimeType(MediaType.APPLICATION_JSON);
    userFeed.addLink(userLink);

    return userFeed;
  }

  @DELETE
  public Response delete() {
    ResponseBuilder responseBuilder;
    if (organization == null || userGroup == null || user == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    try {
      responseBuilder = Response.status(Status.OK);
      userGroup.getUsers().remove(user);
      System.out.print("!!!!!@@@@@@"+user);
      Services.getInstance().getUserGroupService().update(userGroup);
    }
    catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
