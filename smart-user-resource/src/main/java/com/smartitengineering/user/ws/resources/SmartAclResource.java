/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.ws.resources;

import com.smartitengineering.user.filter.SmartAclFilter;
import com.smartitengineering.user.security.service.SmartAclService;
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
@Path("smartacl")
@Component
@Scope(value = "singleton")
public class SmartAclResource {
    /*
    @Resource(name = "smartAclService")
    private SmartAclService smartAclService;

    @POST
    @Consumes("application/xml")
    public Response create(SmartAclElement smartAclElement) {
        try {
            smartAclService.create(smartAclElement.getSmartAcl());
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
    public Response update(SmartAclElement smartAclElement) {
        try {
            smartAclService.update(smartAclElement.getSmartAcl());
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
    public Response delete(SmartAclElement smartAclElement) {
        try {
            smartAclService.delete(smartAclElement.getSmartAcl());
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
    public SmartAclElements searchSmartAclByPost(
            SmartAclFilterElement smartAclFilterElement) {
        SmartAclElements smartAclElements = new SmartAclElements();
        SmartAclFilter smartAclFilter;
        if (smartAclFilterElement != null &&
                smartAclFilterElement.getSmartAclFilter()!= null) {
            smartAclFilter = smartAclFilterElement.getSmartAclFilter();
        } else {
            smartAclFilter = new SmartAclFilter();
        }
        try {
            smartAclElements.setSmartAcls(smartAclService.search(smartAclFilter));
        } catch (Exception e) {
        }
        return smartAclElements;
    }


    @GET
    @Produces("application/xml")
    public SmartAclElements searchSmartAclByGet(
            @DefaultValue(value = "NO OID") @QueryParam(value = "oid") final String oid,
            @DefaultValue(value = "NO OWNERUSERNAME") @QueryParam(value =
            "ownerUsername") final String ownerUsername) {
        System.out.println("At resource: oid of acl: " + oid);
        SmartAclFilter filter = new SmartAclFilter();
        filter.setOid(oid);
        filter.setOwnerUsername(ownerUsername);
        SmartAclElements smartAclElements = new SmartAclElements();
        try {
            smartAclElements.setSmartAcls(smartAclService.search(filter));
        } catch (Exception e) {
        }
        return smartAclElements;
    }
     * 
     */
}
