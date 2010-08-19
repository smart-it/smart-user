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
<%@page import="com.smartitengineering.user.domain.User"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">




<c:if test="${param['lang']!=null}">
  <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>

<div class="show" id="showList">

  <div id="userslist">

    <div id="title_of_organization_users">
      <label><fmt:message key="org.usrtitle"/></label>
    </div>

    <div id="top_row">

      <div class="tableheadname_user">
        <label class="tablehead_label"><fmt:message key="org.usrtablehead1"/></label>
      </div>

      <div class="tableheadname_user">
        <label class="tablehead_label"><fmt:message key="org.usrtablehead2"/></label>
      </div>

    </div>



    <c:forEach var="user" items="${it}">

      <div id="individual_row">
        <div class="tablecontentname_user">
          <label class="tablecontent_label"> <a href="users/username/${user.username}" ><label><c:out value="${user.id}" /></label></a></label>
        </div>

        <div class="tablecontentname_user">
          <label class="tablecontent_label"><a href="users/username/${user.username}" ><label><c:out value="${user.username}"/></label></a></label>
        </div>

      </div>

    </c:forEach>


    <div class="tablecontent_label">
      <a href="javascript: Orgpageselect()">Create</a>
    </div>

  </div>



</div>

<fmt:message key="org.usrinput6" var="submitbtn"/>

<div class="hide"  id="create">

  <div id="header_organization_users">
    <label>Users Entry</label>
  </div>

  <div id="form_organizationentry">
    <form action="http://russel:9090/users" method="post" accept="application/json" enctype="" id="userform">

      <div class="inner-left-label"><label><fmt:message key="org.usrinput1"/></label></div>
      <div class="inner-right-text"><input type="text" name="name" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left-label"><label><fmt:message key="org.usrinput2"/></label></div>
      <div class="inner-right-text"><input type="text" name="uniqueShortName" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left-label"><label><fmt:message key="org.usrinput3"/></label></div>
      <div class="inner-right-text"><input type="text" name="uniqueShortName" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left-label"><label><fmt:message key="org.usrinput4"/></label></div>
      <div class="inner-right-text"><input type="text" name="uniqueShortName" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left-label"><label><fmt:message key="org.usrinput5"/></label></div>
      <div class="inner-right-text"><input type="text" name="uniqueShortName" class="textField"></div>
      <div class="clear"></div>


      <div id="btnfield"><input type="submit" value="${submitbtn}" name="submitBtn" ></div>
      <div class="clear"></div>

    </form>
  </div>

</div>