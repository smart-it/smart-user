/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;


import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.ws.element.RoleElement;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
    @Resource(name = "userService")
    private UserService userService;
    
    @POST
    @Consumes("application/xml")
    public void createRole(RoleElement roleElement) {
        try {
            userService.create(roleElement.getRole());
        } catch (Exception e) {
        }
    }
    
    @PUT
    @Consumes("application/xml")
    public void updateRole(RoleElement roleElement) {
        try {
            userService.update(roleElement.getRole());
        } catch (Exception e) {
        }
    }

    @DELETE
    @Path("{name}")
    @Consumes("application/xml")
    public void deleteRole(@PathParam("name") String name) {
        try {
            userService.delete(userService.getRoleByName(name));
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
            roleElement.setRole(userService.getRoleByName(name));
        } catch (Exception e) {
        }
        return roleElement;
    }

}
