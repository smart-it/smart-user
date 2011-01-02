/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.User;
import com.smartitengineering.util.rest.atom.server.AbstractResource;
import java.util.Date;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author modhu7
 */
@Path("/login")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class LoginResource extends AbstractResource {

  static final UriBuilder LOGIN_URI_BUILDER = UriBuilder.fromResource(LoginResource.class);
  @QueryParam("username")
  private String userNameWithOrganizationName;

  public LoginResource() {
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {

    ResponseBuilder responseBuilder = Response.status(Status.OK);

    Feed atomFeed = getFeed("Login Resource", new Date());

    Link organizationsLink = Abdera.getNewFactory().newLink();
    organizationsLink.setHref(getRelativeURIBuilder().path(OrganizationsResource.class).build().toString());
    organizationsLink.setRel("Organizations");
    atomFeed.addLink(organizationsLink);

    Link rolesLink = Abdera.getNewFactory().newLink();
    rolesLink.setHref(getRelativeURIBuilder().path(RolesResource.class).build().toString());
    rolesLink.setRel("Roles");
    atomFeed.addLink(rolesLink);

    User user = Services.getInstance().getUserService().getUserByUsername(userNameWithOrganizationName);
    String userName = user.getUsername();
    String shortName = user.getOrganization().getUniqueShortName();

    Link UserLink = Abdera.getNewFactory().newLink();
    UserLink.setHref(getRelativeURIBuilder().path(OrganizationUserResource.class).build(shortName, userName).toString());
    UserLink.setRel("User");
    atomFeed.addLink(UserLink);


    Link organizationLink = Abdera.getNewFactory().newLink();
    organizationLink.setHref(getRelativeURIBuilder().path(OrganizationResource.class).build(shortName).toString());
    organizationLink.setRel("Organization");
    atomFeed.addLink(organizationLink);


    Link aclAuthLink = Abdera.getNewFactory().newLink();
    aclAuthLink.setHref(getRelativeURIBuilder().path(AuthorizationResource.class).path(
        AuthorizationResource.ACL_AUTHORIZATION_METHOD).build().toString());
    aclAuthLink.setRel("aclAuth");
    atomFeed.addLink(aclAuthLink);

    Link roleAuthLink = Abdera.getNewFactory().newLink();
    roleAuthLink.setHref(getRelativeURIBuilder().path(AuthorizationResource.class).path(
        AuthorizationResource.ROLE_AUTHORIZATION_METHOD).build().toString());
    roleAuthLink.setRel("roleAuth");
    atomFeed.addLink(roleAuthLink);


    Link userGetterLink = Abdera.getNewFactory().newLink();
    userGetterLink.setHref(getRelativeURIBuilder().path(UserLinkResource.class).build().toString());
    userGetterLink.setRel("userGetter");
    atomFeed.addLink(userGetterLink);

    responseBuilder.entity(atomFeed);

    return responseBuilder.build();
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
