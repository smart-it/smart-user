/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.util.rest.atom.server.AbstractResource;
import com.sun.jersey.api.view.Viewable;
import java.util.Date;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
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
    organizationsLink.setHref(getRelativeURIBuilder().path(OrganizationsResource.class).build().toString());
    organizationsLink.setRel("Organizations");
    atomFeed.addLink(organizationsLink);
    responseBuilder.entity(atomFeed);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getHtml()
  {
    ResponseBuilder responseBuilder = Response.ok();


    Viewable view = new Viewable("rootPage.jsp");

    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
