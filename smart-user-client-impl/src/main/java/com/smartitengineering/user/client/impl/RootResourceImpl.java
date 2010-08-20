/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.LoginResource;
import com.smartitengineering.user.client.api.RootResource;
import com.smartitengineering.util.rest.atom.ClientUtil;
import com.sun.jersey.api.client.ClientResponse;
import java.net.URI;
import javax.ws.rs.core.MediaType;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;

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
//    URI testUri = null;
//    try{
//      testUri = new URI("http://localhost:9090");
//    }catch(Exception ex){
//
//    }
    ClientResponse response = ClientUtil.readClientResponse( BASE_URI, getHttpClient(), MediaType.APPLICATION_ATOM_XML);
    Feed feed = ClientUtil.getFeed(response);
    Link link = feed.getLink(REL_LOGIN);
    loginLink = link;
  }


  @Override
  public LoginResource performAuthentication(String userName, String password) {
    return new LoginResourceImpl(userName, password, loginLink);
  }

  public Link getLoginLink(){
    return loginLink;
  }
}
