
<%-- 
    Document   : template
    Created on : Aug 4, 2010, 11:19:15 AM
    Author     : uzzal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%--Uzzal--%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

  <title>aponn for you</title>

  <link rel="Stylesheet" href="/css/style.css">
  <link rel="Stylesheet" href="/css/dashboardstyle.css">
  <link rel="Stylesheet" href="/css/organization-style.css">
  <link rel="Stylesheet" href="/css/smart-list.css">
  <link rel="Stylesheet" href="/css/user-style.css">
  <link rel="Stylesheet" href="/css/smart-menu.css">

  <script type="text/javascript" src="/script/javascript_1.js"></script>
  <script type="text/javascript" src="/script/jquery-1.4.2.js"></script>
  <script type="text/javascript" src="/script/jquery.validate.js"></script>
  <script type="text/javascript" src="/script/siteljquerylib.js"></script>
  <script type="text/javascript" src="/script/scriptFile.js"></script>

  <c:if test="${not empty templateHeadContent}">
    <jsp:include page="${templateHeadContent}"></jsp:include>
  </c:if>

</head>

<body>

  <div id="menu_common" class="leftmenu">
    <div id="menu_common_header_1" class="leftmenu_header"><label>Common Navigator</label></div>
    <div id="menu_common_body_1" class="leftmenu_body">
      <ul>
        <li><a href="/orgs">OrganizationList</a></li>
      </ul>
    </div>
  </div>

  <div id="main" >

    <div id="header">
      <div id="sitel_logo"><img src="/images/site ultimate build 1.0.0.5.jpg" alt="sitel" id="img-sitel-logo"></div>
      <div id="sitel_slogan"><label>IT for smarter living</label></div>
    </div>

    <div id="options"></div>

    <div class="clear"></div>

    <div id="content">
      <jsp:include page="${templateContent}"></jsp:include>
    </div>

    <div id="right"></div>

    <div class="clear"></div>

    <div id="footer">Footer</div>

  </div>

</body>
</html>
<%--Uzzal--%>