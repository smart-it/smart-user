/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author russel
 */
@Path("/orgs/sn/{organizationName}/users/un/{userName}/privileges")
public class UserPrivilegesResource extends AbstractResource {

  private String organizationName;
  private String userName;
  private User user;
  static UriBuilder USER_PRIVILEGE_URIBUILDER;
  static UriBuilder USER_PRIVILEGE_AFTER_NAME_URIBUILDER;
  static UriBuilder USER_PRIVILEGE_BEFORE_NAME_URIBUILDER;

  static {
    USER_PRIVILEGE_URIBUILDER = UriBuilder.fromResource(UserPrivilegesResource.class);
    USER_PRIVILEGE_BEFORE_NAME_URIBUILDER = UriBuilder.fromResource(UserPrivilegesResource.class);

    try {
      USER_PRIVILEGE_BEFORE_NAME_URIBUILDER.path(UserPrivilegesResource.class.getMethod("getBefore", String.class));
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    USER_PRIVILEGE_AFTER_NAME_URIBUILDER = UriBuilder.fromResource(UserPrivilegesResource.class);
    try {
      USER_PRIVILEGE_AFTER_NAME_URIBUILDER.path(UserPrivilegesResource.class.getMethod("getAfter", String.class));
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public UserPrivilegesResource(@PathParam("organizationName") String organizationName,
                                @PathParam("userName") String userName) {
    this.organizationName = organizationName;
    this.userName = userName;
    user = Services.getInstance().getUserService().getUserByOrganizationAndUserName(organizationName, userName);
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Path("/before/{privilegeName}")
  public Response getBefore(@PathParam("privilegeName") String beforePrivilegeName) {
    return get(beforePrivilegeName, true);
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Path("/after/{privilegeName}")
  public Response getAfter(@PathParam("privilegeName") String afterPrivilegeName) {
    return get(afterPrivilegeName, false);
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    return get(null, true);
  }

  public Response get(String privilegeName, boolean isBefore) {
    ResponseBuilder responseBuilder = Response.ok();

    // create a new atom feed
    Feed atomFeed = abderaFactory.newFeed();

    // create a link to parent resource, in this case now it is linked to root resource
    Link parentResourceLink = abderaFactory.newLink();
    parentResourceLink.setHref(UriBuilder.fromResource(OrganizationResource.class).build(organizationName).toString());
    parentResourceLink.setRel("organization");
    atomFeed.addLink(parentResourceLink);

    // get the organizations accoring to the query
    Collection<Privilege> privileges = user.getPrivileges();

    if (privileges != null && !privileges.isEmpty()) {
      MultivaluedMap<String, String> queryParam = uriInfo.getQueryParameters();
      List<Privilege> privilegeList = new ArrayList<Privilege>(privileges);

      // uri builder for next and previous organizations according to count
      final UriBuilder nextUri = USER_PRIVILEGE_AFTER_NAME_URIBUILDER.clone();
      final UriBuilder previousUri = USER_PRIVILEGE_BEFORE_NAME_URIBUILDER.clone();

      // link to the next organizations based on count
      Link nextLink = abderaFactory.newLink();
      nextLink.setRel(Link.REL_NEXT);
      Privilege lastPrivilege = privilegeList.get(0);
      for (String key : queryParam.keySet()) {
        final Object[] values = queryParam.get(key).toArray();
        nextUri.queryParam(key, values);
        previousUri.queryParam(key, values);
      }
      nextLink.setHref(nextUri.build(organizationName, userName, lastPrivilege.getName()).toString());
      //nextLink.setHref(UriBuilder.fromResource(OrganizationsResource.class).build(lastOrganization.getUniqueShortName()).toString());

      atomFeed.addLink(nextLink);

      /* link to the previous organizations based on count */
      Link prevLink = abderaFactory.newLink();
      prevLink.setRel(Link.REL_PREVIOUS);
      Privilege firstPrivilege = privilegeList.get(privileges.size() - 1);

      prevLink.setHref(previousUri.build(organizationName, userName, firstPrivilege.getName()).toString());
      //prevLink.setHref(nameLike)
      atomFeed.addLink(prevLink);

      // add entry of individual organization
      for (Privilege privilege : privileges) {
        Entry userPrivilegeEntry = abderaFactory.newEntry();

        userPrivilegeEntry.setId(privilege.getName().toString());
        userPrivilegeEntry.setTitle(privilege.getDisplayName());
        userPrivilegeEntry.setSummary(privilege.getShortDescription());

        Link userPrivilegeLink = abderaFactory.newLink();
        userPrivilegeLink.setHref(UriBuilder.fromResource(UserPrivilegeResource.class).build(organizationName, userName, privilege.
            getName()).toString());
        userPrivilegeLink.setRel(Link.REL_ALTERNATE);
        userPrivilegeLink.setMimeType(MediaType.APPLICATION_ATOM_XML);
        userPrivilegeEntry.addLink(userPrivilegeLink);

        atomFeed.addEntry(userPrivilegeEntry);
      }
    }
    responseBuilder.entity(atomFeed);
    return responseBuilder.build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response post(Privilege privilege) {
    ResponseBuilder responseBuilder;
    try {
      if (privilege.getId() == null || privilege.getVersion() == null) {
        responseBuilder = Response.status(Status.BAD_REQUEST);
      }
      else {
        privilege.setParentOrganization(getOrganization());
        user.getPrivileges().add(privilege);
        Services.getInstance().getUserService().update(user);
        responseBuilder = Response.status(Status.CREATED);
        responseBuilder.location(uriInfo.getBaseUriBuilder().path(UserPrivilegeResource.USER_PRIVILEGE_URI_BUILDER.clone().
            build(organizationName, userName, privilege.getName()).toString()).build());
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Organization getOrganization() {
    return Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(organizationName);
  }
}
