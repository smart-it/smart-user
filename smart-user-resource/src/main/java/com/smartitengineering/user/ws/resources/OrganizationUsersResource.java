/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.service.Services;
import com.smartitengineering.user.domain.Address;
import com.smartitengineering.user.domain.BasicIdentity;
import com.smartitengineering.user.domain.Name;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.GeoLocation;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.util.rest.atom.server.AbstractResource;
import com.sun.jersey.api.view.Viewable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
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
import javax.ws.rs.DefaultValue;
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
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author russel
 */
@Path("/orgs/sn/{uniqueShortName}/users")
public class OrganizationUsersResource extends AbstractResource {

  static final Method ORGANIZATION_USERS_BEFORE_USERNAME_METHOD;
  static final Method ORGANIZATION_USERS_AFTER_USERNAME_METHOD;

  static {
    try {
      ORGANIZATION_USERS_AFTER_USERNAME_METHOD = OrganizationUsersResource.class.getMethod("getAfter", String.class);
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }
    try {
      ORGANIZATION_USERS_BEFORE_USERNAME_METHOD = OrganizationUsersResource.class.getMethod("getBefore", String.class);
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }
  }
  private Organization organization;
  @DefaultValue("10")
  @QueryParam("count")
  private Integer count;
  private String organizationUniqueShortName;
  @Context
  private HttpServletRequest servletRequest;

