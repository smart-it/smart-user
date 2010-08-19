/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.User;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import org.apache.commons.lang.StringUtils;

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
//  @FormParam("user")
//  private String userName;
//  @FormParam("password")
//  private String password;

  public LoginResource() {
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {

    System.out.println("Inside Login Resource");
    ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);

    Feed atomFeed = getFeed("Login Resource", new Date());

        Link organizationsLink = Abdera.getNewFactory().newLink();
        organizationsLink.setHref(OrganizationsResource.ORGANIZATION_URI_BUILDER.build().toString());
        organizationsLink.setRel("Organizations");
        atomFeed.addLink(organizationsLink);

        User user = Services.getInstance().getUserService().getUserByUsername(userNameWithOrganizationName);
        String userName = user.getUsername();
        String shortName = user.getOrganization().getUniqueShortName();

        Link UserLink = Abdera.getNewFactory().newLink();
        UserLink.setHref(OrganizationUserResource.USER_URI_BUILDER.build(shortName,userName).toString());
        UserLink.setRel("User");
        atomFeed.addLink(UserLink);


        Link organizationLink = Abdera.getNewFactory().newLink();
        organizationLink.setHref(OrganizationResource.ORGANIZATION_URI_BUILDER.build(shortName).toString());
        organizationLink.setRel("Organization");
        atomFeed.addLink(organizationLink);

        Link usersLink = Abdera.getNewFactory().newLink();
        usersLink.setHref(OrganizationUsersResource.ORGANIZATION_USERS_URI_BUILDER.build(shortName).toString());
        usersLink.setRel("Users");
        atomFeed.addLink(usersLink);

        responseBuilder.entity(atomFeed);

        return responseBuilder.build();
//    if (StringUtils.isBlank(message)) {
//      responseBuilder = Response.status(Status.BAD_REQUEST);
//      return responseBuilder.build();
//    }

//    final boolean isHtmlPost;
//    if (StringUtils.isBlank(contentType)) {
//      contentType = MediaType.APPLICATION_OCTET_STREAM;
//      isHtmlPost = false;
//    }
//    else if (contentType.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
//      contentType = MediaType.APPLICATION_OCTET_STREAM;
//      isHtmlPost = true;
////      try {
////        //Will search for the first '=' if not found will take the whole string
////        final int startIndex = 0;//message.indexOf("=") + 1;
////        //Consider the first '=' as the start of a value point and take rest as value
////        final String realMsg = message.substring(startIndex);
////        //Decode the message to ignore the form encodings and make them human readable
//
////        message = URLDecoder.decode(realMsg, "UTF-8");
//
////      }
////      catch (UnsupportedEncodingException ex) {
////        ex.printStackTrace();
////      }
//    }
//    else {
//      contentType = contentType;
//      isHtmlPost = false;
//    }

    //if (isHtmlPost) {

//      String username;
//      String password;
//
//      Map<String, String> keyValueMap = new HashMap<String, String>();
//      String[] keyValuePairs = message.split("&");
//
//      for (int i = 0; i < keyValuePairs.length; i++) {
//        String[] keyValuePair = keyValuePairs[i].split("=");
//        keyValueMap.put(keyValuePair[0], keyValuePair[1]);
//      }
//
//      if (keyValueMap.get("username") != null) {
//        username = keyValueMap.get("username");
//      }
//      else {
//        username = "";
//      }
//      if (keyValueMap.get("password") != null) {
//        password = keyValueMap.get("password");
//      }
//      else {
//        password = "";
//      }

//      if (Services.getInstance().getAuthorizationService().login(userName, password)) {
//      if (true) {
//
//        return responseBuilder.status(Status.OK).build();
//      }
//      else {
//        return responseBuilder.status(Status.UNAUTHORIZED).build();
//      }
//
//    }
//    return responseBuilder.build();

  }
}
