<%-- 
    Document   : organizationList
    Created on : Jul 22, 2010, 2:43:43 PM
    Author     : russel
--%>

<%@page import="java.util.Collection"%>
<%@page import="com.smartitengineering.user.domain.Organization"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <table>
        <c:forEach var="organization" items="${it}">
            <tr>
                <td>
                    <c:out value="${organization.name}" />

                </td>
                <td>
                    <c:out value="${organization.uniqueShortName}" />
                </td>
                <td>
                    <c:out value="${organization.address.streetAddress}" />
                </td>
                <td>
                    <c:out value="${organization.address.city}" />
                </td>
                <td>
                    <c:out value="${organization.address.state}" />
                </td>
                <td>
                    <c:out value="${organization.address.country}" />
                </td>
            </tr>
        </c:forEach>
            </table>

    </body>
</html>
