<%-- 
    Document   : login
    Created on : Jul 20, 2010, 6:03:53 PM
    Author     : uzzal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:if test="${param['lang']!=null}">
  <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>


<div id="inloginform">
    <div id="loginheader"><label>sitel secured login!</label></div>
        <form action="" method="post">

            <div id="loginlabeldiv" align="right"><label id="loginlbl"><fmt:message key="org.username"/> </label></div>
            <div id="loginboxdiv"><input type="text" name="name" id="loginnametextbox" align="left"></div>
            <div id="loginlabeldiv" align="right"><label id="loginlbl"><fmt:message key="org.password"/></label></div>
            <div id="loginboxdiv"><input type="password" name="password" id="loginpasstextbox" align="left"></div>
            <div style="clear: both"></div>
            <input type="hidden" name="companyName">
            <div class="btnlinkcheck"><input type="checkbox"><fmt:message key="org.keepsigned"/></div>
            <div class="btnlinkcheck"><input type="submit" value="Sign In" name="btnSubmit" id="loginBtn"></div>
            <div class="btnlinkcheck"><a href="#">Can't access your account?</a></div>

        </form>
</div>