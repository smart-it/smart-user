<%-- 
    Document   : users
    Created on : Jul 17, 2010, 1:48:03 PM
    Author     : atiqul
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User List</title>

       <link rel="Stylesheet" href="../css/userlist.css">
        
    </head>
    <body>
   
        
        <div align="center"><h1>User List</h1></div>

       
        <div align="center" style=" float: left; width: 10%; background-color: silver">
            <h3>ID</h3>
        </div>
        <div align="center" style=" float: left; width: 50%; background-color: silver">
            <h3>User Name</h3>
        </div>
        <div align="center" style="float: left;width: 20%; background-color: silver">
            <h3>Edit</h3>
        </div>
        <div align="center" style="float: left;width: 20%; background-color: silver">
            <h3>Delete</h3>
        </div>
       

        <c:forEach var="i"begin="1" end="10" >
            <c:choose>
                <c:when test="${i%2==0}">
                 

                    <div align="center" style="float: left; width:10%;background-color: white">
                        <h4><c:out value="${i}" /></h4>
                    </div>
                    <div align="center" style="float: left; width: 50%;background-color: white">
                        <h4> Russel</h4>
                    </div>
                    <div align="center" style="float: left;width: 20%; background-color: white"><a href="createuser.jsp">
                            <h4> Edit</h4></a>
                    </div>
                    <div align="center" style="float: left;width: 20%; background-color: white">
                        <h4>Delete</h4>
                    </div>
                  
                </c:when>
                <c:otherwise>
                   
                    <div align="center" style="float: left; width: 10%; background-color: #B6CEE8">
                        <h4><c:out value="${i}" /></h4>
                    </div>
                    <div align="center" style="float: left; width: 50%;  background-color: #C9DCF1">
                        <h4> Russel</h4>
                    </div>
                    <div align="center" style="float: left;width: 20%;  background-color: #B6CEE8"><a href="createuser.jsp">
                            <h4> Edit</h4></a>
                    </div>
                    <div align="center" style="float: left;width: 20%; background-color:#C9DCF1">
                        <h4>Delete</h4>
                    </div>
                   
                </c:otherwise>
            </c:choose>

        </c:forEach>



        
       



    </body>
</html>
