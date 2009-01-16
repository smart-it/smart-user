/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;


import com.smartitengineering.user.filter.UserPersonFilter;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.ws.element.UserPersonElement;
import com.smartitengineering.user.ws.element.UserPersonElements;
import com.smartitengineering.user.ws.element.UserPersonFilterElement;
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
@Path("userperson")
@Component
@Scope(value = "singleton")
public class UserPersonResource {
    @Resource(name = "userService")
    private UserService userService;
    
    
    @POST        
    @Consumes("application/xml")
    public void create(UserPersonElement userPersonElement) {
        try {
            userService.create(userPersonElement.getUserPerson());
        } catch (Exception e) {
        }
    }
    
    @PUT
    @Consumes("application/xml")
    public void updateUserPerson(UserPersonElement userPersonElement) {
        try {
            userService.update(userPersonElement.getUserPerson());
        } catch (Exception e) {
        }
    }

    @DELETE
    @Path("{id}")
    @Consumes("application/xml")
    public void deleteUserPerson(@PathParam("id") Integer id) {
        try {
            userService.delete(userService.getUserByID(id));
        } catch (Exception e) {
        }
    }
    
    @POST
    @Path("search")
    @Consumes("application/xml")
    @Produces("application/xml")
    public UserPersonElements searchUserPerson(
            UserPersonFilterElement userPersonFilterElement) {
        UserPersonElements userPersonElements = new UserPersonElements();
        UserPersonFilter userPersonFilter;
        if (userPersonFilterElement != null && userPersonFilterElement.getUserPersonFilter() != null) {
            userPersonFilter = userPersonFilterElement.getUserPersonFilter();
        } else {
            userPersonFilter = new UserPersonFilter();
        }        
        try {
            userPersonElements.setUserPersons(userService.search(userPersonFilter));
        } catch (Exception e) {
        }
        return userPersonElements;
    }
    
    @GET
    @Path("{id}")
    @Produces("application/xml")
    public UserPersonElement getUserPersonByID(
            @PathParam("id") Integer id) {
        UserPersonElement userPersonElement = new UserPersonElement();
        try {
            userPersonElement.setUserPerson(userService.getUserPersonByID(id));
        } catch (Exception e) {
        }
        return userPersonElement;
    }
    
    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
