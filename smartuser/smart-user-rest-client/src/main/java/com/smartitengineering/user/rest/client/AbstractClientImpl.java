/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.rest.client.login.LoginCenter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author imyousuf
 */
public abstract class AbstractClientImpl {

    protected static final URI BASE_URI = getBaseURI();
    private WebResource webResource;
    private Client client;
    

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
        new ClassPathXmlApplicationContext(
                "client-context.xml");
        final ConnectionConfig connectionConfig =
                ConfigFactory.getInstance().getConnectionConfig();
        return UriBuilder.fromUri(connectionConfig.getBasicUrl()).port(getPort(connectionConfig.
                getPort())).path(connectionConfig.getContextPath()).build();
    }

    protected AbstractClientImpl() {
        Client c = Client.create();
        webResource =
                c.resource(BASE_URI);        
    }

    public WebResource getWebResource() {
        DefaultApacheHttpClientConfig clientConfig = new DefaultApacheHttpClientConfig();
        clientConfig.getState().setCredentials(null, null, -1, LoginCenter.getUsername(), LoginCenter.getPassword());
        clientConfig.getProperties().put(ApacheHttpClientConfig.PROPERTY_PREEMPTIVE_AUTHENTICATION,
            Boolean.TRUE);
        client = ApacheHttpClient.create(clientConfig);
        webResource = client.resource(BASE_URI);
        return webResource;
    }
}
