/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;


import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.ws.element.ExceptionElement;
import com.smartitengineering.user.ws.element.PrivilegeElement;
import com.smartitengineering.user.ws.element.PrivilegeElements;
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
@Path("privilege")
@Component
@Scope(value = "singleton")
public class PrivilegeResource {
    @Resource(name = "userService")
    private PrivilegeService privilegeService;
    
    @POST
    @Consumes("application/xml")
    public Response createPrivilege(PrivilegeElement privilegeElement) {
        try {
            privilegeService.create(privilegeElement.getPrivilege());
            return Response.ok().build();
        } catch (RuntimeException e) {            
            String group = e.getMessage().split("-")[0];            
            String field = e.getMessage().split("-")[1];
            ExceptionElement exceptionElement = new ExceptionElement();
            exceptionElement.setGroup(group);
            exceptionElement.setFieldCausedBy(field);
            return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).entity(exceptionElement).build();            
        }
    }
    
    @PUT
    @Consumes("application/xml")
    public Response updatePrivilege(PrivilegeElement privilegeElement) {
        try {
            privilegeService.update(privilegeElement.getPrivilege());
            return Response.ok().build();
        }catch (RuntimeException e) {            
            String group = e.getMessage().split("-")[0];            
            String field = e.getMessage().split("-")[1];
            ExceptionElement exceptionElement = new ExceptionElement();
            exceptionElement.setGroup(group);
            exceptionElement.setFieldCausedBy(field);
            return Response.serverError().status(Status.INTERNAL_SERVER_ERROR).entity(exceptionElement).build();            
        }
    }

    @DELETE
    @Path("{name}")
    @Consumes("application/xml")
    public void deletePrivilege(@PathParam("name") String name) {
        try {
            privilegeService.delete(privilegeService.getPrivilegeByName(name));
        } catch (Exception e) {
        }
    }


    @GET
    @Path("{name}")
    @Produces("application/xml")
    public PrivilegeElement getPrivilegeByName(
            @PathParam("name") String name) {
        PrivilegeElement privilegeElement = new PrivilegeElement();
        try {
            privilegeElement.setPrivilege(privilegeService.getPrivilegeByName(name));
        } catch (Exception e) {
        }
        return privilegeElement;
    }
    
    @GET
    @Path("search/{name}")
    @Produces("application/xml")
    public PrivilegeElements getPrivilegesByName(
            @PathParam("name") String name) {
        PrivilegeElements privilegeElements = new PrivilegeElements();
        try {
            privilegeElements.setPrivileges(privilegeService.getPrivilegesByName(name));
        } catch (Exception e) {
        }
        return privilegeElements;
    }
}
