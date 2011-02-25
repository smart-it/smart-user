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
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author modhu7
 */
public class RedirectionFilter implements Filter {

  private static final String[] DEFAULT_BROWSERS = {"Chrome", "Firefox", "Safari", "Opera", "MSIE 8", "MSIE 7", "MSIE 6"};
  public static final String KEY_BROWSER_IDS = "browserIds";
  public static final String LOGIN_FORM_POST_URL = "/j_spring_security_check";
  public static final String REDIRECTOR_URL = "/";
  public static final String LOGIN_ERROR_PARAM_NAME = "login_error";
  public static final String REDIRECTION_URL_PARAM_NAME = "rurl";
  private Logger logger = LoggerFactory.getLogger(RedirectionFilter.class);
  // Configured params
  private String[] browserIds;
  private String loginFormPostUrl;
  private String loginErrorParamName;
  private String redirectorUrl;
  private String redirectionUrlParamName;
  private String loginUrl;

  @Override
  public void init(FilterConfig fc) throws ServletException {

    loginUrl = fc.getInitParameter("loginUrl");
    if (loginUrl == null) {
      throw new IllegalArgumentException("RedirecitonFilter requires param redirectionUrl");
    }

//    loginFormPostUrl = fc.getInitParameter("loginFormPostUrl");
//    if (loginFormPostUrl == null) {
//      loginFormPostUrl = LOGIN_FORM_POST_URL;
//    }
//
//    loginErrorParamName = fc.getInitParameter("loginErrorParamName");
//    if (loginErrorParamName == null) {
//      loginErrorParamName = LOGIN_ERROR_PARAM_NAME;
//    }
//
//    redirectorUrl = fc.getInitParameter("redirectorUrl");
//    if (redirectorUrl == null) {
//      redirectorUrl = LOGIN_ERROR_PARAM_NAME;
//    }
//
//    redirectionUrlParamName = fc.getInitParameter("redirectionUrlParamName");
//    if (redirectionUrlParamName == null) {
//      redirectionUrlParamName = LOGIN_ERROR_PARAM_NAME;
//    }

    String ids = fc.getInitParameter(KEY_BROWSER_IDS);
    this.browserIds = (ids != null) ? ids.split(",") : DEFAULT_BROWSERS;

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException,
                                                                                                ServletException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    HttpServletRequest httpRequest = (HttpServletRequest) request;

//    if (logger.isDebugEnabled()) {
//      logger.debug("servlet path= " + httpRequest.getServletPath());
//      logger.debug("request URL= " + httpRequest.getRequestURL());
//      logger.debug("request URI= " + httpRequest.getRequestURI());
//    }
    logger.info("login url " + loginUrl);
//    logger.info("login error param name " + loginErrorParamName);
//    logger.info("login form post url " + loginFormPostUrl);
//    logger.info("redirector url " + redirectorUrl);
//    logger.info("redirection url param name " + redirectionUrlParamName);
//    logger.info("browser ids " + browserIds);


    GetStatusWrapper wrapper;
    wrapper = new GetStatusWrapper(httpResponse);
    fc.doFilter(request, wrapper);

    int status = wrapper.getStatus();
    String requestUrl = getRequestUrl(httpRequest);
    logger.info("Request url is " + requestUrl);
//    String rurl = request.getParameter(redirectionUrlParamName);
//
//    logger.info("Redirection url from param " + requestUrl);
//
//    if (status == Status.SEE_OTHER.getStatusCode() && requestUrl.contains(loginFormPostUrl)) {
//      logger.info("Request Url is login form post ");
//      if (requestUrl.contains(loginErrorParamName)) {
//        logger.info("Request Url contains login error parameter ");
//        wrapper.sendRedirect(loginUrl + "?" + loginErrorParamName + "=1");
//      }
//      else {
//        logger.info("login is successful, see redirector");
//        if (StringUtils.isNotBlank(rurl)) {
//          logger.info("redirecting to parameter redirection url " + rurl);
//          wrapper.sendRedirect(rurl);
//        }
//        else {
//          logger.info("redirecting to default redirection url ");
//          wrapper.sendRedirect(redirectorUrl);
//        }
//      }
//    }
    if (logger.isInfoEnabled()) {
      logger.info("User Agent " + httpRequest.getHeader("User-Agent"));
      logger.info("Status " + status);
    }
    if (status == Status.SEE_OTHER.getStatusCode() && requestUrl.equals(loginUrl) && !isUserAgentBrowser(httpRequest.
        getHeader("User-Agent"))) {
      logger.info("status is 302 and client is browser");
      wrapper.setStatus(Status.UNAUTHORIZED.getStatusCode());
//      if (requestUrl != null) {
//        logger.info("redirecting to " + loginUrl + "?" + redirectionUrlParamName + "=" + requestUrl);
//        wrapper.sendRedirect(loginUrl + "?" + redirectionUrlParamName + "=" + requestUrl);
//      }
    }
  }

  @Override
  public void destroy() {
  }

  private boolean isUserAgentBrowser(String userAgent) {
    logger.info("user agent " + userAgent);
    for (String browser_id : browserIds) {
      logger.info("Browser " + browser_id + "------ User agent" + userAgent);
      if (userAgent.contains(browser_id)) {
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
