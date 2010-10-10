/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.LoginResource;
import com.smartitengineering.user.client.api.RootResource;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.smartitengineering.util.rest.client.jersey.cache.CacheableClientConfigProps;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.atom.abdera.impl.provider.entity.FeedProvider;
import java.net.URISyntaxException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public class RootResourceImpl
    extends AbstractFeedClientResource<Resource<? extends Feed>>
    implements RootResource {

  public static final String REL_LOGIN = "Login";
  private final String username, password;
  private final static ThreadLocal<Entry<String, String>> usernamePass = new ThreadLocal<Entry<String, String>>();

  public static RootResource getInstance(String username, String password) {
    usernamePass.set(new SimpleEntry<String, String>(username, password));
    return new RootResourceImpl(username, password);
  }

  private RootResourceImpl(String username, String password) {
    super(null, BASE_URI);
    this.username = username;
    this.password = password;
  }

  @Override
  public LoginResource getLoginResource() {
    try {
      return new LoginResourceImpl(username, password, getLoginLink(), this);
    }
    catch (URISyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public ResourceLink getLoginLink() {
    return getRelatedResourceUris().getFirst(REL_LOGIN);
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
    clientConfig.getProperties().put(CacheableClientConfigProps.USERNAME, usernamePass.get().getKey());
    clientConfig.getProperties().put(CacheableClientConfigProps.PASSWORD, usernamePass.get().getValue());
    clientConfig.getClasses().add(JacksonJsonProvider.class);
    clientConfig.getClasses().add(FeedProvider.class);
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return null;
  }
}
