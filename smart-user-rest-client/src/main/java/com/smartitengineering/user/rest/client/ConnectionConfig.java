/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.rest.client;

/**
 *
 * @author imyousuf
 */
public class ConnectionConfig {

    private String basicUrl;
    private int port;
    private String contextPath;

    public String getBasicUrl() {
        return basicUrl;
    }

    public void setBasicUrl(String basicUrl) {
        this.basicUrl = basicUrl;
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
