<%-- 
    Document   : userList
    Created on : Jul 22, 2010, 3:36:48 PM
    Author     : russel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
        <title>User List</title>
        <link rel="Stylesheet" href="/css/user.css" type="text/css" >
        <script type="text/javascript">

            <%--function UserInf()
            {
               var className=document.getElementById("showList") ;
            

               if(className.className=="show")
                   {
                       document.getElementById("showList").className="hide";
                       document.getElementById("edit").className="show";
                   }
                   else
                       {
                           document.getElementById("showList").className="show";
                           document.getElementById("edit").className="hide";
                       }
            }
                var d = new Date();
            var time = d.getHours();

            if (time > 10)
              {
              document.write("<b>Good morning</b>");
              }--%>
             
 

        </script>
    </head>
    <body>
        <div class="show"  id="showList">
        
        <div id="header"><h1>User List</h1></div>
        <div id="tablebar">
            <table>
                <tr>
                    <th width="100px" style="border-right-style: double; font-size: 20px">ID</th>
                    <th width="500px" style=" font-size: 20px">User Name</th>
                    <%--<th  style="font-size: 20px"><a href="users/username/${user.username}">Edit</a></th>
                    <th style="font-size: 20px"><input type="button" value="Delete" id="button" onclick=""></th>--%>
                </tr>
            </table>
        </div>

       
        <c:forEach var="user" items="${it}">
           <div id="content" style=" width: 100px; margin-left: 200px">  <c:out value="${user.id}"/></div>
            <div id="content"style="width: 500px"><c:out value="${user.username}" /></div>

            <div id="content" style="width: 200px"><a href="users/username/${user.username}">Edit</a></div>
            <div id="content"><a href="">Delete</a></div>
           
        </c:forEach>
        
            <div id="footer">
                copyright@smartitengineering ltd.
            </div>
        </div>
        
    </body>
</html>
