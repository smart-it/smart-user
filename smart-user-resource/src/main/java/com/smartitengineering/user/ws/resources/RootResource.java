/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import java.util.Date;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

/**
 *
 * @author imyousuf
 */
@Path("/")
public class RootResource extends AbstractResource {

  private static final Date INIT_DATE = new Date();

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    ResponseBuilder responseBuilder = Response.ok();
    Feed atomFeed = getFeed("ROA Demo", INIT_DATE);
    Link loginLink = Abdera.getNewFactory().newLink();
    //loginLink.setHref(UriBuilder.fromResource(BooksResource.class).build().toString());
    loginLink.setHref(LoginResource.LOGIN_URI_BUILDER.build().toString());
    loginLink.setRel("Login");
    atomFeed.addLink(loginLink);
    Link organizationsLink = Abdera.getNewFactory().newLink();    
    organizationsLink.setHref(OrganizationsResource.ORGANIZATION_URI_BUILDER.build().toString());
    organizationsLink.setRel("Organizations");
    atomFeed.addLink(organizationsLink);
    responseBuilder.entity(atomFeed);
    return responseBuilder.build();
  }
}
