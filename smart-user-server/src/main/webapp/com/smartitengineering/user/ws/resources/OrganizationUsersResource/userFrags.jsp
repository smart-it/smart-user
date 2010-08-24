<%-- 
    Document   : userFrags
    Created on : Aug 21, 2010, 2:26:17 PM
    Author     : russel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.smartitengineering.user.domain.UserPerson"%>


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

<div id="userListRootDiv">
  <div class="tableList" id="userListContainer">
    <c:forEach var="userPerson" items="${it}" varStatus="status">
      <div id="userRow${status.index}" class="row_of_list">
        <c:if test="${status.first}">
          <c:set var="first" value="${userPerson.user.username}"></c:set>
        </c:if>
        <c:if test="${status.last}">
          <c:set var="last" value="${userPerson.user.username}"></c:set>
        </c:if>
        <c:set var="firstName" value="${userPerson.person.self.name.firstName}"></c:set>
        <c:set var="middleInitial" value="${userPerson.person.self.name.middleInitial}"></c:set>
        <c:set var="lastName" value="${userPerson.person.self.name.lastName}"></c:set>
        <div id="userId${status.index}" class="orgName_column"><a href="users/username/${userPerson.user.username}">${userPerson.user.id}</a></div>
        <div id="username${status.index}" class="orgShortName_column"><a href="users/username/${userPerson.user.username}">${userPerson.user.username}</a></div>
        <div id="userFullName${status.index}" class="orgShortName_column"><a href="users/username/${userPerson.user.username}">${firstName} ${middleInitial} ${lastName}</a></div>
      </div>
    </c:forEach>
  </div>

  <div class="navigation_container" id="linkcontainer">
    <div id="nextUsersLinkCont" class="list_navigation_links"><a href="users/after/${last}/frags${qParam}">next >></a> </div>
    <div id="previousUsersLinkCont" class="list_navigation_links"><a href="users/before/${first}/frags${qParam}"><< previous</a></div>
  </div>
</div>
