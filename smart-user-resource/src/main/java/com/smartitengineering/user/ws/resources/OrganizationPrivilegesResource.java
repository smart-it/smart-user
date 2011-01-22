/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.service.Services;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.util.rest.atom.server.AbstractResource;
import com.sun.jersey.api.view.Viewable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

/**
 *
 * @author russel
 */
//@Path("/orgs")
@Path("/orgs/sn/{organizationUniqueShortName}/privs")
public class OrganizationPrivilegesResource extends AbstractResource {

  private String organizationUniqueShortName;
  private Organization organization;
  
  static Method ORGANIZATION_PRIVILEGE_AFTER_NAME_METHOD;
  static Method ORGANIZATION_PRIVILEGE_BEFORE_NAME_METHOD;
  @Context
  private HttpServletRequest servletRequest;

  static {        

    try {
      ORGANIZATION_PRIVILEGE_BEFORE_NAME_METHOD = OrganizationPrivilegesResource.class.getMethod("getBefore", String.class);
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }    
    try {
      ORGANIZATION_PRIVILEGE_AFTER_NAME_METHOD = OrganizationPrivilegesResource.class.getMethod("getAfter", String.class);
    }
    catch (Exception ex) {
      throw new InstantiationError();
    }
  }

  public OrganizationPrivilegesResource(@PathParam("organizationUniqueShortName") String organizationUniqueShortName) {
    this.organizationUniqueShortName = organizationUniqueShortName;
    organization = getOrganization();
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Path("/before/{privilegeName}")
  public Response getBefore(@PathParam("privilegeName") String beforePrivilegeName) {
    return get(beforePrivilegeName, true);
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  @Path("/after/{privilegeName}")
  public Response getAfter(@PathParam("privilegeName") String afterPrivilegeName) {
    return get(afterPrivilegeName, false);
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Response getHtml() {
    ResponseBuilder responseBuilder = Response.ok();
    //Collection<SecuredObject> securedObjects = Services.getInstance().getSecuredObjectService().
    Collection<Privilege> privileges = Services.getInstance().getPrivilegeService().getPrivilegesByOrganization(
        organizationUniqueShortName);

    servletRequest.setAttribute("templateContent2",
                                "/com/smartitengineering/user/ws/resources/OrganizationPrivilegeResource/OrgPrivilegeList.jsp");
//        Viewable view = new Viewable("OrgPrivilegeList", privileges, OrganizationPrivilegeResource.class);
    Viewable view = new Viewable("/template/template.jsp", privileges);
    responseBuilder.entity(view);
    return responseBuilder.build();
  }

  @GET
  @Produces(MediaType.APPLICATION_ATOM_XML)
  public Response get() {
    return get(null, true);
  }

  public Response get(String privilegeName, boolean isBefore) {
    ResponseBuilder responseBuilder = Response.ok();
    if(organization==null){
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    // create a new atom feed
    Feed atomFeed = getAbderaFactory().newFeed();

    // create a link to parent resource, in this case now it is linked to root resource
    Link parentResourceLink = getAbderaFactory().newLink();
    parentResourceLink.setHref(UriBuilder.fromResource(OrganizationResource.class).build(organizationUniqueShortName).
        toString());
    parentResourceLink.setRel("organization");
    atomFeed.addLink(parentResourceLink);

    // get the organizations accoring to the query
    Collection<Privilege> privileges = Services.getInstance().getPrivilegeService().getPrivilegesByOrganization(
        organizationUniqueShortName);

    if (privileges != null && !privileges.isEmpty()) {

      MultivaluedMap<String, String> queryParam = getUriInfo().getQueryParameters();
      List<Privilege> privilegeList = new ArrayList<Privilege>(privileges);

      // uri builder for next and previous organizations according to count
      final UriBuilder nextUri = getRelativeURIBuilder().path(OrganizationPrivilegeResource.class).path(ORGANIZATION_PRIVILEGE_AFTER_NAME_METHOD);
      final UriBuilder previousUri = getRelativeURIBuilder().path(OrganizationPrivilegeResource.class).path(ORGANIZATION_PRIVILEGE_BEFORE_NAME_METHOD);

      // link to the next organizations based on count
      Link nextLink = getAbderaFactory().newLink();
      nextLink.setRel(Link.REL_NEXT);
      Privilege lastPrivilege = privilegeList.get(0);


      for (String key : queryParam.keySet()) {
        final Object[] values = queryParam.get(key).toArray();
        nextUri.queryParam(key, values);
        previousUri.queryParam(key, values);
      }
      nextLink.setHref(nextUri.build(organizationUniqueShortName, lastPrivilege.getName()).toString());
      //nextLink.setHref(UriBuilder.fromResource(OrganizationsResource.class).build(lastOrganization.getUniqueShortName()).toString());

      atomFeed.addLink(nextLink);

      /* link to the previous organizations based on count */
      Link prevLink = getAbderaFactory().newLink();
      prevLink.setRel(Link.REL_PREVIOUS);
      Privilege firstPrivilege = privilegeList.get(privileges.size() - 1);

      prevLink.setHref(previousUri.build(organizationUniqueShortName, firstPrivilege.getName()).toString());
      //prevLink.setHref(nameLike)
      atomFeed.addLink(prevLink);

      // add entry of individual organization
      for (Privilege privilege : privileges) {
        Entry organizationPrivilegeEntry = getAbderaFactory().newEntry();

        organizationPrivilegeEntry.setId(privilege.getName());
        organizationPrivilegeEntry.setTitle(privilege.getDisplayName());
        organizationPrivilegeEntry.setSummary(privilege.getShortDescription());
        //organizationEntry.setUpdated(privilege.);

        /* setting link to the individual organization resource*/

        Link organizationPrivilegeLink = getAbderaFactory().newLink();
        //organizationLink.setHref(OrganizationResource.ORGANIZATION_URI_BUILDER.clone().build(organizationUniqueShortName,privilege.getName()).toString());
        organizationPrivilegeLink.setHref(UriBuilder.fromResource(OrganizationPrivilegeResource.class).build(
            organizationUniqueShortName, privilege.getName()).toString());
        organizationPrivilegeLink.setRel(Link.REL_ALTERNATE);
        organizationPrivilegeLink.setMimeType(MediaType.APPLICATION_ATOM_XML);
        organizationPrivilegeEntry.addLink(organizationPrivilegeLink);

        atomFeed.addEntry(organizationPrivilegeEntry);
      }
    }
    responseBuilder.entity(atomFeed);
    return responseBuilder.build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response post(Privilege privilege) {
    ResponseBuilder responseBuilder = Response.ok();
    if(organization==null){
      return responseBuilder.status(Status.NOT_FOUND).build();
    }
    try {      
      privilege.setParentOrganization(organization);
      Services.getInstance().getPrivilegeService().create(privilege);
      responseBuilder = Response.status(Status.CREATED);
      responseBuilder.location(getAbsoluteURIBuilder().path(OrganizationPrivilegeResource.class).build(organizationUniqueShortName, privilege.getName()));

    }
    catch (Exception ex) {      
      responseBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);
    }
    return responseBuilder.build();
  }

  private Organization getOrganization() {
    return Services.getInstance().getOrganizationService().getOrganizationByUniqueShortName(organizationUniqueShortName);
  }

  @Override
  protected String getAuthor() {
    return "Smart User";
  }
}