  public OrganizationUsersResource(@PathParam("uniqueShortName") String orgUniqueShortName) {
    this.organizationUniqueShortName = orgUniqueShortName;
    organization = getOrganization();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getHtml() {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null) {
      responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }

    Collection<UserPerson> users = Services.getInstance().getUserPersonService().getByOrganization(
        organizationUniqueShortName, null, false, count);

    servletRequest.setAttribute("orgInitial", organizationUniqueShortName);
    servletRequest.setAttribute("templateHeadContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationUsersResource/userListHeader.jsp");
    servletRequest.setAttribute("templateContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationUsersResource/userList.jsp");
    Viewable view = new Viewable("/template/template.jsp", users);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/frags")
  public Response getHtmlFrags() {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null) {
      responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    Collection<UserPerson> userPersons = Services.getInstance().getUserPersonService().getByOrganization(
        organizationUniqueShortName, null, false, count);
    Viewable view = new Viewable("userFrags.jsp", userPersons, OrganizationUsersResource.class);
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
    if (organization == null) {
      responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    Collection<UserPerson> userPersons = Services.getInstance().getUserPersonService().getByOrganization(
        organizationUniqueShortName, beforeUserName, true, count);

    servletRequest.setAttribute("templateContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationUsersResource/userList.jsp");
    Viewable view = new Viewable("/template/template.jsp", userPersons);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/before/{beforeUserName}/frags")
  public Response getBeforeHtmlFrags(@PathParam("beforeUserName") String beforeUserName) {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null) {
      responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    Collection<UserPerson> userPersons = Services.getInstance().getUserPersonService().getByOrganization(
        organizationUniqueShortName, beforeUserName, true, count);
    Viewable view = new Viewable("userFrags.jsp", userPersons);
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
    if (organization == null) {
      responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    Collection<UserPerson> userPersons = Services.getInstance().getUserPersonService().getByOrganization(
        organizationUniqueShortName, afterUserName, false, count);
    servletRequest.setAttribute("templateContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationUsersResource/userList.jsp");
    Viewable view = new Viewable("/template/template.jsp", userPersons);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/after/{afterUserName}/frags")
  public Response getAfterHtmlFrags(@PathParam("afterUserName") String afterUserName) {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null) {
      responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    Collection<UserPerson> userPersons = Services.getInstance().getUserPersonService().getByOrganization(
        organizationUniqueShortName, afterUserName, false, count);
    Viewable view = new Viewable("userFrags.jsp", userPersons);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    return get(organizationUniqueShortName, null, true);
  }

  private Response get(String uniqueOrganizationName, String userNameOption, boolean isBefore) {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null) {
      responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    Feed atomFeed = getFeed(userNameOption, new Date());
    Link parentLink = getAbderaFactory().newLink();
    parentLink.setHref(UriBuilder.fromResource(RootResource.class).build().toString());
    parentLink.setRel("parent");
    atomFeed.addLink(parentLink);
    Collection<UserPerson> userPersons = Services.getInstance().getUserPersonService().getByOrganization(
        organizationUniqueShortName, userNameOption, isBefore, count);

    if (userPersons != null && !userPersons.isEmpty()) {

      MultivaluedMap<String, String> queryParam = getUriInfo().getQueryParameters();
      List<UserPerson> userPersonList = new ArrayList<UserPerson>(userPersons);

      // uri builder for next and previous organizations according to count
      final UriBuilder nextUri = getRelativeURIBuilder().path(OrganizationUsersResource.class).path(
          ORGANIZATION_USERS_AFTER_USERNAME_METHOD);
      final UriBuilder previousUri = getRelativeURIBuilder().path(OrganizationUsersResource.class).path(
          ORGANIZATION_USERS_BEFORE_USERNAME_METHOD);

      // link to the next organizations based on count
      Link nextLink = getAbderaFactory().newLink();
      nextLink.setRel(Link.REL_NEXT);
      //User lastUser = userList.get(userList.size() - 1);
      UserPerson lastUserPerson = userPersonList.get(userPersonList.size() - 1);

      for (String key : queryParam.keySet()) {
        final Object[] values = queryParam.get(key).toArray();
        nextUri.queryParam(key, values);
        previousUri.queryParam(key, values);
      }
      nextLink.setHref(nextUri.build(organizationUniqueShortName, lastUserPerson.getUser().getUsername()).toString());


      atomFeed.addLink(nextLink);

      /* link to the previous organizations based on count */
      Link prevLink = getAbderaFactory().newLink();
      prevLink.setRel(Link.REL_PREVIOUS);
      //User firstUser = userList.get(0);
      UserPerson firstUserPerson = userPersonList.get(0);

      prevLink.setHref(
          previousUri.build(organizationUniqueShortName, firstUserPerson.getUser().getUsername()).toString());
      atomFeed.addLink(prevLink);

      //for (User user : users) {
      for (UserPerson userPerson : userPersons) {

        Entry userEntry = getAbderaFactory().newEntry();

        userEntry.setId(userPerson.getUser().getUsername());
        userEntry.setTitle(userPerson.getUser().getUsername());
        userEntry.setSummary(userPerson.getUser().getUsername());
        userEntry.setUpdated(userPerson.getUser().getLastModifiedDate());

        // setting link to the each individual user
        Link userLink = getAbderaFactory().newLink();
        userLink.setHref(getRelativeURIBuilder().path(OrganizationUserResource.class).build(organizationUniqueShortName, userPerson.
            getUser().getUsername()).toString());
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
    if (organization == null) {
      responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }
    userPerson.getUser().setOrganization(organization);

    try {
      Services.getInstance().getUserPersonService().create(userPerson);
      responseBuilder = Response.status(Status.CREATED);
      responseBuilder.location(getAbsoluteURIBuilder().path(OrganizationUserResource.class).build(
          organizationUniqueShortName, userPerson.getUser().getUsername()));
    }
    catch (Exception ex) {
      ex.printStackTrace();
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private UserPerson getObjectFromContent(String message) {

    Map<String, String> keyValueMap = new HashMap<String, String>();
    String[] keyValuePairs = message.split("&");
    for (int i = 0; i < keyValuePairs.length; i++) {
      String[] keyValuePair = keyValuePairs[i].split("=");
      int l = keyValuePair.length;
      if (l == 1) {
        keyValueMap.put(keyValuePair[0], "");
        //keyValuePair[1] = new String("");
      }
      else {
        keyValueMap.put(keyValuePair[0], keyValuePair[1]);
      }
    }
    User newUser = new User();
    if (keyValueMap.get("id") != null) {
      newUser.setId(NumberUtils.toLong(keyValueMap.get("id")));
    }
    if (keyValueMap.get("userName") != null) {
      newUser.setUsername(keyValueMap.get("userName"));
    }
    if (keyValueMap.get("password") != null) {
      newUser.setPassword(keyValueMap.get("password"));
    }
    newUser.setOrganization(organization);

    Person person = new Person();
    BasicIdentity self = new BasicIdentity();
    Name selfName = new Name();
    boolean isValid = false;

    if (keyValueMap.get("firstName") != null) {
      isValid = true;
      selfName.setFirstName(keyValueMap.get("firstName"));
    }
    if (keyValueMap.get("lastName") != null) {
      isValid = true;
      selfName.setLastName(keyValueMap.get("lastName"));
    }
    if (keyValueMap.get("middleInitial") != null) {
      isValid = true;
      selfName.setMiddleInitial(keyValueMap.get("middleInitial"));
    }
    self.setName(selfName);

    if (keyValueMap.get("nationalID") != null) {
      isValid = true;
      self.setNationalID(keyValueMap.get("nationalID"));
    }
    if (isValid == true) {
      person.setSelf(self);
    }


    BasicIdentity spouse = new BasicIdentity();
    Name spouseName = new Name();
    isValid = false;

    if (keyValueMap.get("spouseFirstName") != null) {
      isValid = true;
      spouseName.setFirstName(keyValueMap.get("spouseFirstName"));
    }
    if (keyValueMap.get("spouseLastName") != null) {
      isValid = true;
      spouseName.setLastName(keyValueMap.get("spouseLastName"));
    }
    if (keyValueMap.get("spouseMiddleInitial") != null) {
      isValid = true;
      spouseName.setMiddleInitial(keyValueMap.get("spouseMiddleInitial"));
    }
    spouse.setName(spouseName);

    if (keyValueMap.get("spouseNationalID") != null) {
      isValid = true;
      spouse.setNationalID(keyValueMap.get("spouseNationalID"));
    }

    if (isValid == true) {
      person.setSpouse(spouse);
    }


    BasicIdentity mother = new BasicIdentity();
    Name motherName = new Name();
    isValid = false;

    if (keyValueMap.get("motherFirstName") != null) {
      isValid = true;
      motherName.setFirstName(keyValueMap.get("motherFirstName"));
    }
    if (keyValueMap.get("motherLastName") != null) {
      isValid = true;
      motherName.setLastName(keyValueMap.get("motherLastName"));
    }
    if (keyValueMap.get("motherMiddleInitial") != null) {
      isValid = true;
      motherName.setMiddleInitial(keyValueMap.get("motherMiddleInitial"));
    }
    mother.setName(motherName);

    if (keyValueMap.get("motherNationalID") != null) {
      isValid = true;
      mother.setNationalID(keyValueMap.get("motherNationalID"));
    }
    if (isValid == true) {
      person.setMother(mother);
    }

    BasicIdentity father = new BasicIdentity();
    Name fatherName = new Name();
    isValid = false;

    if (keyValueMap.get("fatherFirstName") != null) {
      isValid = true;
      fatherName.setFirstName(keyValueMap.get("fatherFirstName"));
    }
    if (keyValueMap.get("fatherLastName") != null) {
      isValid = true;
      fatherName.setLastName(keyValueMap.get("fatherLastName"));
    }
    if (keyValueMap.get("fatherMiddleInitial") != null) {
      isValid = true;
      fatherName.setMiddleInitial(keyValueMap.get("fatherMiddleInitial"));
    }
    father.setName(fatherName);

    if (keyValueMap.get("fatherNationalID") != null) {
      isValid = true;
      father.setNationalID(keyValueMap.get("fatherNationalID"));
    }
    if (isValid == true) {
      person.setFather(father);
    }

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
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response post(
      @HeaderParam("Content-type") String contentType, String message) {
    ResponseBuilder responseBuilder = Response.status(Status.OK);
    if (organization == null) {
      responseBuilder = Response.status(Status.NOT_FOUND);
      return responseBuilder.build();
    }

    if (StringUtils.isBlank(message)) {
      responseBuilder = Response.status(Status.BAD_REQUEST);
      responseBuilder.build();
    }



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

    UserPerson userPerson = null;

    userPerson = getObjectFromContent(message);
    try {
      if (userPerson.getPerson().isValid()) {
        Services.getInstance().getUserPersonService().create(userPerson);
      }
      else {
        Services.getInstance().getUserService().save(userPerson.getUser());
      }
      responseBuilder.status(Status.SEE_OTHER);
      responseBuilder.location(getRelativeURIBuilder().path(OrganizationUserResource.class).build(organization.
          getUniqueShortName(), userPerson.getUser().getUsername()));
    }
    catch (Exception ex) {
      responseBuilder.status(Status.INTERNAL_SERVER_ERROR);
    }

    return responseBuilder.build();
  }

  public Organization getOrganization() {
    return Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(organizationUniqueShortName);
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
