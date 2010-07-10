/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;


/**
 *
 * @author russel
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;


@Path("/organizations")
public class OrganizationsResource {

    @GET
    @Produces("")

    public Response get()
    {
        ResponseBuilder responseBuilder = Response.ok();
        Feed atomFeed = Abdera.getNewFactory().newFeed();
        Link organizationsLink = Abdera.getNewFactory().newLink();
        //organizationsLink.setHref(UriBuilder.fromResource());
        organizationsLink.setRel("root");
        atomFeed.addLink(organizationsLink);
        responseBuilder.entity(atomFeed);
        return responseBuilder.build();
    }

}
