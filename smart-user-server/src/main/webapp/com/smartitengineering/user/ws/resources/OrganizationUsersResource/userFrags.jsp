<%-- 
    Document   : userFrags
    Created on : Aug 21, 2010, 2:26:17 PM
    Author     : russel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.smartitengineering.user.domain.User"%>
<%@page import="com.smartitengineering.user.domain.Organization"%>

<c:set var="first" value="0"></c:set>
<c:set var="last" value="0"></c:set>

<c:choose>
  <c:when test="${empty param.count}">
    <c:set var="qParam" value="" />
  </c:when>
  <c:otherwise>
    <c:set var="qParam" value="?count=${param.count}" />
  </c:otherwise>
</c:choose>

<div class="" id="">
  <c:forEach var="user" items="${it}">
    <c:if test="${status.first}">
      <c:set var="first" value="${user.username}"></c:set>
    </c:if>
    <c:if test="${status.last}">
      <c:set var="last" value="${user.username}"></c:set>
    </c:if>
    <div id=""><a href="users/username/${user.username}">${user.id}</a></div>
    <div id=""><a href="users/username/${user.username}">${user.username}</a></div>
  </c:forEach>
</div>

<div class="navigation_container">
  <div><a href="/before/${first}${qParam}" class="list_navigation_links"><< previous</a></div>
  <div><a href="/after/${last}${qParam}" class="list_navigation_links">next >></a> </div>
</div>