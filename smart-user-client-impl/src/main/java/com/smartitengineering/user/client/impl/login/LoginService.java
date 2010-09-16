/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl.login;

import com.smartitengineering.user.client.impl.AbstractClientImpl;
import com.smartitengineering.user.client.impl.exception.ExceptionElement;
import com.smartitengineering.user.client.impl.exception.SmartException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;


/**
 *
 * @author russel
 */
public class LoginService {

  private static AbstractClientImpl clientImpl = new AbstractClientImpl(){};

    public static void login(String username, String password) {
        System.out.println(username);
        System.out.println(password);
        System.out.println(clientImpl.getBaseUri());
        Client client = clientImpl.getClient();
        WebResource resource = client.resource(clientImpl.getBaseUri());
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
