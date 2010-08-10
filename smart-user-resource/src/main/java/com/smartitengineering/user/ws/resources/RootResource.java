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
    Link booksLink = Abdera.getNewFactory().newLink();
    //booksLink.setHref(UriBuilder.fromResource(BooksResource.class).build().toString());
    booksLink.setHref("test1");
    booksLink.setRel("books");
    atomFeed.addLink(booksLink);

    Link authorsLink = Abdera.getNewFactory().newLink();
    authorsLink.setHref(UriBuilder.fromResource(OrganizationsResource.class).build().toString());
    authorsLink.setHref("Organizations");
    authorsLink.setRel("Organizations");
    atomFeed.addLink(authorsLink);
    responseBuilder.entity(atomFeed);
    return responseBuilder.build();
  }
}
