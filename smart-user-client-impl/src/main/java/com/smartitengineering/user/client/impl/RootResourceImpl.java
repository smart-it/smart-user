/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.smartuser.client.api.LoginResource;
import com.smartitengineering.smartuser.client.api.RootResource;
import com.smartitengineering.user.client.impl.login.LoginCenter;
import com.smartitengineering.util.rest.atom.AtomClientUtil;
import com.smartitengineering.util.rest.client.AbstractClientResource;
import com.smartitengineering.util.rest.client.ResouceLink;
import com.smartitengineering.util.rest.client.jersey.cache.CacheableClientConfigProps;
import com.sun.jersey.api.client.config.ClientConfig;
import java.net.URISyntaxException;
import javax.ws.rs.core.MediaType;
import org.apache.abdera.model.Feed;

/**
 *
 * @author modhu7
 */
public class RootResourceImpl
    extends AbstractClientResource<Feed>
    implements RootResource {

  public static final String REL_LOGIN = "Login";
  public static RootResource getInstance() {

    return new RootResourceImpl();
  }

  public RootResourceImpl() {
    super(null, BASE_URI, MediaType.APPLICATION_ATOM_XML, Feed.class, AtomClientUtil.getInstance());
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

  public ResouceLink getLoginLink() {
    return  getRelatedResourceUris().getFirst(REL_LOGIN);
  }

  @Override
  protected void processClientConfig(ClientConfig clientConfig) {
    clientConfig.getProperties().put(CacheableClientConfigProps.USERNAME, LoginCenter.getUsername());
    clientConfig.getProperties().put(CacheableClientConfigProps.PASSWORD, LoginCenter.getPassword());
  }
}
