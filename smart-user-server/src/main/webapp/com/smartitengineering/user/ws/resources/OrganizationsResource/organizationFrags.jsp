<%-- 
    Document   : frag
    Created on : Aug 21, 2010, 2:05:22 PM
    Author     : russel
--%>
<%@page import="com.smartitengineering.user.domain.Organization"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

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
<div>
  <c:forEach var="organization" items="${it}" varStatus="status">
    <c:if test="${status.first}">
      <c:set var="first" value="${organization.uniqueShortName}"></c:set>
    </c:if>
    <c:if test="${status.last}">
      <c:set var="last" value="${organization.uniqueShortName}"></c:set>
    </c:if>
    <div>
      <div><c:out value="${organization.name}"/></div>
      <div><c:out value="${organization.uniqueShortName}"/></div>
    </div>
  </c:forEach>   
</div>

<div>  
  <div><a href="/orgs/before/${first}${qParam}"><<</a></div>
  <div><a href="/orgs/after/${last}${qParam}">>></a> </div>
</div>