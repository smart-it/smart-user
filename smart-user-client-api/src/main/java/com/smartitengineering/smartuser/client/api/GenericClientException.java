/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.smartuser.client.api;

import com.sun.jersey.api.client.ClientResponse;

/**
 *
 * @author imyousuf
 */
public class GenericClientException extends RuntimeException {

  private ClientResponse clientResponse;

  public GenericClientException(ClientResponse clientResponse) {
    super(clientResponse.getClientResponseStatus().getReasonPhrase());
    this.clientResponse = clientResponse;
  }

  public ClientResponse getClientResponse() {
    return clientResponse;
  }
}
