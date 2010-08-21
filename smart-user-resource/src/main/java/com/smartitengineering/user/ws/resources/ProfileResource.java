/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Address;
import com.smartitengineering.user.domain.BasicIdentity;
import com.smartitengineering.user.domain.GeoLocation;
import com.smartitengineering.user.domain.Name;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.User;
import com.sun.jersey.api.view.Viewable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author modhu7
 */
@Path("/orgs/{organizationShortName}/users/username/{userName}/profile")
public class ProfileResource extends AbstractResource {

  private Person person;
  private User user;
  static final UriBuilder PROFILE_URI_BUILDER = UriBuilder.fromResource(ProfileResource.class);
  static final UriBuilder PROFILE_CONTENT_URI_BUILDER;

  static {
    PROFILE_CONTENT_URI_BUILDER = PROFILE_URI_BUILDER.clone();
    try {
      PROFILE_CONTENT_URI_BUILDER.path(OrganizationUserResource.class.getMethod("getProfile"));
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new InstantiationError();
    }
  }

  public ProfileResource(@PathParam("organizationShortName") String organizationShortName, @PathParam("userName") String userName) {
    user = Services.getInstance().getUserService().getUserByOrganizationAndUserName(organizationShortName, userName);
  }



  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    Feed profileFeed = getProfileFeed();
    ResponseBuilder responseBuilder = Response.ok(profileFeed);
    return responseBuilder.build();
  }

  private Feed getProfileFeed() {
    Feed profileFeed = getFeed(toStringName(person), new Date());
    profileFeed.setTitle(toStringName(person));

    // add a self link
    profileFeed.addLink(getSelfLink());

    // add a edit link
    Link editLink = abderaFactory.newLink();
    editLink.setHref(uriInfo.getRequestUri().toString());
    editLink.setRel(Link.REL_EDIT);
    editLink.setMimeType(MediaType.APPLICATION_JSON);
    profileFeed.addLink(editLink);

    // add a alternate link
    Link altLink = abderaFactory.newLink();
    altLink.setHref(PROFILE_CONTENT_URI_BUILDER.clone().build(user.getOrganization().getUniqueShortName(), user.getUsername()).toString());
    altLink.setRel(Link.REL_ALTERNATE);
    altLink.setMimeType(MediaType.APPLICATION_JSON);
    profileFeed.addLink(altLink);

    return profileFeed;
  }

  private String toStringName(Person person) {
    return person.getSelf().getName().getFirstName() + person.getSelf().getName().getMiddleInitial() + person.getSelf().
        getName().getLastName();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/content")
  public Response getProfile() {
    ResponseBuilder responseBuilder = Response.ok(person);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getHtml() {
    ResponseBuilder responseBuilder = Response.ok();
    Viewable view = new Viewable("PersonProfile", person, ProfileResource.class);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @PUT
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(Person newPerson) {

    ResponseBuilder responseBuilder = Response.status(Status.SERVICE_UNAVAILABLE);
    try {

      Services.getInstance().getPersonService().update(newPerson);

      responseBuilder = Response.ok(getProfileFeed());
    } catch (Exception ex) {
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
      ex.printStackTrace();
    }
    return responseBuilder.build();
  }

  @DELETE
  public Response delete() {
    Services.getInstance().getPersonService().delete(person);
    ResponseBuilder responseBuilder = Response.ok();
    return responseBuilder.build();
  }

  @POST
  @Path("/delete")
  public Response deletePost() {
    Services.getInstance().getPersonService().delete(person);
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
    } else if (contentType.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
      contentType = MediaType.APPLICATION_OCTET_STREAM;
      isHtmlPost = true;
      try {
        //Will search for the first '=' if not found will take the whole string
        final int startIndex = 0;//message.indexOf("=") + 1;
        //Consider the first '=' as the start of a value point and take rest as value
        final String realMsg = message.substring(startIndex);
        //Decode the message to ignore the form encodings and make them human readable
        message = URLDecoder.decode(realMsg, "UTF-8");
      } catch (UnsupportedEncodingException ex) {
        ex.printStackTrace();
      }
    } else {
      contentType = contentType;
      isHtmlPost = false;
    }

    if (isHtmlPost) {
      Person newPerson = getProfileFromContent(message);
      try {
        Services.getInstance().getPersonService().update(newPerson);
        responseBuilder = Response.ok(getProfileFeed());
      } catch (Exception ex) {
        responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
      }
    }
    return responseBuilder.build();
  }

  private Person getProfileFromContent(String message) {

    Map<String, String> keyValueMap = new HashMap<String, String>();

    String[] keyValuePairs = message.split("&");

    for (int i = 0; i < keyValuePairs.length; i++) {

      String[] keyValuePair = keyValuePairs[i].split("=");
      keyValueMap.put(keyValuePair[0], keyValuePair[1]);
    }

    Person newPerson = new Person();
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
    newPerson.setSelf(self);


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
    newPerson.setSpouse(spouse);

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
    newPerson.setMother(mother);

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
    newPerson.setFather(father);

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
    newPerson.setAddress(address);

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
      newPerson.setPrimaryEmail(keyValueMap.get("primaryEmail"));
    }

    if (keyValueMap.get("phoneNumber") != null) {
      newPerson.setPhoneNumber(keyValueMap.get("phoneNumber"));
    }
    if (keyValueMap.get("secondaryEmail") != null) {
      newPerson.setSecondaryEmail(keyValueMap.get("secondaryEmail"));
    }
    if (keyValueMap.get("faxNumber") != null) {
      newPerson.setFaxNumber(keyValueMap.get("faxNumber"));
    }
    if (keyValueMap.get("cellPhoneNumber") != null) {
      newPerson.setCellPhoneNumber(keyValueMap.get("cellPhoneNumber"));
    }
    return newPerson;
  }
}
