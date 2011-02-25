/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security.filters;

import java.io.IOException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author modhu7
 */
class GetStatusWrapper extends HttpServletResponseWrapper {

  private int status;
  private String location;
  private boolean redirectSet;
  protected final transient Logger logger = LoggerFactory.getLogger(getClass());

  GetStatusWrapper(HttpServletResponse response) {
    super(response);
  }

  @Override
  public void sendError(int sc) throws IOException {
    setStatus(sc);
    super.sendError(sc);
  }

  @Override
  public void sendError(int sc, String msg) throws IOException {
    setStatus(sc);
    super.sendError(sc, msg);
  }

  @Override
  public void setStatus(int sc) {
    status = sc;
    if (logger.isInfoEnabled()) {
      logger.info("Setting status " + sc);
    }
    super.setStatus(sc);

  }

  @Override
  public void sendRedirect(String location) throws IOException {
    setStatus(SC_FOUND);
    this.location = location;
    if (logger.isInfoEnabled()) {
      logger.info("Setting Location header to " + location);
    }
    redirectSet = true;
  }

  @Override
  public void setResponse(ServletResponse response) {
    logger.info("Setting response directly");
    super.setResponse(response);
  }

  public int getStatus() {
    return status;
  }

  public String getLocation() {
    return location;
  }

  public void enableSendRedirect() throws IOException {
    if (isRedirectSet()) {
      super.sendRedirect(location);
    }
  }

  public boolean isRedirectSet() {
    return redirectSet;
  }
}
