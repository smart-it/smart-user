/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.rest.client.login;

import com.smartitengineering.user.rest.client.AbstractClientImpl;
import com.smartitengineering.user.rest.client.exception.SmartException;
import com.smartitengineering.user.ws.element.ExceptionElement;
import com.smartitengineering.user.ws.element.UserElements;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

/**
 *
 * @author modhu7
 */
public class LoginService extends AbstractClientImpl {

    public void login(String username, String password) {
        DefaultApacheHttpClientConfig clientConfig = new DefaultApacheHttpClientConfig();
        clientConfig.getState().setCredentials(null, null, -1, username, password);
        clientConfig.getProperties().put(ApacheHttpClientConfig.PROPERTY_PREEMPTIVE_AUTHENTICATION,
            Boolean.TRUE);
        Client client = ApacheHttpClient.create(clientConfig);
        WebResource resource = client.resource(BASE_URI);
        resource = resource.path("user/alluser");

        UserElements userElements = new UserElements();

        try {
            userElements = resource.get(UserElements.class);
            LoginCenter.setUsername(username);
            LoginCenter.setPassword(password);
        } catch (UniformInterfaceException ex) {
            ExceptionElement message = ex.getResponse().getEntity(ExceptionElement.class);
            int status = ex.getResponse().getStatus();
            throw new SmartException(message, status, ex);
        }
    }
}
