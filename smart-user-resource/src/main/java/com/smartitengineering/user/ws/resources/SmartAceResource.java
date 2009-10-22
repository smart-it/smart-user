/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.filter.SmartAceFilter;
import com.smartitengineering.user.security.service.SmartAceService;
import com.smartitengineering.user.ws.element.ExceptionElement;
import com.smartitengineering.user.ws.element.SmartAceElement;
import com.smartitengineering.user.ws.element.SmartAceElements;
import com.smartitengineering.user.ws.element.SmartAceFilterElement;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 *
 * @author modhu7
 */
@Path("smartace")
@Component
@Scope(value = "singleton")
public class SmartAceResource {
    @Resource(name = "smartAceService")
    private SmartAceService smartAceService;

    @POST
    @Consumes("application/xml")
    public Response create(SmartAceElement smartAceElement) {
        try {
            smartAceService.create(smartAceElement.getSmartAce());
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
    public Response update(SmartAceElement smartAceElement) {
        try {
            smartAceService.update(smartAceElement.getSmartAce());
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
    @Consumes("application/xml")
    public Response delete(SmartAceElement smartAceElement) {
        try {
            smartAceService.delete(smartAceElement.getSmartAce());
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

    @POST
    @Path("search")
    @Consumes("application/xml")
    @Produces("application/xml")
    public SmartAceElements searchSmartAceByPost(
            SmartAceFilterElement smartAceFilterElement) {
        SmartAceElements smartAceElements = new SmartAceElements();
        SmartAceFilter smartAceFilter;
        if (smartAceFilterElement != null &&
                smartAceFilterElement.getSmartAceFilter()!= null) {
            smartAceFilter = smartAceFilterElement.getSmartAceFilter();
        } else {
            smartAceFilter = new SmartAceFilter();
        }
        try {
            smartAceElements.setSmartAces(smartAceService.search(smartAceFilter));
        } catch (Exception e) {
        }
        return smartAceElements;
    }

    @GET
    @Produces("application/xml")
    public SmartAceElements searchSmartAceByGet(
            @DefaultValue(value = "NO OID") @QueryParam(value = "oid") final String oid,
            @DefaultValue(value = "NO SIDUSERNAME") @QueryParam(value =
            "firstName") final String sidUsername) {
        SmartAceFilter filter = new SmartAceFilter();
        filter.setOid(oid);
        filter.setSidUsername(sidUsername);
        SmartAceElements smartAceElements = new SmartAceElements();
        try {
            smartAceElements.setSmartAces(smartAceService.search(filter));
        } catch (Exception e) {
        }
        return smartAceElements;
    }
}
