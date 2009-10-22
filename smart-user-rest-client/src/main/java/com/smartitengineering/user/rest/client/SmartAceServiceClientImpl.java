/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.filter.SmartAceFilter;
import com.smartitengineering.user.rest.client.exception.SmartException;
import com.smartitengineering.user.security.domain.SmartAce;
import com.smartitengineering.user.security.service.SmartAceService;
import com.smartitengineering.user.ws.element.ExceptionElement;
import com.smartitengineering.user.ws.element.SmartAceElement;
import com.smartitengineering.user.ws.element.SmartAceElements;
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
public class SmartAceServiceClientImpl extends AbstractClientImpl implements SmartAceService{

    public void create(SmartAce smartAce) {
        SmartAceElement smartAceElement = new SmartAceElement();
        smartAceElement.setSmartAce(smartAce);
        final Builder type = getWebResource().path("smartace").type(
                "application/xml");
        try {
            type.post(smartAceElement);
        } catch (UniformInterfaceException e) {
            ExceptionElement message = e.getResponse().getEntity(
                    ExceptionElement.class);
            int status = e.getResponse().getStatus();
            throw new SmartException(message, status, e);
        }
    }

    public void update(SmartAce smartAce) {
        SmartAceElement smartAceElement = new SmartAceElement();
        smartAceElement.setSmartAce(smartAce);
        final Builder type = getWebResource().path("smartace").type(
                "application/xml");
        try {
            type.put(smartAceElement);
        } catch (UniformInterfaceException e) {
            ExceptionElement message = e.getResponse().getEntity(
                    ExceptionElement.class);
            int status = e.getResponse().getStatus();
            throw new SmartException(message, status, e);
        }
    }

    public void delete(SmartAce smartAce) {
        SmartAceElement smartAceElement = new SmartAceElement();
        smartAceElement.setSmartAce(smartAce);
        try {
            getWebResource().path("smartace/" + smartAce.getId()).delete();
        } catch (UniformInterfaceException ex) {
            ExceptionElement message = ex.getResponse().getEntity(
                    ExceptionElement.class);
            int status = ex.getResponse().getStatus();
            throw new SmartException(message, status, ex);
        }
    }

    public Collection<SmartAce> search(SmartAceFilter filter) {
        MultivaluedMap<String, String> map = new MultivaluedMapImpl();
        map.add("oid", filter.getOid());
        map.add("sidUsername", filter.getSidUsername());
        WebResource resource = getWebResource().path("smartAce").queryParams(map);
        final SmartAceElements smartAceElements = resource.get(SmartAceElements.class);
        return smartAceElements.getSmartAces();
    }

}
