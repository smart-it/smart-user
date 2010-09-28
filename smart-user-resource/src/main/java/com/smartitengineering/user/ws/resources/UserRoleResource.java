/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

/**
 *
 * @author modhu7
 */
import com.smartitengineering.user.domain.Role;
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
@Path("/orgs/sn/{organizationUniqueShortName}/un/{username}/roles/name/{roleName}")
public class UserRoleResource extends AbstractResource {

  static final UriBuilder USER_ROLE_URI_BUILDER = UriBuilder.fromResource(UserRoleResource.class);
  static final UriBuilder ROLE_URI_BUILDER = RoleResource.ROLE_URI_BUILDER.clone();
  private String organizationUniqueShortName;
  private String username;
  private String roleName;
  private static String REL_ROLE = "role";

  public UserRoleResource(@PathParam("organizationUniqueShortName") String organizationUniqueShortName, @PathParam(
      "username") String username, @PathParam("roleName") String roleName) {
    this.organizationUniqueShortName = organizationUniqueShortName;
    this.username = username;
    this.roleName = roleName;
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
      User user = Services.getInstance().getUserService().getUserByOrganizationAndUserName(organizationUniqueShortName,
                                                                                           username);
      user.getRoles().remove(getRole());
      Services.getInstance().getUserService().update(user);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Feed getUserRoleFeed() {

    Feed roleFeed = abderaFactory.newFeed();

    roleFeed.setId(roleName);
    roleFeed.setTitle(roleName);
    roleFeed.addLink(getSelfLink());

    Link altLink = abderaFactory.newLink();
    altLink.setHref(USER_ROLE_URI_BUILDER.clone().build(organizationUniqueShortName, username, roleName).
        toString());
    altLink.setRel(Link.REL_ALTERNATE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);
    roleFeed.addLink(altLink);

    Link roleLink = abderaFactory.newLink();
    altLink.setHref(ROLE_URI_BUILDER.clone().build(organizationUniqueShortName, roleName).toString());
    altLink.setRel(REL_ROLE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);
    roleFeed.addLink(roleLink);

    return roleFeed;
  }

  private Role getRole() {
    return Services.getInstance().getRoleService().getRoleByName(roleName);
  }
}
