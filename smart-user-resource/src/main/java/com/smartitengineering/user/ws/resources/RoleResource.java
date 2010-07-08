/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.service.RoleService;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author modhu7
 */
@Path("role")
@Component
@Scope(value = "singleton")
public class RoleResource {
/*
    @Resource(name = "userService")
    private RoleService roleService;

    @POST
    @Consumes("application/xml")
    public Response createRole(RoleElement roleElement) {
        try {
            roleService.create(roleElement.getRole());
            return Response.ok().build();
        } catch (RuntimeException e) {
            String group = e.getMessage().split("-")[0];
            String field = e.getMessage().split("-")[1];
            ExceptionElement exceptionElement = new ExceptionElement();
            exceptionElement.setGroup(group);
            exceptionElement.setFieldCausedBy(field);
            return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).
                    entity(exceptionElement).build();
        }
    }

    @PUT
    @Consumes("application/xml")
    public Response updateRole(RoleElement roleElement) {
        try {
            roleService.update(roleElement.getRole());
            return Response.ok().build();
        } catch (RuntimeException e) {
            String group = e.getMessage().split("-")[0];
            String field = e.getMessage().split("-")[1];
            ExceptionElement exceptionElement = new ExceptionElement();
            exceptionElement.setGroup(group);
            exceptionElement.setFieldCausedBy(field);
            return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).
                    entity(exceptionElement).build();
        }
    }

    @DELETE
    @Path("{name}")
    @Consumes("application/xml")
    public void deleteRole(@PathParam("name") String name) {
        try {
            roleService.delete(roleService.getRoleByName(name));
        } catch (Exception e) {
        }
    }

    @GET
    @Path("{name}")
    @Produces("application/xml")
    public RoleElement getRoleByName(
            @PathParam("name") String name) {
        RoleElement roleElement = new RoleElement();
        try {
            roleElement.setRole(roleService.getRoleByName(name));
        } catch (Exception e) {
        }
        return roleElement;
    }

    @GET
    @Path("search/{name}")
    @Produces("application/xml")
    public RoleElements getRolesByName(
            @PathParam("name") String name) {
        RoleElements roleElements = new RoleElements();
        try {
            roleElements.setRoles(roleService.getRolesByName(name));
        } catch (Exception e) {
        }
        return roleElements;
    }
 * 
 */
}
