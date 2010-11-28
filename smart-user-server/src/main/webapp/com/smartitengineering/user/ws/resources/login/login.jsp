<%-- 
    Document   : login
    Created on : Jul 20, 2010, 6:03:53 PM
    Author     : uzzal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--<c:if test="${param['lang']!=null}">
  <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>--%>

<c:out value="modhuuuuuuuuuuuuu"/>
  

<div id="inloginform" class="loginSection">
  <div id="loginheader" class="loginHeader"><label>SITEL secured login!</label></div>
  <div class="clear"></div>
  <form action="<c:url value='j_spring_security_check'/>" method="POST">

    <div class="loginEntrySection">
      <div id="loginlabeldiv" class="loginLabelDiv"><label>Username:</label></div>
      <div id="loginboxdiv" class="loginTextBoxDiv"><input type="text" name="j_username" value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>' id="loginnametextbox" class="loginTextBox"></div>
      <div class="clear"></div>
      <div id="loginlabeldiv" class="loginLabelDiv"><label>Password:</label></div>
      <div id="loginboxdiv" class="loginTextBoxDiv"><input type="password" name="j_password" id="loginpasstextbox" class="loginTextBox"></div>
      <div class="clear"></div>

      <input type="hidden" name="companyName">
    </div>

    <div class="btnLinkCheckDiv">
      <div class="btnlinkcheck"><input type="checkbox" name="_spring_security_remember_me"> Keep me signed in</div>
      <div class="btnDiv"><input type="submit" value="Sign In" name="submit" id="loginBtn"></div>
      <%--<div class="btnlinkcheck"><input type="image" src="more_btn.jpg" name="btnSubmit" id="loginBtn" alt="SUBMIT"></div>--%>

      <div class="btnlinkcheck"><a href="#">Can't access your account?</a></div>
    </div>

  </form>

</div>