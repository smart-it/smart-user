/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author modhu7
 */
@Path("/authz")
public class AuthorizationResource extends AbstractResource {

  static final UriBuilder AUTHORIZATION_URI_BUILDER;// = UriBuilder.fromResource(AuthorizationResource.class);
  static final UriBuilder ROLE_AUTHORIZATION_URI_BUILDER;
  static final UriBuilder ACL_AUTHORIZATION_URI_BUILDER;

  static {
    AUTHORIZATION_URI_BUILDER = UriBuilder.fromResource(AuthorizationResource.class);
    ACL_AUTHORIZATION_URI_BUILDER = UriBuilder.fromResource(AuthorizationResource.class);
    try {
      ACL_AUTHORIZATION_URI_BUILDER.path(
          AuthorizationResource.class.getMethod("getAclAuthorization", String.class, String.class, String.class,
                                                Integer.class));
    }
    catch (NoSuchMethodException ex) {
      Logger.getLogger(AuthorizationResource.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (SecurityException ex) {
      Logger.getLogger(AuthorizationResource.class.getName()).log(Level.SEVERE, null, ex);
    }

    ROLE_AUTHORIZATION_URI_BUILDER = UriBuilder.fromResource(AuthorizationResource.class);
    try {
      ROLE_AUTHORIZATION_URI_BUILDER.path(AuthorizationResource.class.getMethod("getRoleAuthorization",
                                                                                String.class, String.class, String.class));
    }
    catch (NoSuchMethodException ex) {
      Logger.getLogger(AuthorizationResource.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (SecurityException ex) {
      Logger.getLogger(AuthorizationResource.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Path("acl")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response getAclAuthorization(
      @DefaultValue("NO USERNAME") @QueryParam("username") final String userName,
      @QueryParam("orgname") final String organizationName,
      @QueryParam("oid") final String oid,
      @QueryParam("permission") final Integer permission) {
    ResponseBuilder responseBuilder = Response.status(Status.OK);
    Integer auth = Services.getInstance().getAuthorizationService().authorize(userName, organizationName, oid,
                                                                              permission);
    Boolean authResult;
    if(auth < 1)
      authResult = Boolean.FALSE;
    else
      authResult = Boolean.TRUE;
    responseBuilder.entity(authResult.toString());
    return responseBuilder.build();
  }

  @Path("role")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response getRoleAuthorization(
      @DefaultValue("NO USERNAME") @QueryParam("username") final String userName,
      @DefaultValue("NO ORGNAME") @QueryParam("orgname") final String organizationName,
      @DefaultValue("NO CONFIG") @QueryParam("config") final String configAttribute) {
    ResponseBuilder responseBuilder = Response.status(Status.OK);
    User user = Services.getInstance().getUserService().getUserByOrganizationAndUserName(organizationName, userName);
    Boolean authResult = Boolean.FALSE;
    for (Role role : user.getRoles()) {
      if (role.getName().equals(configAttribute)) {
        authResult = Boolean.TRUE;
      }
    }
    responseBuilder.entity(authResult.toString());
    return responseBuilder.build();
  }

  private static class GenericEntityImpl extends GenericEntity<Integer> {

    public GenericEntityImpl(Integer entity) {
      super(entity);
    }
  }
}
