/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import java.util.Date;
import java.util.StringTokenizer;
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
@Path("/ulg")
public class UserLinkResource extends AbstractResource {

  static final UriBuilder USER_LINK_GETTER_URI_BUILDER = UriBuilder.fromResource(UserLinkResource.class);

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get(@QueryParam("username") final String usernameWithOrganizationName) {

    ResponseBuilder responseBuilder = Response.status(Status.OK);
    Feed atomFeed = getFeed("User Link Getter Resource", new Date());
    
    String username;
    String organizationName;

    StringTokenizer tokenizer = new StringTokenizer(usernameWithOrganizationName, "@");
    if (tokenizer.hasMoreTokens()) {
      username = tokenizer.nextToken();
    }
    else {
      username = "";
    }
    if (tokenizer.hasMoreTokens()) {
      organizationName = tokenizer.nextToken();
    }
    else {
      organizationName = "";
    }

    Link userGetterLink = Abdera.getNewFactory().newLink();
    userGetterLink.setHref(OrganizationUserResource.USER_URI_BUILDER.build(organizationName, username).toString());
    userGetterLink.setRel("userLink");
    atomFeed.addLink(userGetterLink);

    responseBuilder.entity(atomFeed);
    
    return responseBuilder.build();
  }
}
