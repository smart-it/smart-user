/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.client.impl;

/**
 *
 * @author russel
 */
public class ConnectionConfig {

    private String basicUri;
    private int port;
    private String host;
    private String contextPath;

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

    public String getBasicUri() {
        return basicUri;
    }

    public void setBasicUri(String basicUrl) {
        this.basicUri = basicUrl;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
