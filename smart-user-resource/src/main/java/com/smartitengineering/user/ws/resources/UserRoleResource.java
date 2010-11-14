/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

/**
 *
 * @author modhu7
 */
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.User;
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
@Path("/orgs/sn/{organizationUniqueShortName}/users/un/{username}/roles/name/{roleName}")
public class UserRoleResource extends AbstractResource {

  private String organizationUniqueShortName;
  private String username;
  private String roleName;
  private Organization organization;
  private User user;
  private Role role;
  private static String REL_ROLE = "role";

  public UserRoleResource(@PathParam("organizationUniqueShortName") String organizationUniqueShortName, @PathParam(
      "username") String username, @PathParam("roleName") String roleName) {
    this.organizationUniqueShortName = organizationUniqueShortName;
    this.username = username;
    this.roleName = roleName;
    role = getRole();
    user = getUser();
    organization = getOrganization();

  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    ResponseBuilder responseBuilder;
    try {
      responseBuilder = Response.status(Status.OK);
      Feed roleFeed = getUserRoleFeed();
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
      user.getRoles().remove(role);
      Services.getInstance().getUserService().update(user);
    }
    catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Feed getUserRoleFeed() {

    Feed roleFeed = getAbderaFactory().newFeed();

    roleFeed.setId(roleName);
    roleFeed.setTitle(roleName);
    roleFeed.addLink(getSelfLink());

    Link altLink = getAbderaFactory().newLink();
    altLink.setHref(getRelativeURIBuilder().path(UserRoleResource.class).build(organizationUniqueShortName, username,
                                                                               roleName).toString());
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

  private User getUser() {
    return Services.getInstance().getUserService().getUserByOrganizationAndUserName(organizationUniqueShortName,
                                                                                    username);
  }

  private Organization getOrganization() {
    return Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(organizationUniqueShortName);
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
