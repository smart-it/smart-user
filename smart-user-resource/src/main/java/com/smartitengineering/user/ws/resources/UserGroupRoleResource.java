/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.service.Services;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Role;
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
@Path("/orgs/sn/{organizationUniqueShortName}/usergroups/name/{groupName}/roles/name/{roleName}")
public class UserGroupRoleResource extends AbstractResource {

  private String organizationUniqueShortName;
  private String groupName;
  private String roleName;
  private Organization organization;
  private UserGroup userGroup;
  private Role role;
  private static String REL_ROLE = "role";

  public UserGroupRoleResource(@PathParam("organizationUniqueShortName") String organizationUniqueShortName, @PathParam(
      "groupName") String groupName, @PathParam("roleName") String roleName) {
    this.organizationUniqueShortName = organizationUniqueShortName;
    this.groupName = groupName;
    this.roleName = roleName;
    role = getRole();
    userGroup = getUserGroup();
    organization = getOrganization();

  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    ResponseBuilder responseBuilder;
    try {
      responseBuilder = Response.status(Status.OK);
      Feed roleFeed = getUserGroupRoleFeed();
      responseBuilder = Response.ok(roleFeed);
    }
    catch (Exception ex) {      
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  @DELETE
  public Response delete() {
    ResponseBuilder responseBuilder;
    try {
      responseBuilder = Response.status(Status.OK);
      userGroup.getRoles().remove(role);
      Services.getInstance().getUserGroupService().update(userGroup);
    }
    catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Feed getUserGroupRoleFeed() {

    Feed roleFeed = getAbderaFactory().newFeed();

    roleFeed.setId(roleName);
    roleFeed.setTitle(roleName);
    roleFeed.addLink(getSelfLink());

    Link altLink = getAbderaFactory().newLink();
    altLink.setHref(getRelativeURIBuilder().path(UserGroupRoleResource.class).build(organizationUniqueShortName, groupName, roleName).toString());
    altLink.setRel(Link.REL_ALTERNATE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);
    roleFeed.addLink(altLink);

    Link roleLink = getAbderaFactory().newLink();
    roleLink.setHref(getRelativeURIBuilder().path(RoleResource.class).build(roleName).toString());
    roleLink.setRel(REL_ROLE);
    roleLink.setMimeType(MediaType.APPLICATION_JSON);
    roleFeed.addLink(roleLink);

    return roleFeed;
  }

  private Role getRole() {
    return Services.getInstance().getRoleService().getRoleByName(roleName);
  }

  private UserGroup getUserGroup() {
    return Services.getInstance().getUserGroupService().getByOrganizationAndUserGroupName(organizationUniqueShortName,
                                                                                          groupName);
  }

  private Organization getOrganization() {
    return Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(organizationUniqueShortName);
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
