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

<div id="leftmenu">
  <div id="leftmenu_header">User-Creation</div>
  <div id="leftmenu_body">
    <ul>
      <li><a href="javascript: Orgpageselect()">Create</a></li>
    </ul>
  </div>
</div>


<c:if test="${param['lang']!=null}">
  <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>

<div class="show" id="showList">

        <div id="title_of_organization_users">
          <label><fmt:message key="org.usrtitle"/></label>
      </div>


  <div id="top_row">

    <%--<div id="userslist">--%>

      <div class="tableheadname_user">
        <label class="tablehead_label"><fmt:message key="org.usrtablehead1"/></label>
      </div>

      <div class="tableheadname_user">
        <label class="tablehead_label"><fmt:message key="org.usrtablehead2"/></label>
      </div>

    <%--</div>--%>
  </div>


  <div id="userlist">


    
  </div>


  <div id="tablecontentlink_of_next_user"></div>
  <div id="tablecontentlink_of_prev_user"></div>

  

</div>



<div class="hide"  id="create">

  <div id="header_organization_users">
    <label>Users Entry</label>
  </div>

<div id="form_organizationentry">
  <form action="" accept="application/json" enctype="" id="userform" method="post">
    
    <div class="inner-left-label"><label><fmt:message key="org.usrinput1"/></label></div>
    <div class="inner-right-text"><input type="text" name="name" id="fname" class="textfield"></div>
    <div class="clear"></div>

    <div class="inner-left-label"><label><fmt:message key="org.usrinput2"/></label></div>
    <div class="inner-right-text"><input type="text" name="midName" id="mname" class="textfield"></div>
    <div class="clear"></div>

    <div class="inner-left-label"><label><fmt:message key="org.usrinput3"/></label></div>
    <div class="inner-right-text"><input type="text" name="lastName" id="lname" class="textfield"></div>
    <div class="clear"></div>

    <div class="inner-left-label"><label><fmt:message key="org.usrtablehead2"/></label></div>
    <div class="inner-right-text"><input type="text" name="userName" id="uname" class="textfield"></div>
    <div class="clear"></div>

    <div class="inner-left-label"><label><fmt:message key="org.usrinput4"/></label></div>
    <div class="inner-right-text"><input id="password" type="password" name="password" class="textfield"></div>
    <div class="clear"></div>


    <div class="inner-left-label"><label><fmt:message key="org.usrinput7"/></label></div>
    <div class="inner-right-text"><input id="confirmPassword" type="password" name="confirmPassword" class="textfield"></div>
    <div class="clear"></div>

    <div class="inner-left-label"><label><fmt:message key="org.usrinput5"/></label></div>
    <div class="inner-right-text"><input type="text" name="phone" id="phone" class="textfield"></div>
    <div class="clear"></div>

    <div id="btnfield"><label><fmt:message key="org.usrinput6" var="submitbtn"/></label><input name="submitbtn" type="submit" class="submit" value="submit"></div>
    <div class="clear"></div>
  </form>
  
</div>

</div>