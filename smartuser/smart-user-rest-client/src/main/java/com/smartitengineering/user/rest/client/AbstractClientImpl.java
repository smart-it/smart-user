/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.rest.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author imyousuf
 */
public abstract class AbstractClientImpl {

    protected static final URI BASE_URI = getBaseURI();
    private WebResource webResource;

    private static int getPort(int defaultPort) {
        String port = System.getenv("JERSEY_HTTP_PORT");
        if (null != port) {
            try {
                return Integer.parseInt(port);
            } catch (NumberFormatException e) {
            }
        }
        return defaultPort;
    }

    private static URI getBaseURI() {
        /*
         * Please note that the following code needs to change to read the
         * host, port and context from a properties file
         */
        Properties properties = new Properties();

        try {
            InputStream file = AbstractClientImpl.class.getClassLoader().getResourceAsStream(
                    "connection.properties");
            properties.load(file);
        } catch (IOException ex) {
        }
        String url = properties.getProperty("url");
        String port = properties.getProperty("port");
        String warname = properties.getProperty("warname");
        return UriBuilder.fromUri(url).port(getPort(new Integer(port))).path(warname).build();
    }

    protected AbstractClientImpl() {
        Client c = Client.create();
        webResource =
                c.resource(BASE_URI);
        System.out.println(BASE_URI);
    }

    public WebResource getWebResource() {
        return webResource;
    }
}
