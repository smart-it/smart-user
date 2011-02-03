/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.service.Services;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.util.rest.atom.server.AbstractResource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author modhu7
 */
@Path("/orgs/sn/{organizationName}/users/un/{userName}/roles")
public class UserRolesResource extends AbstractResource {

  private String organizationName;
  private String userName;
  private Organization organization;
  private User user;  
  static final Method ROLE_AFTER_ROLE_NAME__METHOD;
  static final Method ROLE_BEFORE_ROLE_NAME__METHOD;
  @QueryParam("count")
  private Integer count;

  static {
    try {
      ROLE_AFTER_ROLE_NAME__METHOD = (RolesResource.class.getMethod("getAfter", String.class));
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }
    try {
      ROLE_BEFORE_ROLE_NAME__METHOD = (RolesResource.class.getMethod("getBefore", String.class));
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }
  }

  public UserRolesResource(@PathParam("organizationName") String organizationName,
                           @PathParam("userName") String userName) {
    this.organizationName = organizationName;
    this.userName = userName;
    user = Services.getInstance().getUserService().getUserByOrganizationAndUserName(organizationName, userName);
    organization = Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(organizationName);
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Path("/after/{roleName}")
  public Response getAfter(@PathParam("roleName") String roleName) {
    return get(roleName, false);
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Path("/before/{roleName}")
  public Response getBefore(@PathParam("roleName") String roleName) {
    return get(roleName, true);
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    return get(null, true);
  }

  public Response get(String roleName, boolean isBefore) {
    if (count == null) {
      count = 10;
    }
    if(organization==null || user==null){
      ResponseBuilder responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }

    ResponseBuilder responseBuilder = Response.status(Status.OK);
    Feed atomFeed = getFeed("roles", new Date());
    Link rolesLink = getAbderaFactory().newLink();

    rolesLink.setHref(UriBuilder.fromResource(RootResource.class).build().toString());
    rolesLink.setRel("parent");
    atomFeed.addLink(rolesLink);

    Collection<Role> roles = user.getRoles();
    if (roles != null && !roles.isEmpty()) {

      // uri builder for next and previous uri according to the count value
      UriBuilder nextRoleUri = getRelativeURIBuilder().path(UserRolesResource.class).path(ROLE_AFTER_ROLE_NAME__METHOD);
      UriBuilder previousRoleUri = getRelativeURIBuilder().path(UserRolesResource.class).path(ROLE_BEFORE_ROLE_NAME__METHOD);

      List<Role> roleList = new ArrayList<Role>(roles);

      // link to the next uri according to the count value
      Link nextLink = getAbderaFactory().newLink();
      nextLink.setRel(Link.REL_NEXT);

      Role firstRole = roleList.get(0);
      nextLink.setHref(nextRoleUri.build(organizationName, userName, firstRole.getName()).toString());
      atomFeed.addLink(nextLink);

      Role lastRole = roleList.get(roleList.size() - 1);

      // link to the previous uri according to the count value
      Link previousLink = getAbderaFactory().newLink();
      previousLink.setRel(Link.REL_PREVIOUS);
      previousLink.setHref(previousRoleUri.build(organizationName, userName, lastRole.getName()).toString());

      atomFeed.addLink(previousLink);

      for (Role role : roles) {
        Entry roleEntry = getAbderaFactory().newEntry();

        roleEntry.setId(role.getName());
        roleEntry.setTitle(role.getDisplayName());
        roleEntry.setSummary(role.getShortDescription());
        roleEntry.setUpdated(role.getLastModifiedDate());

        // setting link to each individual role
        Link roleLink = getAbderaFactory().newLink();
        roleLink.setRel(Link.REL_ALTERNATE);
        roleLink.setHref(getRelativeURIBuilder().path(UserRoleResource.class).build(organizationName, userName, role.getName()).toString());
        roleLink.setMimeType(MediaType.APPLICATION_ATOM_XML);
        roleEntry.addLink(roleLink);
        atomFeed.addEntry(roleEntry);
      }

    }
    responseBuilder.entity(atomFeed);
    return responseBuilder.build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response post(Role role) {
    ResponseBuilder responseBuilder;
     if(organization==null || user==null){
      responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }

    try {
      if (role.getId() == null) {
        responseBuilder = Response.status(Status.BAD_REQUEST);
      }
      else {
        user.getRoles().add(role);
        Services.getInstance().getUserService().update(user);
        responseBuilder = Response.status(Status.CREATED);
        responseBuilder.location(getAbsoluteURIBuilder().path(UserRoleResource.class).build(organizationName, userName, role.getName()));
      }
    }
    catch (Exception ex) {      
      responseBuilder = Response.status(Status.BAD_REQUEST);
    }
    return responseBuilder.build();
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
