<%-- 
    Document   : OrganizationUserDetails
    Created on : Jul 24, 2010, 1:30:52 PM
    Author     : russel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.smartitengineering.user.domain.User" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.Collection"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">

<c:if test="${param['lang']!=null}">
  <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>


<%--<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><c:out value="${it.username}"></c:out></title>
    
      
        <link rel="Stylesheet" href="/css/organization-style.css">
        <script type="text/javascript" src="/script/javascript_1.js"></script>
        <script type="text/javascript" src="/script/jquery-1.4.2.js"></script>
    </head>
   

    <body>--%>
        


<div id="leftmenu">
  <div id="leftmenu_header"><label>Individual-User</label></div>

  <div id="leftmenu_body">
    <ul>
      <li><a href="javascript: Orgpageselect()"><fmt:message key="org.tablehead4"/></a></li>
      <li><a href="http://localhost:9090/orgs">OrganizationList</a></li>
    </ul></div>
</div>

<div id="showList" class="show">
  
  <div id="individual_user_details_header"><label><c:out value="${it.username}"></c:out></label></div>

  <div id="individual_user_details_content">

      

  <div class="individual_user_label"><label><fmt:message key="org.usrtablehead2"/></label></div>
  <div class="individual_user_data"><label>${it.username}</label></div>
  <div class="clear"></div>

  <div class="individual_user_label"><label><fmt:message key="org.usrinput4"/></label></div>
  <div class="individual_user_data"><label>${it.password}</label></div>
  <div class="clear"></div>


  </div>
  
</div>



<div id="create" class="hide">

  <div id="header_organization_users"><marquee><label id="header_user_label"><c:out value="${it.username}"></c:out>-Edit Information</label></marquee></div>

  <fmt:message key="org.usrinput6" var="submitbtn"/>


        

  <div id="form_organizationentry">
    
    <form method="POST" action ="" accept="application/json" id="organizationform">

        
       <div class="inner-left"><label><fmt:message key="org.usrtablehead1"/></label></div>
       <div class="inner-right" align="left"><input type="hidden" name="id" value="${it.id}" class="textField" id="id"></div>
       <div class="inner-left"><label><fmt:message key="org.usrtablehead3"/></label></div>
       <div class="inner-right" align="left"><input type="text" name="version" value="${it.version}" class="textField" id="version"></div>

      <div class="inner-left"><label><fmt:message key="org.usrtablehead2"/></label></div>
      <div class="inner-right" align="left"><input type="text" name="userName" value="${it.username}" class="textField" id="name"></div>

      <div class="inner-left"><label><fmt:message key="org.usrinput4"/></label></div>
      <div class="inner-right" align="left"><input type="text" name="password" value="${it.password}" class="textField" id="password"></div>



      <div style="clear: both"></div>
      <div id="btnfield" align="center"><input type="submit" value="UPDATE" name="submitbtn"></div>
      <div style="clear: both"></div>

      <div style="clear: both"></div>
      <div id="btnfield" align="center"><input type="submit" value="DELETE" name="submitbtn"></div>
      <div style="clear: both"></div>

    </form>

  </div>

</div>