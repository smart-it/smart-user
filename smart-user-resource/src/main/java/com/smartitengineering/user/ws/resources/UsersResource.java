/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.impl.Services;
import com.smartitengineering.user.domain.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;


/**
 *
 * @author russel
 */
@Path("/users")
public class UsersResource extends AbstractResource{

    @QueryParam("username")
    private String userName;
    @QueryParam("count")
    private Integer count;

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get(){
        return get(null, true);
    }


    public Response get(String userName, boolean isBefore){

        if(count == null){
            count = 10;
        }
        ResponseBuilder responseBuilder = Response.ok();
        Feed atomFeed = getFeed("users", new Date());
        Link usersLink = abderaFactory.newLink();
        usersLink.setHref(UriBuilder.fromResource(this.getClass()).build().toString());
        usersLink.setRel("root");
        atomFeed.addLink(usersLink);

        Collection<User> users = Services.getInstance().getUserService().getAllUser();

        if(users != null && !users.isEmpty()){

            MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
            List<User> userList = new ArrayList<User>(users);

            Link nextLink = abderaFactory.newLink();
            nextLink.setRel(Link.REL_PREVIOUS);
            
        }

        return responseBuilder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(User user){

        ResponseBuilder responseBuilder;
        try{
            Services.getInstance().getUserService().save(user);
            responseBuilder = Response.status(Status.OK);
        }
        catch(Exception ex){
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
            ex.printStackTrace();
        }
        return responseBuilder.build();
    }
}
