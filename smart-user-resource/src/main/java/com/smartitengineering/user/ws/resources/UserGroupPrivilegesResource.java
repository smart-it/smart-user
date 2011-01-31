/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.service.Services;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.UserGroup;
import com.smartitengineering.util.rest.atom.server.AbstractResource;
import java.lang.reflect.Method;
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
@Path("/orgs/sn/{organizationName}/usergroups/name/{groupName}/privs")
public class UserGroupPrivilegesResource extends AbstractResource {

  private String organizationName;
  private String groupName;
  private Organization organization;
  private UserGroup userGroup;
  static Method USER_GROUP_PRIVILEGE_AFTER_NAME_METHOD;
  static Method USER_GROUP_PRIVILEGE_BEFORE_NAME_METHOD;

  static {
    try {
      USER_GROUP_PRIVILEGE_BEFORE_NAME_METHOD = UserGroupPrivilegesResource.class.getMethod("getBefore", String.class);
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }
    try {
      USER_GROUP_PRIVILEGE_AFTER_NAME_METHOD = UserGroupPrivilegesResource.class.getMethod("getAfter", String.class);
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }
  }

  public UserGroupPrivilegesResource(@PathParam("organizationName") String organizationName,
                                     @PathParam("groupName") String groupName) {
    this.organizationName = organizationName;
    this.groupName = groupName;
    userGroup = Services.getInstance().getUserGroupService().getByOrganizationAndUserGroupName(organizationName,
                                                                                               groupName);
    organization = getOrganization();
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
    if (organization == null || userGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    // create a new atom feed
    Feed atomFeed = getAbderaFactory().newFeed();

    // create a link to parent resource, in this case now it is linked to root resource
    Link parentResourceLink = getAbderaFactory().newLink();
    parentResourceLink.setHref(getRelativeURIBuilder().path(OrganizationResource.class).build(organizationName).toString());
    parentResourceLink.setRel("organization");
    atomFeed.addLink(parentResourceLink);

    // get the organizations accoring to the query
    Collection<Privilege> privileges = userGroup.getPrivileges();

    if (privileges != null && !privileges.isEmpty()) {
      MultivaluedMap<String, String> queryParam = getUriInfo().getQueryParameters();
      List<Privilege> privilegeList = new ArrayList<Privilege>(privileges);

      // uri builder for next and previous organizations according to count
      final UriBuilder nextUri = getRelativeURIBuilder().path(UserGroupPrivilegesResource.class).path(
          USER_GROUP_PRIVILEGE_AFTER_NAME_METHOD);
      final UriBuilder previousUri = getRelativeURIBuilder().path(UserGroupPrivilegesResource.class).path(
          USER_GROUP_PRIVILEGE_BEFORE_NAME_METHOD);

      // link to the next organizations based on count
      Link nextLink = getAbderaFactory().newLink();
      nextLink.setRel(Link.REL_NEXT);
      Privilege lastPrivilege = privilegeList.get(0);
      for (String key : queryParam.keySet()) {
        final Object[] values = queryParam.get(key).toArray();
        nextUri.queryParam(key, values);
        previousUri.queryParam(key, values);
      }
      nextLink.setHref(nextUri.build(organizationName, groupName, lastPrivilege.getName()).toString());
      //nextLink.setHref(UriBuilder.fromResource(OrganizationsResource.class).build(lastOrganization.getUniqueShortName()).toString());

      atomFeed.addLink(nextLink);

      /* link to the previous organizations based on count */
      Link prevLink = getAbderaFactory().newLink();
      prevLink.setRel(Link.REL_PREVIOUS);
      Privilege firstPrivilege = privilegeList.get(privileges.size() - 1);

      prevLink.setHref(previousUri.build(organizationName, groupName, firstPrivilege.getName()).toString());
      //prevLink.setHref(nameLike)
      atomFeed.addLink(prevLink);

      // add entry of individual organization
      for (Privilege privilege : privileges) {
        Entry userPrivilegeEntry = getAbderaFactory().newEntry();

        userPrivilegeEntry.setId(privilege.getName().toString());
        userPrivilegeEntry.setTitle(privilege.getDisplayName());
        userPrivilegeEntry.setSummary(privilege.getShortDescription());

        Link userPrivilegeLink = getAbderaFactory().newLink();
        userPrivilegeLink.setHref(getRelativeURIBuilder().path(UserGroupPrivilegeResource.class).build(organizationName,
                                                                                                  groupName, privilege.
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
    System.out.println("---------------------Start form resource ");
    if (organization == null) {
      System.out.println("Organization null");
    }
    if (userGroup == null) {
      System.out.println("User Group null");
    }
    System.out.println("---------------------End form resource ");
    if (organization == null || userGroup == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    if (privilege.getId() != null) {
      System.out.println("$$$Id of privelege " + privilege.getId());
    }
    if (privilege.getVersion() != null) {
      System.out.println("$$$Version of privelege " + privilege.getVersion());
    }
    try {
      if (privilege.getId() == null /*|| privilege.getVersion() == null*/) {
        responseBuilder = Response.status(Status.BAD_REQUEST);
      }
      else {
        privilege.setParentOrganization(organization);
        if(userGroup.getPrivileges() != null){
          System.out.println("$$@@@@ User Group Privileges"+userGroup.getPrivileges());
        }
        else{
          System.out.println("$$@@@@ User Group Privileges is null");
        }
        userGroup.getPrivileges().add(privilege);
        Services.getInstance().getUserGroupService().update(userGroup);
        responseBuilder = Response.status(Status.CREATED);
        responseBuilder.location(getAbsoluteURIBuilder().path(UserGroupPrivilegeResource.class).build(organizationName,
                                                                                                      groupName, privilege.
            getName()));
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

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
