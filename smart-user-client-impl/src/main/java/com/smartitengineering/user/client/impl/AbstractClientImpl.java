/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.util.rest.atom.HttpClient;
import com.sun.corba.se.impl.orb.ParserTable.TestContactInfoListFactory;
import com.sun.jersey.api.client.Client;
import java.net.URI;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import javax.ws.rs.core.UriBuilder;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author russel
 */
public class AbstractClientImpl {

  protected static final URI BASE_URI;
  protected static final ConnectionConfig CONNECTION_CONFIG;

  static {
    ApplicationContext context = new ClassPathXmlApplicationContext("config-context.xml");
    CONNECTION_CONFIG = ConfigFactory.getInstance().getConnectionConfig();
    BASE_URI = UriBuilder.fromUri(CONNECTION_CONFIG.getContextPath()).host(CONNECTION_CONFIG.getHost()).port(CONNECTION_CONFIG.
        getPort()).path(CONNECTION_CONFIG.getBasicUri()).build();
  }
  private Client client;
  private HttpClient httpClient;

  protected AbstractClientImpl() {
  }

  public Client getClient() {
    if (client == null) {
      DefaultApacheHttpClientConfig clientConfig = new DefaultApacheHttpClientConfig();
      client = ApacheHttpClient.create(clientConfig);
    }
    return client;
  }

  public HttpClient getHttpClient() {
    if (httpClient == null) {
      httpClient = new HttpClient(getClient(), BASE_URI.getHost(), CONNECTION_CONFIG.getPort());
    }
    return httpClient;
  }
}
