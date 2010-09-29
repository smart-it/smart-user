/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserGroup;
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
 * @author modhu7
 */
@Path("/orgs/sn/{organizationName}/usergroups/name/{name}/users")
public class UserGroupUsersResource extends AbstractResource {

  private String organizationName;
  private String groupName;
  private UserGroup userGroup;
  private Organization organization;
  static UriBuilder USER_GROUP_USERS_URIBUILDER;
  static UriBuilder USER_GROUP_USERS_AFTER_NAME_URIBUILDER;
  static UriBuilder USER_GROUP_USERS_BEFORE_NAME_URIBUILDER;

  static {
    USER_GROUP_USERS_URIBUILDER = UriBuilder.fromResource(UserGroupUsersResource.class);
    USER_GROUP_USERS_BEFORE_NAME_URIBUILDER = UriBuilder.fromResource(UserGroupUsersResource.class);

    try {
      USER_GROUP_USERS_BEFORE_NAME_URIBUILDER.path(UserGroupUsersResource.class.getMethod("getBefore", String.class));
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    USER_GROUP_USERS_AFTER_NAME_URIBUILDER = UriBuilder.fromResource(UserGroupUsersResource.class);
    try {
      USER_GROUP_USERS_AFTER_NAME_URIBUILDER.path(UserGroupUsersResource.class.getMethod("getAfter", String.class));
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public UserGroupUsersResource(@PathParam("organizationName") String organizationName,
                                @PathParam("name") String groupName) {
    this.organizationName = organizationName;
    this.groupName = groupName;
    userGroup = Services.getInstance().getUserGroupService().getByOrganizationAndUserGroupName(organizationName,
                                                                                               groupName);
    organization = getOrganization();
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Path("/before/{groupName}")
  public Response getBefore(@PathParam("groupName") String beforeGroupName) {
    return get(beforeGroupName, true);
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Path("/after/{groupName}")
  public Response getAfter(@PathParam("groupName") String afterGroupName) {
    return get(afterGroupName, false);
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    return get(null, true);
  }

  public Response get(String groupName, boolean isBefore) {
    ResponseBuilder responseBuilder = Response.ok();
    if(organization == null || userGroup == null){
      return Response.status(Status.NOT_FOUND).build();
    }

    // create a new atom feed
    Feed atomFeed = abderaFactory.newFeed();

    // create a link to parent resource, in this case now it is linked to root resource
    Link parentResourceLink = abderaFactory.newLink();
    parentResourceLink.setHref(UriBuilder.fromResource(OrganizationResource.class).build(organizationName).toString());
    parentResourceLink.setRel("organization");
    atomFeed.addLink(parentResourceLink);

    // get the organizations accoring to the query
    Collection<User> users = userGroup.getUsers();

    if (users != null && !users.isEmpty()) {
      MultivaluedMap<String, String> queryParam = uriInfo.getQueryParameters();
      List<User> usersList = new ArrayList<User>(users);

      // uri builder for next and previous organizations according to count
      final UriBuilder nextUri = USER_GROUP_USERS_AFTER_NAME_URIBUILDER.clone();
      final UriBuilder previousUri = USER_GROUP_USERS_BEFORE_NAME_URIBUILDER.clone();

      // link to the next organizations based on count
      Link nextLink = abderaFactory.newLink();
      nextLink.setRel(Link.REL_NEXT);
      User lastUser = usersList.get(0);
      for (String key : queryParam.keySet()) {
        final Object[] values = queryParam.get(key).toArray();
        nextUri.queryParam(key, values);
        previousUri.queryParam(key, values);
      }
      nextLink.setHref(nextUri.build(organizationName, groupName, lastUser.getUsername()).toString());
      //nextLink.setHref(UriBuilder.fromResource(OrganizationsResource.class).build(lastOrganization.getUniqueShortName()).toString());

      atomFeed.addLink(nextLink);

      /* link to the previous organizations based on count */
      Link prevLink = abderaFactory.newLink();
      prevLink.setRel(Link.REL_PREVIOUS);
      User firstUser = usersList.get(users.size() - 1);

      prevLink.setHref(previousUri.build(organizationName, groupName, firstUser.getUsername()).toString());
      //prevLink.setHref(nameLike)
      atomFeed.addLink(prevLink);

      // add entry of individual organization
      for (User user : users) {
        Entry userGroupUserEntry = abderaFactory.newEntry();

        userGroupUserEntry.setId(user.getUsername());
        userGroupUserEntry.setTitle(user.getUsername());
        userGroupUserEntry.setSummary(user.getUsername());
        userGroupUserEntry.setUpdated(user.getLastModifiedDate());

        Link userGroupUserLink = abderaFactory.newLink();
        userGroupUserLink.setHref(UriBuilder.fromResource(OrganizationUserResource.class).build(organizationName, user.
            getUsername()).toString());
        userGroupUserLink.setRel(Link.REL_ALTERNATE);
        userGroupUserLink.setMimeType(MediaType.APPLICATION_ATOM_XML);
        userGroupUserEntry.addLink(userGroupUserLink);

        atomFeed.addEntry(userGroupUserEntry);
      }
    }
    responseBuilder.entity(atomFeed);
    return responseBuilder.build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response post(User user) {
    ResponseBuilder responseBuilder;
    if(organization == null || userGroup == null){
      return Response.status(Status.NOT_FOUND).build();
    }
    try {
      if (user.getId() == null || user.getVersion() == null) {
        responseBuilder = Response.status(Status.BAD_REQUEST);
      }
      else {
        user.setOrganization(organization);
        userGroup.getUsers().add(user);
        Services.getInstance().getUserService().update(user);
        responseBuilder = Response.status(Status.CREATED);
        responseBuilder.location(uriInfo.getBaseUriBuilder().path(UserGroupUserResource.USER_GROUP_USER_URI_BUILDER.clone().
            build(organizationName, groupName, user.getUsername()).toString()).build());
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
