/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Address;
import com.smartitengineering.user.domain.BasicIdentity;
import com.smartitengineering.user.domain.Name;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.GeoLocation;
import com.smartitengineering.user.domain.UserPerson;
import com.sun.jersey.api.view.Viewable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
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
public class OrganizationUsersResource extends AbstractResource {


  static final UriBuilder ORGANIZATION_USERS_URI_BUILDER;
  static final UriBuilder ORGANIZATION_USERS_BEFORE_USERNAME_URI_BUILDER;
  static final UriBuilder ORGANIZATION_USERS_AFTER_USERNAME_URI_BUILDER;

  static {
    ORGANIZATION_USERS_URI_BUILDER = UriBuilder.fromResource(OrganizationUsersResource.class);

    ORGANIZATION_USERS_AFTER_USERNAME_URI_BUILDER = UriBuilder.fromResource(OrganizationUsersResource.class);
    try {
      ORGANIZATION_USERS_AFTER_USERNAME_URI_BUILDER.path(OrganizationUsersResource.class.getMethod("getAfter",
                                                                                                   String.class));

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    ORGANIZATION_USERS_BEFORE_USERNAME_URI_BUILDER = UriBuilder.fromResource(OrganizationUsersResource.class);
    try {
      ORGANIZATION_USERS_BEFORE_USERNAME_URI_BUILDER.path(OrganizationUsersResource.class.getMethod("getBefore",
                                                                                                    String.class));
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  @QueryParam("count")
  private Integer count;
  @PathParam("uniqueShortName")
  private String organizationUniqueShortName;
  @Context
  private HttpServletRequest servletRequest;

  public OrganizationUsersResource(@PathParam("uniqueShortName") String organizationUniqueShortName) {
    this.organizationUniqueShortName = organizationUniqueShortName;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getHtml() {
    ResponseBuilder responseBuilder = Response.ok();    

    Collection<User> users = Services.getInstance().getUserService().getUserByOrganization(organizationUniqueShortName,
                                                                  organizationUniqueShortName, true, count);

    servletRequest.setAttribute("templateContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationsResource/organizationList.jsp");
    Viewable view = new Viewable("/template/template.jsp", users);
    
    responseBuilder.entity(view);
    return responseBuilder.build();

  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/frags")
  public Response getHtmlFrags() {
    ResponseBuilder responseBuilder = Response.ok();
    Collection<User> users = Services.getInstance().getUserService().getUserByOrganization(organizationUniqueShortName);

    Viewable view = new Viewable("userList", users, OrganizationUsersResource.class);
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
  public Response getBeforeHtml(@PathParam("beforeUserName") String beforeUserName) {
    ResponseBuilder responseBuilder = Response.ok();
    if (count == null) {
      count = 10;
    }
    Collection<User> users = Services.getInstance().getUserService().getUsers(
        null, beforeUserName, true, count);

    servletRequest.setAttribute("templateContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationsResource/organizationList.jsp");
    Viewable view = new Viewable("/template/template.jsp", users);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/before/{beforeUserName}/frags")
  public Response getBeforeHtmlFrags(@PathParam("beforeUserName") String beforeUserName) {
    ResponseBuilder responseBuilder = Response.ok();
    if (count == null) {
      count = 10;
    }
    Collection<User> users = Services.getInstance().getUserService().getUsers(
        null, beforeUserName, true, count);

    Viewable view = new Viewable("userFrags.jsp", users);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }


  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Path("/after/{afterUserName}")
  public Response getAfter(@PathParam("afterUserName") String afterUserName) {
    return get(organizationUniqueShortName, afterUserName, false);
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/after/{afterUserName}")
  public Response getAfterHtml(@PathParam("afterUserName") String afterUserName) {

    ResponseBuilder responseBuilder = Response.ok();
    if (count == null) {
      count = 10;
    }
    Collection<User> users = Services.getInstance().getUserService().getUsers(
        null, afterUserName, true, count);
    servletRequest.setAttribute("templateContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationsResource/organizationList.jsp");
    Viewable view = new Viewable("/template/template.jsp", users);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/after/{afterUserName}/frags")
  public Response getAfterHtmlFrags(@PathParam("afterUserName") String afterUserName) {

    ResponseBuilder responseBuilder = Response.ok();
    if (count == null) {
      count = 10;
    }
    Collection<User> users = Services.getInstance().getUserService().getUsers(
        null, afterUserName, true, count);    
    Viewable view = new Viewable("userFrags.jsp", users);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    return get(organizationUniqueShortName, null, true);
  }

  private Response get(String uniqueOrganizationName, String userName, boolean isBefore) {

    if (count == null) {
      count = 10;
    }
    ResponseBuilder responseBuilder = Response.ok();
    Feed atomFeed = getFeed(userName, new Date());

    Link parentLink = abderaFactory.newLink();
    parentLink.setHref(UriBuilder.fromResource(RootResource.class).build().toString());
    parentLink.setRel("parent");
    atomFeed.addLink(parentLink);

    Collection<User> users = Services.getInstance().getUserService().getUserByOrganization(uniqueOrganizationName,
                                                                                           userName, isBefore, count);

    if (users != null && !users.isEmpty()) {

      MultivaluedMap<String, String> queryParam = uriInfo.getQueryParameters();
      List<User> userList = new ArrayList<User>(users);

      // uri builder for next and previous organizations according to count
      final UriBuilder nextUri = ORGANIZATION_USERS_AFTER_USERNAME_URI_BUILDER.clone();
      final UriBuilder previousUri = ORGANIZATION_USERS_BEFORE_USERNAME_URI_BUILDER.clone();

      // link to the next organizations based on count
      Link nextLink = abderaFactory.newLink();
      nextLink.setRel(Link.REL_NEXT);
      User lastUser = userList.get(userList.size() - 1);


      for (String key : queryParam.keySet()) {
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

      for (User user : users) {

        Entry userEntry = abderaFactory.newEntry();

        userEntry.setId(user.getUsername());
        userEntry.setTitle(user.getUsername());
        userEntry.setSummary(user.getUsername());
        userEntry.setUpdated(user.getLastModifiedDate());

        // setting link to the each individual user
        Link userLink = abderaFactory.newLink();
        userLink.setHref(OrganizationUserResource.USER_URI_BUILDER.clone().build(organizationUniqueShortName, user.
            getUsername()).toString());
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
  public Response post(UserPerson userPerson) {


    ResponseBuilder responseBuilder;
    User user = userPerson.getUser();
    try {
      if (user.getRoleIDs() != null) {
        Services.getInstance().getRoleService().populateRole(user);
      }
      if (user.getPrivilegeIDs() != null) {
        Services.getInstance().getPrivilegeService().populatePrivilege(user);
      }
      if (user.getParentOrganizationID() == null) {
        throw new Exception("No organization found");
      }
      Services.getInstance().getOrganizationService().populateOrganization(user);
      Services.getInstance().getUserPersonService().create(userPerson);
      responseBuilder = Response.status(Status.CREATED);
    }
    catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
      ex.printStackTrace();
    }
    return responseBuilder.build();
  }


  private UserPerson getObjectFromContent(String message) {


    Map<String, String> keyValueMap = new HashMap<String, String>();

    String[] keyValuePairs = message.split("&");

    for (int i = 0; i < keyValuePairs.length; i++) {

      String[] keyValuePair = keyValuePairs[i].split("=");
      keyValueMap.put(keyValuePair[0], keyValuePair[1]);
    }

    User newUser = new User();

    if (keyValueMap.get("id") != null) {
      newUser.setId(Integer.valueOf(keyValueMap.get("id")));
    }

    if (keyValueMap.get("userName") != null) {
      newUser.setUsername(keyValueMap.get("userName"));
    }
    if (keyValueMap.get("password") != null) {
      newUser.setPassword(keyValueMap.get("password"));
    }

    if (keyValueMap.get("uniqueShortName") != null) {
      Organization parentOrg = Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(keyValueMap.
          get("uniqueShortName"));

      if (parentOrg != null) {
        newUser.setOrganization(parentOrg);

      }
    }

    Person person = new Person();
    BasicIdentity self = new BasicIdentity();
    Name selfName = new Name();

    if (keyValueMap.get("firstName") != null) {
      selfName.setFirstName(keyValueMap.get("firstName"));
    }
    if (keyValueMap.get("lastName") != null) {
      selfName.setFirstName(keyValueMap.get("lastName"));
    }
    if (keyValueMap.get("middleInitial") != null) {
      selfName.setMiddleInitial(keyValueMap.get("middleInitial"));
    }
    self.setName(selfName);

    if (keyValueMap.get("nationalID") != null) {
      self.setNationalID(keyValueMap.get("nationalID"));
    }
    person.setSelf(self);


    BasicIdentity spouse = new BasicIdentity();
    Name spouseName = new Name();

    if (keyValueMap.get("spouseFirstName") != null) {
      spouseName.setFirstName(keyValueMap.get("spouseFirstName"));
    }
    if (keyValueMap.get("spouseLastName") != null) {
      spouseName.setFirstName(keyValueMap.get("spouseLastName"));
    }
    if (keyValueMap.get("spouseMiddleInitial") != null) {
      spouseName.setMiddleInitial(keyValueMap.get("spouseMiddleInitial"));
    }
    spouse.setName(spouseName);

    if (keyValueMap.get("spouseNationalID") != null) {
      spouse.setNationalID(keyValueMap.get("spouseNationalID"));
    }
    person.setSpouse(spouse);

    BasicIdentity mother = new BasicIdentity();
    Name motherName = new Name();

    if (keyValueMap.get("motherFirstName") != null) {
      motherName.setFirstName(keyValueMap.get("motherFirstName"));
    }
    if (keyValueMap.get("motherLastName") != null) {
      motherName.setFirstName(keyValueMap.get("motherLastName"));
    }
    if (keyValueMap.get("motherMiddleInitial") != null) {
      motherName.setMiddleInitial(keyValueMap.get("motherMiddleInitial"));
    }
    mother.setName(motherName);

    if (keyValueMap.get("motherNationalID") != null) {
      mother.setNationalID(keyValueMap.get("motherNationalID"));
    }
    person.setMother(mother);

    BasicIdentity father = new BasicIdentity();
    Name fatherName = new Name();

    if (keyValueMap.get("fatherFirstName") != null) {
      fatherName.setFirstName(keyValueMap.get("fatherFirstName"));
    }
    if (keyValueMap.get("fatherLastName") != null) {
      fatherName.setFirstName(keyValueMap.get("fatherLastName"));
    }
    if (keyValueMap.get("fatherMiddleInitial") != null) {
      fatherName.setMiddleInitial(keyValueMap.get("fatherMiddleInitial"));
    }
    father.setName(fatherName);

    if (keyValueMap.get("fatherNationalID") != null) {
      father.setNationalID(keyValueMap.get("fatherNationalID"));
    }
    person.setFather(father);

    Address address = new Address();
    GeoLocation geoLocation = new GeoLocation();


    if (keyValueMap.get("longitude") != null) {
      Double longitude = Double.parseDouble(keyValueMap.get("longitude"));
      geoLocation.setLongitude(longitude);
    }

    if (keyValueMap.get("latitude") != null) {
      Double latitude = Double.parseDouble(keyValueMap.get("latitude"));
      geoLocation.setLatitude(latitude);
    }

    address.setGeoLocation(geoLocation);

    if (keyValueMap.get("city") != null) {
      address.setCity(keyValueMap.get("city"));
    }

    if (keyValueMap.get("country") != null) {
      address.setCountry(keyValueMap.get("country"));
    }

    if (keyValueMap.get("state") != null) {
      address.setState(keyValueMap.get("state"));
    }
    if (keyValueMap.get("zip") != null) {
      address.setZip(keyValueMap.get("zip"));
    }
    person.setAddress(address);

    if (keyValueMap.get("birthDate") != null) {
      String dateString = keyValueMap.get("birthDate");
      SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
      try {
        Date birthDate = format.parse(dateString);
      }
      catch (Exception ex) {
      }
    }

    if (keyValueMap.get("primaryEmail") != null) {
      person.setPrimaryEmail(keyValueMap.get("primaryEmail"));
    }

    if (keyValueMap.get("phoneNumber") != null) {
      person.setPhoneNumber(keyValueMap.get("phoneNumber"));
    }
    if (keyValueMap.get("secondaryEmail") != null) {
      person.setSecondaryEmail(keyValueMap.get("secondaryEmail"));
    }
    if (keyValueMap.get("faxNumber") != null) {
      person.setFaxNumber(keyValueMap.get("faxNumber"));
    }
    if (keyValueMap.get("cellPhoneNumber") != null) {
      person.setCellPhoneNumber(keyValueMap.get("cellPhoneNumber"));
    }

    UserPerson userPerson = new UserPerson();
    userPerson.setUser(newUser);
    userPerson.setPerson(person);

    return userPerson;


  }

  @POST
  public Response post(@HeaderParam("Content-type") String contentType, String message) {
    ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);

    if (StringUtils.isBlank(message)) {
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

    if (isHtmlPost) {
      UserPerson userPerson = getObjectFromContent(message);
      if (userPerson.getPerson().isValid()) {
        Services.getInstance().getUserPersonService().create(userPerson);
      }
      else {
        Services.getInstance().getUserService().save(userPerson.getUser());
      }

    }
    return responseBuilder.build();
  }

}
