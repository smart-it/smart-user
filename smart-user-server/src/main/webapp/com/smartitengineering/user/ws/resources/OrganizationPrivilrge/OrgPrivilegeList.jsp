<%-- 
    Document   : OrgPrivilegeList
    Created on : Aug 5, 2010, 5:15:18 PM
    Author     : saumitra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Collection"%>
<%@page import="com.smartitengineering.user.domain.Privilege"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <c:forEach var="privilege" items="${it}">
            <c:out value="${it.name}"></c:out>
        </c:forEach>>
    </body>
</html>
