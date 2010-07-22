<%-- 
    Document   : userList
    Created on : Jul 22, 2010, 3:36:48 PM
    Author     : russel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User List</title>
    </head>
    <body>
        <table border="1">
        <c:forEach var="user" items="${it}">
            <tr>
                <td>
                    <c:out value="${user.username}" />

                </td>
                <td>
                    <c:out value="${user.password}" />
                </td>                
            </tr>
        </c:forEach>
            </table>
    </body>
</html>
