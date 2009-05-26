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

    public static void login(String username, String password) {
        System.out.println(username);
        System.out.println(password);
        System.out.println(BASE_URI);
        DefaultApacheHttpClientConfig clientConfig = new DefaultApacheHttpClientConfig();
        clientConfig.getState().setCredentials(null, null, -1, username, password);
        clientConfig.getProperties().put(ApacheHttpClientConfig.PROPERTY_PREEMPTIVE_AUTHENTICATION,
                Boolean.TRUE);
        Client client = ApacheHttpClient.create(clientConfig);
        WebResource resource = client.resource(BASE_URI);                

        try {
            resource.get(UserElements.class);
        } catch (UniformInterfaceException ex) {
            int status = ex.getResponse().getStatus();
            if (status == 404) {
                LoginCenter.setUsername(username);
                LoginCenter.setPassword(password);
            }else{
                throw new SmartException(new ExceptionElement(), status, ex);
            }
        }

    }
}
