/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.rest.client.exception.SmartException;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.ws.element.ExceptionElement;
import com.smartitengineering.user.ws.element.PrivilegeElement;
import com.smartitengineering.user.ws.element.PrivilegeElements;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public class PrivilegeServiceClientImpl extends AbstractClientImpl implements 
        PrivilegeService {

    public void create(Privilege privilege) {
        PrivilegeElement privilegeElement = new PrivilegeElement();
        privilegeElement.setPrivilege(privilege);
        final Builder type =
                getWebResource().path("privilege").type("application/xml");
        try {
            type.post(privilegeElement);
        } catch (UniformInterfaceException ex) {
            ExceptionElement message =
                    ex.getResponse().
                    getEntity(ExceptionElement.class);
            int status = ex.getResponse().getStatus();
            throw new SmartException(message, status, ex);
        }
    }

    public void delete(Privilege privilege) {
        PrivilegeElement privilegeElement = new PrivilegeElement();
        privilegeElement.setPrivilege(privilege);
        getWebResource().path("privilege/" + privilege.getName()).delete();
    }

    public Privilege getPrivilegeByName(String name) {
        WebResource resource = getWebResource().path("privilege/" + name);
        final PrivilegeElement privilegeElement =
                resource.get(PrivilegeElement.class);
        return privilegeElement.getPrivilege();
    }

    public Collection<Privilege> getPrivilegesByName(String name) {
        WebResource resource =
                getWebResource().path("privilege/" + "search/" + name);
        final PrivilegeElements privilegeElements =
                resource.get(PrivilegeElements.class);
        return privilegeElements.getPrivileges();
    }

    public void update(Privilege privilege) {
        PrivilegeElement privilegeElement = new PrivilegeElement();
        privilegeElement.setPrivilege(privilege);
        final Builder type =
                getWebResource().path("privilege").type("application/xml");
        try {
            type.put(privilegeElement);
        } catch (UniformInterfaceException ex) {
            ExceptionElement message =
                    ex.getResponse().
                    getEntity(ExceptionElement.class);
            int status = ex.getResponse().getStatus();
            throw new SmartException(message, status, ex);
        }
    }
}
