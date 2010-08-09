/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.LoginResource;
import com.smartitengineering.user.client.api.RootResource;
import com.sun.jersey.api.client.WebResource;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.abdera.protocol.client.ClientResponse;

/**
 *
 * @author modhu7
 */
public class RootResourceImpl extends AbstractClientImpl implements RootResource {
  public static final String REL_LOGIN = "login";

  private final Link loginLink;

  public static RootResource getInstance(){
    return new RootResourceImpl();
  }

  public RootResourceImpl() {
    WebResource resource = getClient().resource(BASE_URI);
    ClientResponse Response = resource.get(ClientResponse.class);
    Feed feed = null;
    Link link = feed.getLink(REL_LOGIN);
    loginLink = link;
  }


  @Override
  public LoginResource performAuthentication(String userName, String password) {
    return new LoginResourceImpl(userName, password, loginLink);
  }
}
