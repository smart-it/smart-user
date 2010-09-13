/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.smartuser.client.api.LoginResource;
import com.smartitengineering.smartuser.client.api.RootResource;
import com.smartitengineering.user.client.impl.login.LoginCenter;
import com.smartitengineering.util.rest.atom.AbstractFeedClientResource;
import com.smartitengineering.util.rest.client.Resource;
import com.smartitengineering.util.rest.client.ResourceLink;
import com.smartitengineering.util.rest.client.jersey.cache.CacheableClientConfigProps;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.atom.abdera.impl.provider.entity.FeedProvider;
import java.net.URISyntaxException;
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public class RootResourceImpl
    extends AbstractFeedClientResource<Resource<? extends Feed>>
    implements RootResource {

  public static final String REL_LOGIN = "Login";
  public static RootResource getInstance() {

    return new RootResourceImpl();
  }

  public RootResourceImpl() {
    super(null, BASE_URI);
  }

  @Override
  public LoginResource performAuthentication(String userName,
                                             String password) {
    try {
      return new LoginResourceImpl(userName, password, getLoginLink(), this);
    }
    catch (URISyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }

  public ResourceLink getLoginLink() {
    return  getRelatedResourceUris().getFirst(REL_LOGIN);
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
    clientConfig.getProperties().put(CacheableClientConfigProps.USERNAME, LoginCenter.getUsername());
    clientConfig.getProperties().put(CacheableClientConfigProps.PASSWORD, LoginCenter.getPassword());
    clientConfig.getClasses().add(JacksonJsonProvider.class);
    clientConfig.getClasses().add(FeedProvider.class);
  }

  @Override
  protected Resource<? extends Feed> instantiatePageableResource(ResourceLink link) {
    return null;
  }
}
