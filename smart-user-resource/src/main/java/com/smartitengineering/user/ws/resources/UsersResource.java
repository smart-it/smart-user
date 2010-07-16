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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
@Path("/users")
public class UsersResource extends AbstractResource{

    static final UriBuilder USERS_URI_BUILDER;
    static final UriBuilder USERS_AFTER_USERNAME_URI_BUILDER;
    static final UriBuilder USERS_BEFORE_USERNAME_URI_BUILDER;

    static{
        USERS_URI_BUILDER = UriBuilder.fromResource(UsersResource.class);

        USERS_AFTER_USERNAME_URI_BUILDER = UriBuilder.fromResource(UsersResource.class);
        try{
            USERS_AFTER_USERNAME_URI_BUILDER.path(UsersResource.class.getMethod("getAfter", String.class));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        USERS_BEFORE_USERNAME_URI_BUILDER = UriBuilder.fromResource(UsersResource.class);
        try{
            USERS_BEFORE_USERNAME_URI_BUILDER.path(UsersResource.class.getMethod("getBefore", String.class));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @QueryParam("username")
    private String userName;
    @QueryParam("count")
    private Integer count;

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/after/{userName}")
    public Response getAfter(@PathParam("userName") String userName){
        return get(userName, false);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/before/{userName}")
    public Response getBefore(@PathParam("userName") String userName){
        return get(userName, true);
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get(){
        return get(null, true);
    }


    private Response get(String userName, boolean isBefore){

        if(count == null){
            count = 10;
        }
        ResponseBuilder responseBuilder = Response.ok();
        Feed atomFeed = getFeed(userName, new Date());

        Link parentLink = abderaFactory.newLink();
        parentLink.setHref(UriBuilder.fromResource(RootResource.class).build().toString());
        parentLink.setRel("parent");
        atomFeed.addLink(parentLink);

              
        Collection<User> users = Services.getInstance().getUserService().getAllUser();

        if(users != null && !users.isEmpty()){

            MultivaluedMap<String, String> queryParam = uriInfo.getQueryParameters();
            List<User> userList = new ArrayList<User>(users);

            // uri builder for next and previous organizations according to count
            final UriBuilder nextUri = USERS_AFTER_USERNAME_URI_BUILDER.clone();
            final UriBuilder previousUri = USERS_BEFORE_USERNAME_URI_BUILDER.clone();

            // link to the next organizations based on count
            Link nextLink = abderaFactory.newLink();
            nextLink.setRel(Link.REL_NEXT);
            User lastUser = userList.get(0);


            for(String key:queryParam.keySet()){
                final Object[] values = queryParam.get(key).toArray();
                nextUri.queryParam(key, values);
                previousUri.queryParam(key, values);
            }
            nextLink.setHref(nextUri.build(lastUser.getUsername()).toString());
            

            atomFeed.addLink(nextLink);

            /* link to the previous organizations based on count */
            Link prevLink = abderaFactory.newLink();
            prevLink.setRel(Link.REL_PREVIOUS);
            User firstUser = userList.get(users.size() - 1);

            prevLink.setHref(previousUri.build(firstUser.getUsername()).toString());            
            atomFeed.addLink(prevLink);

            for(User user: users){

                Entry userEntry = abderaFactory.newEntry();

                userEntry.setId(user.getUsername());
                userEntry.setTitle(user.getUsername());
                userEntry.setSummary(user.getUsername());
                userEntry.setUpdated("Not available");

                // setting link to the each individual user
                Link userLink = abderaFactory.newLink();
                userLink.setHref(UserResource.USER_URI_BUILDER.clone().build(user.getUsername()).toString());
                userLink.setRel(Link.REL_ALTERNATE);
                userLink.setMimeType(MediaType.APPLICATION_ATOM_XML);

                userEntry.addLink(userLink);

                atomFeed.addEntry(userEntry);
            }                       
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
