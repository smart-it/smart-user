/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.security.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author modhu7
 */
public class RedirectionFilter implements Filter {

  private static final String[] DEFAULT_BROWSERS = {"Chrome", "Firefox", "Safari", "Opera", "MSIE"};
  public static final String KEY_BROWSER_IDS = "browserIds";
  public static final String LOGIN_FORM_POST_URL = "/j_spring_security_check";
  public static final String REDIRECTOR_URL = "/";
  public static final String LOGIN_ERROR_PARAM_NAME = "login_error";
  public static final String REDIRECTION_URL_PARAM_NAME = "rurl";
  private Logger logger = LoggerFactory.getLogger(RedirectionFilter.class);
  // Configured params
  private String[] browserIds;
  private String loginUri;

  @Override
  public void init(FilterConfig fc) throws ServletException {

    loginUri = fc.getInitParameter("loginUrl");
    if (loginUri == null) {
      throw new IllegalArgumentException("RedirecitonFilter requires param redirectionUrl");
    }
    String ids = fc.getInitParameter(KEY_BROWSER_IDS);
    this.browserIds = (ids != null) ? ids.split(",") : DEFAULT_BROWSERS;

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException,
                                                                                                ServletException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    HttpServletRequest httpRequest = (HttpServletRequest) request;

    final String contextPath = httpRequest.getContextPath();
    String loginRedirectUrl = new StringBuilder("http://").append(httpRequest.getHeader(HttpHeaders.HOST)).append(StringUtils.
        isBlank(contextPath) ? "/" : contextPath).append(loginUri).toString();
    if (logger.isInfoEnabled()) {
      logger.info("login url " + loginUri);
      logger.info("login url to check for " + loginRedirectUrl);
    }
    final GetStatusWrapper wrapper;
    wrapper = new GetStatusWrapper(httpResponse);
    fc.doFilter(request, wrapper);
    final int status = wrapper.getStatus();
    String requestUrl = getRequestUrl(httpRequest);
    if (logger.isInfoEnabled()) {
      logger.info("Request url is " + requestUrl);
      logger.info("User Agent " + httpRequest.getHeader(HttpHeaders.USER_AGENT));
      logger.info("Status " + status);
    }
    if (wrapper.isRedirectSet()) {
      String location = wrapper.getLocation();
      if (location.startsWith(loginRedirectUrl) && !isUserAgentBrowser(httpRequest.getHeader(HttpHeaders.USER_AGENT))) {
        logger.info("status is 302 and client is not browser");
        wrapper.setStatus(Status.UNAUTHORIZED.getStatusCode());
      }
      else {
        wrapper.enableSendRedirect();
      }
    }
  }

  @Override
  public void destroy() {
  }

  private boolean isUserAgentBrowser(String userAgent) {
    for (String browserId : browserIds) {
      if (logger.isInfoEnabled()) {
        logger.info("Browser " + browserId + " ------ User agent" + userAgent);
      }
      if (userAgent.contains(browserId)) {
        return true;
      }
    }
    return false;
  }

  private String getRequestUrl(HttpServletRequest request) {
    String result = request.getRequestURI();
    if (result == null) {
      result = request.getServletPath();
    }
    if ((result == null) || (result.equals(""))) {
      result = "/";
    }
    return result;
  }
}
