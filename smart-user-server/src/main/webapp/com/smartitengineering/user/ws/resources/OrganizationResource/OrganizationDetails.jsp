<%-- 
    Document   : OrganizationDetails
    Created on : Jul 24, 2010, 12:55:21 PM
    Author     : russel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.smartitengineering.user.domain.Organization" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><c:out value="${it.name}"></c:out></title>
    </head>
    <body>        

        <c:out value="${it.name}"></c:out>
        <br>
        <c:out value="${it.uniqueShortName}"></c:out>
        <br>
        <c:out value="${it.address.streetAddress}"></c:out>
        <br>
        <c:out value="${it.address.city}"></c:out>
        <br>
        <c:out value="${it.address.state}"></c:out>
        <br>
        <c:out value="${it.address.country}"></c:out>
        <br>

    </body>
</html>
