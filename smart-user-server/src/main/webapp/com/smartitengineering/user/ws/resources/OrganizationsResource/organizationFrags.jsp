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
<div id="mainOrgPaginatedList" class="tableList">
  <c:forEach var="organization" items="${it}" varStatus="status">
    <c:if test="${status.first}">
      <c:set var="first" value="${organization.uniqueShortName}"></c:set>
    </c:if>
    <c:if test="${status.last}">
      <c:set var="last" value="${organization.uniqueShortName}"></c:set>
    </c:if>
    <div id="row${status.index}" class="row_of_list">
      <div id="orgName${status.index}" class="orgName_column"><a href="/orgs/shortname/${organization.uniqueShortName}">${organization.name}</a></div>
      <div id="orgShortName${status.index}" class="orgShortName_column"><a href="/orgs/shortname/${organization.uniqueShortName}">${organization.uniqueShortName}</a></div>
    </div>
  </c:forEach>   
</div>

<div class="navigation_container">
  <div  class="list_navigation_links"><a href="/orgs/after/${last}${qParam}">next >></a> </div>
  <div class="list_navigation_links"><a href="/orgs/before/${first}${qParam}"><< previous</a></div>
</div>