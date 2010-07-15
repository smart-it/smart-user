/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;


/**
 *
 * @author russel
 */
@Provider
@Consumes(value = {"application/json", "text/json"})
@Produces(value = {"application/json", "text/json"})
public class JacksonJsonProvider extends org.codehaus.jackson.jaxrs.JacksonJsonProvider{

}
