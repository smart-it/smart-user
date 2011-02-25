/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security.filters;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 * @author modhu7
 */
public class GetStatusWrapper extends HttpServletResponseWrapper {

  private int status;

  GetStatusWrapper(HttpServletResponse response) {
    super(response);
  }

  @Override
  public void sendError(int sc) throws IOException {
    status = sc;
    super.sendError(sc);
  }

  @Override
  public void sendError(int sc, String msg) throws IOException {
    status = sc;
    super.sendError(sc, msg);
  }

  @Override
  public void setStatus(int sc) {
    status = sc;
    super.setStatus(sc);

  }

  public int getStatus() {
    return status;
  }
}
