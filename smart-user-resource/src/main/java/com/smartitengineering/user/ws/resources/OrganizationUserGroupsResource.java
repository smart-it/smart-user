/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.UserGroup;
import com.sun.jersey.api.view.Viewable;
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
@Path("/organizations/{uniqueShortName}/usergroups")
public class OrganizationUserGroupsResource extends AbstractResource{

    private String organizationUniqueShortName;

    static final UriBuilder ORGANIZATION_USER_GROUPS_URI_BUILDER;
    static final UriBuilder ORGANIZATIONS_USER_GROUPS_BEFORE_NAME_URI_BUILDER;
    static final UriBuilder ORGANIZATIONS_USER_GROUPS_AFTER_NAME_URI_BUILDER;

    static {

        ORGANIZATION_USER_GROUPS_URI_BUILDER = UriBuilder.fromResource(OrganizationUserGroupsResource.class);

        ORGANIZATIONS_USER_GROUPS_AFTER_NAME_URI_BUILDER = UriBuilder.fromResource(OrganizationUserGroupsResource.class);
        try {
            ORGANIZATIONS_USER_GROUPS_AFTER_NAME_URI_BUILDER.path(OrganizationUsersResource.class.getMethod("getAfter", String.class));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ORGANIZATIONS_USER_GROUPS_BEFORE_NAME_URI_BUILDER = UriBuilder.fromResource(OrganizationUserGroupsResource.class);
        try {
            ORGANIZATIONS_USER_GROUPS_BEFORE_NAME_URI_BUILDER.path(OrganizationUsersResource.class.getMethod("getBefore", String.class));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @PathParam("count")
    private Integer count;


    public OrganizationUserGroupsResource(@PathParam("uniqueShortName") String organizationUniqueShortName) {
        this.organizationUniqueShortName = organizationUniqueShortName;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml() {
        ResponseBuilder responseBuilder = Response.ok();
        Collection<UserGroup> userGroups = Services.getInstance().getUserGroupService().getByOrganizationName(organizationUniqueShortName);

        Viewable view = new Viewable("userList", userGroups, OrganizationUsersResource.class);
        responseBuilder.entity(view);
        return responseBuilder.build();

    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/before/{beforeUserGroupName}")
    public Response getBefore(@PathParam("beforeUserGroupName") String beforeUserGroupName) {
        return get(organizationUniqueShortName, beforeUserGroupName, true);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/after/{afterUserGroupName}")
    public Response getAfter(@PathParam("afterUserGroupName") String afterUserGroupName) {
        return get(organizationUniqueShortName, afterUserGroupName, false);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get() {
        return get(organizationUniqueShortName, null, true);
    }

    private Response get(String uniqueOrganizationName, String userName, boolean isBefore) {

        if (count == null) {
            count = 10;
        }
        ResponseBuilder responseBuilder = Response.ok();
        Feed atomFeed = getFeed(userName, new Date());

        Link parentLink = abderaFactory.newLink();
        parentLink.setHref(UriBuilder.fromResource(RootResource.class).build().toString());
        parentLink.setRel("parent");
        atomFeed.addLink(parentLink);


        Collection<UserGroup> userGroups = Services.getInstance().getUserGroupService().getByOrganizationName(uniqueOrganizationName);


        if (userGroups != null && !userGroups.isEmpty()) {

            MultivaluedMap<String, String> queryParam = uriInfo.getQueryParameters();
            List<UserGroup> userGroupList = new ArrayList<UserGroup>(userGroups);

            // uri builder for next and previous organizations according to count
            final UriBuilder nextUri = ORGANIZATIONS_USER_GROUPS_AFTER_NAME_URI_BUILDER.clone();
            final UriBuilder previousUri = ORGANIZATIONS_USER_GROUPS_BEFORE_NAME_URI_BUILDER.clone();

            // link to the next organizations based on count
            Link nextLink = abderaFactory.newLink();
            nextLink.setRel(Link.REL_NEXT);
            UserGroup lastUserGroup = userGroupList.get(0);


            for (String key : queryParam.keySet()) {
                final Object[] values = queryParam.get(key).toArray();
                nextUri.queryParam(key, values);
                previousUri.queryParam(key, values);
            }
            nextLink.setHref(nextUri.build(organizationUniqueShortName, lastUserGroup.getName()).toString());


            atomFeed.addLink(nextLink);

            /* link to the previous organizations based on count */
            Link prevLink = abderaFactory.newLink();
            prevLink.setRel(Link.REL_PREVIOUS);
            UserGroup firstUser = userGroupList.get(userGroups.size() - 1);

            prevLink.setHref(previousUri.build(organizationUniqueShortName, firstUser.getName()).toString());
            atomFeed.addLink(prevLink);

            for (UserGroup userGroup : userGroups) {

                Entry userEntry = abderaFactory.newEntry();

                userEntry.setId(userGroup.getName());
                userEntry.setTitle(userGroup.getName());
                userEntry.setSummary(userGroup.getName());
                userEntry.setUpdated(userGroup.getLastModifiedDate());

                // setting link to the each individual user
                Link userLink = abderaFactory.newLink();                
                userLink.setHref(OrganizationUserGroupResource.ORGANIZATION_USER_GROUP_URI_BUILDER.build(organizationUniqueShortName, userGroup.getName()).toString());
                userLink.setRel(Link.REL_ALTERNATE);
                userLink.setMimeType(MediaType.APPLICATION_ATOM_XML);

                userEntry.addLink(userLink);

                atomFeed.addEntry(userEntry);
            }
        }
        responseBuilder.entity(atomFeed);
        return responseBuilder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(UserGroup userGroup) {

        ResponseBuilder responseBuilder;
        try {
//            if (user.getRoleIDs() != null) {
//                Services.getInstance().getRoleService().populateRole(user);
//            }
//            if (user.getPrivilegeIDs() != null) {
//                Services.getInstance().getPrivilegeService().populatePrivilege(user);
//            }
            if (userGroup.getParentOrganizationID() == null) {
                throw new Exception("No organization found");
            }
            Services.getInstance().getOrganizationService().populateOrganization(userGroup);
            Services.getInstance().getUserGroupService().save(userGroup);
            responseBuilder = Response.status(Status.CREATED);
        } catch (Exception ex) {
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
            ex.printStackTrace();
        }
        return responseBuilder.build();
    }
}
