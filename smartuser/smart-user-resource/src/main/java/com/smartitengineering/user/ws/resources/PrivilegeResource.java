/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;


import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.ws.element.PrivilegeElement;
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
    private UserService userService;
    
    @POST
    @Consumes("application/xml")
    public Response createPrivilege(PrivilegeElement privilegeElement) {
        try {
            userService.create(privilegeElement.getPrivilege());
            return Response.ok().build();
        } catch (Exception e) {
            return Response.ok().build();
        }
    }
    
    @PUT
    @Consumes("application/xml")
    public Response updatePrivilege(PrivilegeElement privilegeElement) {
        try {
            userService.update(privilegeElement.getPrivilege());
            return Response.ok().build();
        } catch (Exception e) {
            return Response.ok().build();
        }
    }

    @DELETE
    @Path("{name}")
    @Consumes("application/xml")
    public Response deletePrivilege(@PathParam("name") String name) {
        try {
            userService.delete(userService.getPrivilegeByName(name));
            return Response.ok().build();
        } catch (Exception e) {
            return Response.ok().build();
        }
    }


    @GET
    @Path("{name}")
    @Produces("application/xml")
    public PrivilegeElement getPrivilegeByName(
            @PathParam("name") String name) {
        PrivilegeElement privilegeElement = new PrivilegeElement();
        try {
            privilegeElement.setPrivilege(userService.getPrivilegeByName(name));
        } catch (Exception e) {
        }
        return privilegeElement;
    }
}
