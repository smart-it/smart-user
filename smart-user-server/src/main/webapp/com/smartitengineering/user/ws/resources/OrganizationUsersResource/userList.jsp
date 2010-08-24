<%-- 
    Document   : userList
    Created on : Jul 22, 2010, 3:36:48 PM
    Author     : russel
--%>

<%@page import="javax.swing.text.Document"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Collection"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@taglib prefix="pg" uri="/WEB-INF/taglib139.tld" %>--%>
<%@page import="com.smartitengineering.user.domain.User"%>
<%@page import="com.smartitengineering.user.domain.Organization"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">

<div id="leftmenu_userlist_1" class="leftmenu">
  <div id="leftmenu_header_userlist_1" class="leftmenu_header">User-Creation</div>
  <div id="leftmenu_body_userlist_1" class="leftmenu_body">
    <ul>
      <li><a href="javascript: Orgpageselect()">Create</a></li>
    </ul>
  </div>
</div>

<c:if test="${param['lang']!=null}">
  <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>

<div class="show" id="showList">
  <div id="title_of_organization_users" class="header_of_list">
    <label><fmt:message key="org.usrtitle"/></label>
  </div>
  <div id="top_row" class="list_column_names">
    <div class="tableheadname">
      <label class="tablehead_label"><fmt:message key="org.usrtablehead1"/></label>
    </div>
    <div class="tableheadname">
      <label class="tablehead_label"><fmt:message key="org.usrtablehead2"/></label>
    </div>
    <div class="tableheadname">
      <label class="tablehead_label"><fmt:message key="org.usrtablehead3"/></label>
    </div>
  </div>
  <div class="tablecontentname" id="tablecontentid"></div>
</div>

<div class="hide"  id="create">
  <div id="header_organization_users" class="header_entry_form">
    <label>Users Entry</label>
  </div>

  <div id="form_organizationentry" class="entry_form">
    <form action="" accept="application/json" enctype="" id="userform" method="post">
      <div class="form_label"><label><fmt:message key="org.usrinput1"/></label></div>
      <div class="form_textField"><input type="text" name="firstName" id="fname" class="textfield"></div>
      <div class="clear"></div>
      <div class="form_label"><label><fmt:message key="org.usrinput2"/></label></div>
      <div class="form_textField"><input type="text" name="middleInitial" id="mname" class="textfield"></div>

      <div class="clear"></div>
      <div class="form_label"><label><fmt:message key="org.usrinput3"/></label></div>
      <div class="form_textField"><input type="text" name="lastName" id="lname" class="textfield"></div>
      <div class="clear"></div>
      <div class="form_label"><label><fmt:message key="org.usrtablehead2"/></label></div>
      <div class="form_textField"><input type="text" name="userName" id="fname" class="textfield"></div>
      <div class="clear"></div>
      <div class="form_label"><label><fmt:message key="org.usrinput8"/></label></div>
      <div class="form_textField"><input type="text" name="nationalID" id="fname" class="textfield"></div>
      <div class="form_label"><label><fmt:message key="org.usrinput9"/></label></div>
      <div class="form_textField"><input type="text" name="primaryEmail" id="fname" class="textfield"></div>
      <div class="clear"></div>

      <div class="form_label"><label><fmt:message key="org.usrinput4"/></label></div>
      <div class="form_textField"><input id="password" type="password" name="password" class="textfield"></div>
      <div class="clear"></div>
      <div class="form_label"><label><fmt:message key="org.usrinput7"/></label></div>
      <div class="form_textField"><input id="confirmPassword" type="password" name="confirmPassword" class="textfield"></div>
      <div class="clear"></div>
      <div class="form_label"><label><fmt:message key="org.usrinput5"/></label></div>
      <div class="form_textField"><input type="text" name="phone" id="phone" class="textfield"></div>

      <div class="clear"></div>
      <div class="btnfield"><label><fmt:message key="org.usrinput6" var="submitbtn"/></label><input name="submitbtn" type="submit" class="submitbtn" value="SUBMIT"></div>
      <div class="clear"></div>
    </form>
  </div>
</div>