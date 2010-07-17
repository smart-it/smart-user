<%-- 
    Document   : user
    Created on : Jul 17, 2010, 12:16:18 PM
    Author     : atiqul
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Account List</title>
    </head>
    <body>

        <table border="1">
         <c:forEach var="customer" items="${grid.customers}">
             <tr>
                 <td>
                    <c:out value="${customer.id}" />
                </td>
                <td>
                     <c:out value="${customer.name}" />
                 </td>
             </tr>
          </c:forEach>
        </table>

    </body>
</html>
