/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.rest.client.exception.SmartException;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.ws.element.ExceptionElement;
import com.smartitengineering.user.ws.element.RoleElement;
import com.smartitengineering.user.ws.element.RoleElements;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public class RoleServiceClientImpl extends AbstractClientImpl implements
        RoleService {

    public void create(Role role) {
        RoleElement roleElement = new RoleElement();
        roleElement.setRole(role);
        final Builder type =
                getWebResource().path("role").type("application/xml");
        try {
            type.post(roleElement);
        } catch (UniformInterfaceException ex) {
            ExceptionElement message =
                    ex.getResponse().
                    getEntity(ExceptionElement.class);
            int status = ex.getResponse().getStatus();
            throw new SmartException(message, status, ex);
        }
    }

    public void delete(Role role) {
        RoleElement roleElement = new RoleElement();
        roleElement.setRole(role);
        getWebResource().path("role/" + role.getName()).delete();
    }

    public Role getRoleByName(String name) {
        WebResource resource = getWebResource().path("role/" + name);
        final RoleElement roleElement =
                resource.get(RoleElement.class);
        return roleElement.getRole();
    }

    public Collection<Role> getRolesByName(String name) {
        WebResource resource = getWebResource().path("role/" + "search/" + name);
        final RoleElements roleElements =
                resource.get(RoleElements.class);
        return roleElements.getRoles();
    }

    public void update(Role role) {
        RoleElement roleElement = new RoleElement();
        roleElement.setRole(role);
        final Builder type =
                getWebResource().path("role").type("application/xml");
        try {
            type.put(roleElement);
        } catch (UniformInterfaceException ex) {
            ExceptionElement message =
                    ex.getResponse().
                    getEntity(ExceptionElement.class);
            int status = ex.getResponse().getStatus();
            throw new SmartException(message, status, ex);
        }
    }
}
