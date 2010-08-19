<%-- 
    Document   : OrganizationDetails
    Created on : Jul 24, 2010, 12:55:21 PM
    Author     : russel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.smartitengineering.user.domain.Organization" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.Collection"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">



<c:if test="${param['lang']!=null}">
    <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Individual Organization Details</title>
        <link rel="Stylesheet" href="/css/organization-style.css">
        <script type="text/javascript" src="/script/javascript_1.js"></script>

    </head>
    <body>
        <div>
  <div id="">Related tabs will be in this div</div>
  <h1><c:out value="${it.name}"></c:out></h1>
  
  <div id="showList" class="show">
         <div class=""><label><fmt:message key="org.tablehead2"/></label></div><label>${it.name}</label><div class=""></div>
    <div class=""><label><fmt:message key="org.tablehead3"/></label></div><div class=""><label>${it.uniqueShortName}</label></div>
    <div class=""><label><fmt:message key="org.inputlabel3"/></label></div><div class=""><label>${it.address.streetAddress}</label></div>
    <div class=""><label><fmt:message key="org.inputlabel4"/></label></div><div class=""><label>${it.address.city}</label></div>
    <div class=""><label><fmt:message key="org.inputlabel5"/></label></div><div class=""><label>${it.address.state}</label></div>
    <div class=""><label><fmt:message key="org.inputlabel6"/></label></div><div class=""><label>${it.address.country}</label></div>
    <div class=""><label><fmt:message key="org.inputlabel7"/></label></div><div class=""><label>${it.address.zip}</label></div>
    <div><a href="javascript: Orgpageselect()">Edit</a></div>
    </div>
    
   </div>

    <div><a href="/orgs/${it.uniqueShortName}/users">UserList</a></div>
    



    <div id="create" class="hide">

    <fmt:message key="org.usrinput6" var="submitbtn"/>
        <div id="form_organizationentry" align="center">

        <form method="POST" action ="http://russel:9090/orgs/shortname/${it.uniqueShortName}" accept="application/json" id="organizationform">

            <div class="inner-left"><label><fmt:message key="org.tablehead2"/></label></div>
            <div class="inner-right" align="left"><input type="text" name="name" value="${it.name}" class="textField"></div>

            <div class="inner-left"><label><fmt:message key="org.tablehead3"/></label></div>
            <div class="inner-right" align="left"><input type="text" name="uniqueShortName" value="${it.uniqueShortName}" class="textField"></div>

            <div class="inner-left"><label><fmt:message key="org.inputlabel3"/></label></div>
            <div class="inner-right" align="left"><input type="text" name="streetAddress" value="${it.address.streetAddress}" class="textField"></div>

            <div class="inner-left"><label><fmt:message key="org.inputlabel4"/></label></div>
            <div class="inner-right" align="left"><input type="text" name="city" value="${it.address.city}" class="textField"></div>

            <div class="inner-left"><fmt:message key="org.inputlabel5"/></div>
            <div class="inner-right" align="left"><input type="text" name="state" value="${it.address.state}" class="textField"></div>

            <div class="inner-left"><fmt:message key="org.inputlabel6"/></div>
            <div class="inner-right" align="left"><input type="text" name="country" value="${it.address.country}" class="textField"></div>

            <div class="inner-left"><fmt:message key="org.inputlabel7"/></div>
            <div class="inner-right" align="left"><input type="text" name="zip" value="" class="textField"></div>

            <div></div>
            <div><input type="hidden" name="id" value="${it.id}"></div>

            <div></div>
            <div><input type="hidden" name="version" value="${it.version}"></div>

            <div style="clear: both"></div>
            <div id="btnfield" align="center"><input type="submit" value="UPDATE" name="submitbtn"></div>
            <div style="clear: both"></div>

        </form>

        </div>

     </div>

    </body>
</html>
