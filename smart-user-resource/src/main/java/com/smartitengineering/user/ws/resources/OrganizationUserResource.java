/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.service.Services;
import com.smartitengineering.user.domain.Address;
import com.smartitengineering.user.domain.BasicIdentity;
import com.smartitengineering.user.domain.GeoLocation;
import com.smartitengineering.user.domain.Name;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.util.rest.atom.server.AbstractResource;
import com.sun.jersey.api.view.Viewable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilderException;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author russel
 */
@Path("/orgs/sn/{organizationShortName}/users/un/{userName}")
public class OrganizationUserResource extends AbstractResource {

  static final Method USER_CONTENT_METHOD;
  @Context
  private HttpServletRequest servletRequest;

  static {
    try {
      USER_CONTENT_METHOD = OrganizationUserResource.class.getMethod("getUser");
    }
    catch (Exception ex) {
      throw new InstantiationError();

    }
  }
  private String organizationUniqueShortName;
  private String userName;
  private String REL_USER_PRIVILEGES = "privileges";
  private String REL_USER_ROLES = "roles";
  private Organization organization;
  private UserPerson userPerson;

  public OrganizationUserResource(@PathParam("organizationShortName") String organizationShortName, @PathParam(
      "userName") String userName) {
    this.organizationUniqueShortName = organizationShortName;
    this.userName = userName;
    organization = getOrganization();
    userPerson = getUserPerson();
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null || userPerson == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    Feed userFeed = getUserFeed();
    responseBuilder = Response.ok(userFeed);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/content")
  public Response getUser() {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null || userPerson == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    responseBuilder = Response.ok(userPerson);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getHtml() {
    ResponseBuilder responseBuilder = Response.ok();
    if (organization == null || userPerson == null) {
      return responseBuilder.status(Status.NOT_FOUND).build();
    }

    servletRequest.setAttribute("orgInitial", organizationUniqueShortName);
    servletRequest.setAttribute("templateHeadContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationUserResource/userDetailsHeader.jsp");
    servletRequest.setAttribute("templateContent",
                                "/com/smartitengineering/user/ws/resources/OrganizationUserResource/OrganizationUserDetails.jsp");
    Viewable view = new Viewable("/template/template.jsp", userPerson);

    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @PUT
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(UserPerson newUserPerson) {

    ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
    try {
      newUserPerson.getUser().setOrganization(organization);
      newUserPerson.getUser().setPrivileges(getUserPerson().getUser().getPrivileges());
      newUserPerson.getUser().setRoles(getUserPerson().getUser().getRoles());
      Services.getInstance().getUserPersonService().update(newUserPerson);
      responseBuilder = Response.ok(getUserFeed());
    }
    catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Feed getUserFeed() throws UriBuilderException, IllegalArgumentException {
    Feed userFeed = getFeed(userName, new Date());
    userFeed.setTitle(userName);

    // add a self link
    userFeed.addLink(getSelfLink());

    // add a edit link
    Link editLink = getAbderaFactory().newLink();
    editLink.setHref(getUriInfo().getRequestUri().toString());
    editLink.setRel(Link.REL_EDIT);
    editLink.setMimeType(MediaType.APPLICATION_JSON);
    userFeed.addLink(editLink);

    // add a alternate link
    Link altLink = getAbderaFactory().newLink();
    altLink.setHref(getRelativeURIBuilder().path(OrganizationUserResource.class).path(USER_CONTENT_METHOD).build(
        organizationUniqueShortName, userName).toString());
    altLink.setRel(Link.REL_ALTERNATE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);
    userFeed.addLink(altLink);

    Link privilegesLink = getAbderaFactory().newLink();
    privilegesLink.setHref(getRelativeURIBuilder().path(UserPrivilegesResource.class).build(organizationUniqueShortName,
                                                                                            userName).toString());
    privilegesLink.setRel(REL_USER_PRIVILEGES);
    privilegesLink.setMimeType(MediaType.APPLICATION_JSON);
    userFeed.addLink(privilegesLink);

    Link rolesLink = getAbderaFactory().newLink();
    rolesLink.setHref(getRelativeURIBuilder().path(UserRolesResource.class).build(organizationUniqueShortName, userName).toString());
    rolesLink.setRel(REL_USER_ROLES);
    rolesLink.setMimeType(MediaType.APPLICATION_JSON);
    userFeed.addLink(rolesLink);

    Link organizationLink = getAbderaFactory().newLink();
    organizationLink.setHref(getRelativeURIBuilder().path(OrganizationResource.class).build(organizationUniqueShortName).
        toString());
    organizationLink.setRel("organization");
    organizationLink.setMimeType(MediaType.APPLICATION_JSON);
    userFeed.addLink(organizationLink);

    return userFeed;
  }

  @DELETE
  public Response delete() {
    Services.getInstance().getUserPersonService().delete(userPerson);
    ResponseBuilder responseBuilder = Response.ok();
    return responseBuilder.build();
  }

  @POST
  @Path("/delete")
  public Response deletePost() {
    UserPerson userPersonForDelete = userPerson;
    Services.getInstance().getUserPersonService().delete(userPersonForDelete);
    Services.getInstance().getUserService().delete(userPersonForDelete.getUser());
    Services.getInstance().getPersonService().delete(userPersonForDelete.getPerson());
    ResponseBuilder responseBuilder = Response.ok();
    return responseBuilder.build();
  }

  @POST
  @Path("/update")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response updatePost(@HeaderParam("Content-type") String contentType, String message) {
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
      }
    }
    else {
      isHtmlPost = false;
    }

    if (isHtmlPost) {
      UserPerson userPersonForUpdate = userPerson;
      UserPerson newUserPerson = getUserFromContent(message);
      newUserPerson.getUser().setOrganization(organization);
      newUserPerson.getUser().setPrivileges(userPersonForUpdate.getUser().getPrivileges());
      newUserPerson.getUser().setRoles(userPersonForUpdate.getUser().getRoles());
      try {
        Services.getInstance().getUserPersonService().update(newUserPerson);
        responseBuilder = Response.ok(getUserFeed());
      }
      catch (Exception ex) {
        responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
      }
    }
    return responseBuilder.build();
  }

  private UserPerson getUserFromContent(String message) {

    Map<String, String> keyValueMap = new HashMap<String, String>();

    String[] keyValuePairs = message.split("&");

    for (int i = 0; i < keyValuePairs.length; i++) {

      String[] keyValuePair = keyValuePairs[i].split("=");
      if (keyValuePair.length > 1) {
        keyValueMap.put(keyValuePair[0], keyValuePair[1]);
      }
    }

    User newUser = new User();


    if (keyValueMap.get("userName") != null) {
      newUser.setUsername(keyValueMap.get("userName"));
    }
    if (keyValueMap.get("password") != null) {
      newUser.setPassword(keyValueMap.get("password"));
    }

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

    UserPerson userPersonContent = new UserPerson();

    if (keyValueMap.get("id") != null) {
      userPersonContent.setId(NumberUtils.toLong(keyValueMap.get("id")));
    }
    if (keyValueMap.get("version") != null) {
      userPersonContent.setVersion(Long.valueOf(keyValueMap.get("version")));
    }

    if (keyValueMap.get("userId") != null) {
      newUser.setId(NumberUtils.toLong(keyValueMap.get("userId")));
    }
    if (keyValueMap.get("userVersion") != null) {
      newUser.setVersion(Long.valueOf(keyValueMap.get("userVersion")));
    }

    if (keyValueMap.get("personId") != null) {
      person.setId(NumberUtils.toLong(keyValueMap.get("personId")));
    }
    if (keyValueMap.get("personVersion") != null) {
      person.setVersion(Long.valueOf(keyValueMap.get("personVersion")));
    }

    if (keyValueMap.get("selfId") != null) {
      person.getSelf().setId(NumberUtils.toLong(keyValueMap.get("selfId")));
    }
    if (keyValueMap.get("selfVersion") != null) {
      person.getSelf().setVersion(Long.valueOf(keyValueMap.get("selfVersion")));
    }

    if (keyValueMap.get("spouseId") != null) {
      person.getSpouse().setId(NumberUtils.toLong(keyValueMap.get("spouseId")));
    }
    if (keyValueMap.get("spouseVersion") != null) {
      person.getSpouse().setVersion(Long.valueOf(keyValueMap.get("spouseVersion")));
    }

    if (keyValueMap.get("motherId") != null) {
      person.getMother().setId(NumberUtils.toLong(keyValueMap.get("motherId")));
    }
    if (keyValueMap.get("motherVersion") != null) {
      person.getMother().setVersion(Long.valueOf(keyValueMap.get("motherVersion")));
    }

    if (keyValueMap.get("fatherId") != null) {
      person.getFather().setId(NumberUtils.toLong(keyValueMap.get("fatherId")));
    }
    if (keyValueMap.get("fatherVersion") != null) {
      person.getFather().setVersion(Long.valueOf(keyValueMap.get("fatherVersion")));
    }

    userPersonContent.setUser(newUser);
    userPersonContent.setPerson(person);

    return userPersonContent;
  }

  private Organization getOrganization() {
    return Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(organizationUniqueShortName);
  }

  private UserPerson getUserPerson() {
    return Services.getInstance().getUserPersonService().getUserPersonByUsernameAndOrgName(userName,
                                                                                           organizationUniqueShortName);
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
