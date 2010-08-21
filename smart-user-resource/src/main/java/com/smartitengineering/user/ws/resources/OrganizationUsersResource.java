/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.User;
import com.sun.jersey.api.view.Viewable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author russel
 */
@Path("/orgs/{uniqueShortName}/users")

public class OrganizationUsersResource extends AbstractResource{

    static final UriBuilder ORGANIZATION_USERS_URI_BUILDER;
    static final UriBuilder ORGANIZATION_USERS_BEFORE_USERNAME_URI_BUILDER;
    static final UriBuilder ORGANIZATION_USERS_AFTER_USERNAME_URI_BUILDER;

    @Context
    private HttpServletRequest servletRequest;

    static{
        ORGANIZATION_USERS_URI_BUILDER = UriBuilder.fromResource(OrganizationUsersResource.class);

        ORGANIZATION_USERS_AFTER_USERNAME_URI_BUILDER = UriBuilder.fromResource(OrganizationUsersResource.class);
        try{
            ORGANIZATION_USERS_AFTER_USERNAME_URI_BUILDER.path(OrganizationUsersResource.class.getMethod("getAfter", String.class));
        }catch(Exception ex){
            ex.printStackTrace();
        }

        ORGANIZATION_USERS_BEFORE_USERNAME_URI_BUILDER = UriBuilder.fromResource(OrganizationUsersResource.class);
        try{
            ORGANIZATION_USERS_BEFORE_USERNAME_URI_BUILDER.path(OrganizationUsersResource.class.getMethod("getBefore", String.class));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    @QueryParam("count")
    private Integer count;
    @PathParam("uniqueShortName")
    private String organizationUniqueShortName;

    public OrganizationUsersResource(@PathParam("uniqueShortName")String organizationUniqueShortName){
        this.organizationUniqueShortName = organizationUniqueShortName;
    }

    
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getHtml(){
        ResponseBuilder responseBuilder = Response.ok();
        Collection<User> users = Services.getInstance().getUserService().getUserByOrganization(organizationUniqueShortName);

        servletRequest.setAttribute("templateContent", "/com/smartitengineering/user/ws/resources/OrganizationUsersResource/userList.jsp");
        Viewable view = new Viewable("/template/template.jsp", users);

//        Viewable view = new Viewable("userList", users, OrganizationUsersResource.class);
        responseBuilder.entity(view);
        return responseBuilder.build();
        
    }
    

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/before/{beforeUserName}")
    public Response getBefore(@PathParam("beforeUserName") String beforeUserName) {
        return get(organizationUniqueShortName, beforeUserName, true);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/before/{beforeUserName}")
    public Response getBeforeHtml(@PathParam("beforeUserName") String beforeUserName){
      ResponseBuilder responseBuilder = Response.ok();
      if(count == null)
            count = 10;
       Collection<User> users = Services.getInstance().getUserService().getUsers(
           null, beforeUserName, true, count);

       servletRequest.setAttribute("templateContent", "/com/smartitengineering/user/ws/resources/OrganizationsResource/organizationList.jsp");
        Viewable view = new Viewable("/template/template.jsp", users);
        responseBuilder.entity(view);
        return responseBuilder.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Path("/after/{afterUserName}")
    public Response getAfter(@PathParam("afterUserName") String afterUserName) {
      return get(organizationUniqueShortName,afterUserName, false);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/after/{afterUserName}")
    public Response getAfterHtml(@PathParam("afterUserName") String afterUserName){

      ResponseBuilder responseBuilder = Response.ok();
      if(count == null)
            count = 10;
       Collection<User> users = Services.getInstance().getUserService().getUsers(
           null, afterUserName, true, count);
       servletRequest.setAttribute("templateContent", "/com/smartitengineering/user/ws/resources/OrganizationsResource/organizationList.jsp");
        Viewable view = new Viewable("/template/template.jsp", users);
        responseBuilder.entity(view);
        return responseBuilder.build();
    }

    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public Response get() {
      return get(organizationUniqueShortName, null, true);
    }

    private Response get(String uniqueOrganizationName, String userName, boolean isBefore){

        if(count == null){
            count = 10;
        }
        ResponseBuilder responseBuilder = Response.ok();
        Feed atomFeed = getFeed(userName, new Date());

        Link parentLink = abderaFactory.newLink();
        parentLink.setHref(UriBuilder.fromResource(RootResource.class).build().toString());
        parentLink.setRel("parent");
        atomFeed.addLink(parentLink);

        Collection<User> users = Services.getInstance().getUserService().getUserByOrganization(uniqueOrganizationName, userName, isBefore, count);        

        if(users != null && !users.isEmpty()){

            MultivaluedMap<String, String> queryParam = uriInfo.getQueryParameters();
            List<User> userList = new ArrayList<User>(users);

            // uri builder for next and previous organizations according to count
            final UriBuilder nextUri = ORGANIZATION_USERS_AFTER_USERNAME_URI_BUILDER.clone();
            final UriBuilder previousUri = ORGANIZATION_USERS_BEFORE_USERNAME_URI_BUILDER.clone();

            // link to the next organizations based on count
            Link nextLink = abderaFactory.newLink();
            nextLink.setRel(Link.REL_NEXT);
            User lastUser = userList.get(userList.size() -1);


            for(String key:queryParam.keySet()){
                final Object[] values = queryParam.get(key).toArray();
                nextUri.queryParam(key, values);
                previousUri.queryParam(key, values);
            }
            nextLink.setHref(nextUri.build(organizationUniqueShortName, lastUser.getUsername()).toString());


            atomFeed.addLink(nextLink);

            /* link to the previous organizations based on count */
            Link prevLink = abderaFactory.newLink();
            prevLink.setRel(Link.REL_PREVIOUS);
            User firstUser = userList.get(0);

            prevLink.setHref(previousUri.build(organizationUniqueShortName, firstUser.getUsername()).toString());
            atomFeed.addLink(prevLink);

            for(User user: users){

                Entry userEntry = abderaFactory.newEntry();

                userEntry.setId(user.getUsername());
                userEntry.setTitle(user.getUsername());
                userEntry.setSummary(user.getUsername());
                userEntry.setUpdated(user.getLastModifiedDate());

                // setting link to the each individual user
                Link userLink = abderaFactory.newLink();
                userLink.setHref(OrganizationUserResource.USER_URI_BUILDER.clone().build(organizationUniqueShortName, user.getUsername()).toString());
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
    public Response post(User user){

        ResponseBuilder responseBuilder;
        try{
            if(user.getRoleIDs() != null){
                Services.getInstance().getRoleService().populateRole(user);
            }
            if(user.getPrivilegeIDs() != null){
                Services.getInstance().getPrivilegeService().populatePrivilege(user);
            }
            if(user.getParentOrganizationID() == null){
                throw new Exception("No organization found");
            }
            Services.getInstance().getOrganizationService().populateOrganization(user);
            Services.getInstance().getUserService().save(user);
            responseBuilder = Response.status(Status.CREATED);
        }
        catch(Exception ex){
            responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
            ex.printStackTrace();
        }
        return responseBuilder.build();
    }

    private User getObjectFromContent(String message){

        Map<String, String> keyValueMap = new HashMap<String, String>();

        String[] keyValuePairs = message.split("&");

        for(int i=0; i<keyValuePairs.length; i++){

          String[] keyValuePair = keyValuePairs[i].split("=");
          keyValueMap.put(keyValuePair[0], keyValuePair[1]);
        }

        User newUser = new User();

        if(keyValueMap.get("id") != null){
          newUser.setId(Integer.valueOf(keyValueMap.get("id")));
        }

        if(keyValueMap.get("userName") != null){
          newUser.setUsername(keyValueMap.get("userName"));
        }
        if(keyValueMap.get("password") != null){
          newUser.setPassword(keyValueMap.get("password"));
        }

        

        Organization parentOrg = Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(organizationUniqueShortName);

          if(parentOrg != null)
            newUser.setOrganization(parentOrg);
        



        

        return newUser;
    }

    @POST
    public Response post(@HeaderParam("Content-type")String contentType, String message){
      ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);

      if(StringUtils.isBlank(message)){
        responseBuilder = Response.status(Status.BAD_REQUEST);
        responseBuilder.build();
      }

      final boolean isHtmlPost;
      if (StringUtils.isBlank(contentType)) {
        contentType = MediaType.APPLICATION_OCTET_STREAM;
        isHtmlPost = false;
      }
      else if (contentType.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
        contentType = MediaType.APPLICATION_OCTET_STREAM;
        isHtmlPost = true;
        try {
          //Will search for the first '=' if not found will take the whole string
          final int startIndex = 0;//message.indexOf("=") + 1;
          //Consider the first '=' as the start of a value point and take rest as value
          final String realMsg = message.substring(startIndex);
          //Decode the message to ignore the form encodings and make them human readable
          message = URLDecoder.decode(realMsg, "UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
          ex.printStackTrace();
        }
      }
      else {
        contentType = contentType;
        isHtmlPost = false;
      }

      if(isHtmlPost){

        User newUser = getObjectFromContent(message);
        Services.getInstance().getUserService().save(newUser);


    }
      return responseBuilder.build();
  }

}
