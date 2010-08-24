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

<c:if test="${param['lang']!=null}">
  <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>

<div id="leftmenu_userdeatils_1" class="leftmenu">
  <div id="leftmenu_header_userdeatils_1" class="leftmenu_header"><label>Individual-User</label></div>

  <div id="leftmenu_body_userdeatils_1" class="leftmenu_body">
    <ul>
      <li><a href="javascript: Orgpageselect()"><fmt:message key="org.tablehead4"/></a></li>
    </ul>
  </div>
</div>

<div id="showList" class="show">
  <div id="individual_user_details_header"  class="header_details_info"><label><c:out value="${it.username}"></c:out></label></div>
  <div id="individual_user_details_content" class="content_details_info">
    <div class="individual_details_label"><label><fmt:message key="org.usrtablehead2"/></label></div>
    <div class="individual_details_data"><label>${it.username}</label></div>
    <div class="clear"></div>
    <div class="individual_details_label"><label><fmt:message key="org.usrinput4"/></label></div>
    <div class="individual_details_data"><label>${it.password}</label></div>
    <div class="clear"></div>
  </div>
</div>

<div id="create" class="hide">

  <div id="header_organization_users" class="header_entry_form"><marquee><label id="header_user_label"><c:out value="${it.username}"></c:out>-Edit Information</label></marquee></div>

  <fmt:message key="org.usrinput6" var="submitbtn"/>

  <div id="form_organizationentry" class="entry_form">
    <form method="POST" action ="" accept="application/json" id="organizationform">

      <input type="hidden" name="id" value="${it.id}" class="textField" id="id">
      <input type="hidden" name="version" value="${it.version}" class="textField" id="version">
      <div style="clear: both"></div>
      <div class="form_label"><label><fmt:message key="org.usrtablehead2"/></label></div>
      <div class="form_textField"><input type="text" name="userName" value="${it.username}" class="textField" id="name"></div>
      <div style="clear: both"></div>
      <div class="form_label"><label><fmt:message key="org.usrinput4"/></label></div>
      <div class="form_textField"><input type="text" name="password" value="${it.password}" class="textField" id="password"></div>
      <div style="clear: both"></div>
      <div class="btnfield"><input type="submit" value="DELETE" name="submitbtn" class="submitbtn"></div>
      <div class="btnfield"><input type="submit" value="UPDATE" name="submitbtn" class="submitbtn"></div>
      <div style="clear: both"></div>
    </form>
  </div>

</div>