<%-- 
    Document   : rootPage
    Created on : Aug 28, 2010, 2:09:24 PM
    Author     : uzzal
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<c:if test="${param['lang']!=null}">
  <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><fmt:message key="org.rootTitle"/></title>
    <link rel="Stylesheet" href="<c:url value="css/default.css"/>" >
    <link rel="Stylesheet" href="<c:url value="css/login.css"/>">
  </head>

  <body>
    
    <%--This is Header portion--%>

    <div id="header" class="homepageHeader">
      <div class="logoContainer"><img src="images/site ultimate build 1.0.0.5.png" alt="SITEL" id="sitelLogo"></div>
      <div class="sitel_slogan_container"><label>IT for smarter living</label></div>
    </div>

    <%--Header ends here--%>

    <div id="maindivision" class="homepageMainDivision">
      <div id="container" class="homepageContainer">
        <%--This is left side or content portion--%>
        <div id="content" class="leftContent">
<%--                    <div id="signup">
                      <jsp:include page="signup.jsp"></jsp:include>
                    </div>--%>
        </div>
        <%--Content portion ends here--%>

        <%--This is right side or login section--%>
        <div id="right" class="rightContent">
          <jsp:include page="login.jsp"></jsp:include>
        </div>
        <%--login or right portion ends here--%>
      </div>
      <div class="clear"></div>

    </div>
    <%--This is Footer portion--%>

    <div id="footer" class="homepageFooter">
      <div class="labelContainer">
        <label>copyright@smart it engineering limited 2010</label>
      </div>
    </div>
    <%--Footer portion ends here--%>
  </body>
</html>