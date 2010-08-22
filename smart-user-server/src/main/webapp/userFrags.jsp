<%-- 
    Document   : userFrags
    Created on : Aug 21, 2010, 2:26:17 PM
    Author     : russel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.smartitengineering.user.domain.User"%>

<div class="" id="">
  <c:forEach var="user" items="${it}">
    <div id=""><c:out value="${user.id}" /></div>
    <div id=""><c:out value="${user.username}" /></div>
  </c:forEach>
</div>
