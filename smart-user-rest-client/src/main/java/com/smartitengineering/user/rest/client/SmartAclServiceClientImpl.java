/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.filter.SmartAclFilter;
import com.smartitengineering.user.rest.client.exception.SmartException;
import com.smartitengineering.user.security.domain.SmartAce;
import com.smartitengineering.user.security.domain.SmartAcl;
import com.smartitengineering.user.security.service.SmartAclService;
import com.smartitengineering.user.ws.element.ExceptionElement;
import com.smartitengineering.user.ws.element.SmartAclElement;
import com.smartitengineering.user.ws.element.SmartAclElement;
import com.smartitengineering.user.ws.element.SmartAclElements;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.Collection;
import javax.ws.rs.core.MultivaluedMap;

/**
 *
 * @author modhu7
 */
public class SmartAclServiceClientImpl extends AbstractClientImpl implements SmartAclService{

    public void create(SmartAcl smartAcl) {
        SmartAclElement smartAclElement = new SmartAclElement();
        smartAclElement.setSmartAcl(smartAcl);
        final Builder type = getWebResource().path("smartacl").type(
                "application/xml");
        try {
            type.post(smartAclElement);
        } catch (UniformInterfaceException e) {
            ExceptionElement message = e.getResponse().getEntity(
                    ExceptionElement.class);
            int status = e.getResponse().getStatus();
            throw new SmartException(message, status, e);
        }
    }

    public void update(SmartAcl smartAcl) {
        SmartAclElement smartAclElement = new SmartAclElement();
        smartAclElement.setSmartAcl(smartAcl);
        final Builder type = getWebResource().path("smartacl").type(
                "application/xml");
        try {
            type.put(smartAclElement);
        } catch (UniformInterfaceException e) {
            ExceptionElement message = e.getResponse().getEntity(
                    ExceptionElement.class);
            int status = e.getResponse().getStatus();
            throw new SmartException(message, status, e);
        }
    }

    public void delete(SmartAcl smartAcl) {
        SmartAclElement smartAclElement = new SmartAclElement();
        smartAclElement.setSmartAcl(smartAcl);
        try {
            getWebResource().path("smartacl/" + smartAcl.getId()).delete();
        } catch (UniformInterfaceException ex) {
            ExceptionElement message = ex.getResponse().getEntity(
                    ExceptionElement.class);
            int status = ex.getResponse().getStatus();
            throw new SmartException(message, status, ex);
        }
    }

    public Collection<SmartAcl> search(SmartAclFilter filter) {
        MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.add("oid", filter.getOid());
        map.add("ownerUsername", filter.getOwnerUsername());
        WebResource resource = getWebResource().path("smartacl").queryParams(map);
        final SmartAclElements smartAclElements = resource.get(SmartAclElements.class);
        return smartAclElements.getSmartAcls();
    }

    public Collection<SmartAce> getAceEntries(SmartAcl acl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void validate(SmartAcl acl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
