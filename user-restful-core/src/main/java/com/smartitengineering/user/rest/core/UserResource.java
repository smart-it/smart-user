/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.rest.core;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import com.smartitengineering.user.domain.User;
import javax.ws.rs.POST;



/**
 *
 * @author russel
 */

@Path("{user}")
public class UserResource {

    private UriInfo uriInfo;

    public UserResource(){
        
    }

  @GET
  @Produces(MediaType.APPLICATION_JSON)  
  public User getByUser(String userName) {
      
      return new User();
  }

  @POST
  @Consumes
  @Path("users")
  public void postUser(User user ){
      
  }


}
