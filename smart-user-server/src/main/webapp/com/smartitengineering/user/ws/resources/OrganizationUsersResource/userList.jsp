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

            function UserInf()
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
              }
             
 

        </script>
    </head>
    <body>
        <div class="show"  id="showList">
        
        <div id="header"><h1>User List</h1></div>
        <div id="tablebar">
            <table>
                <tr>
                    <th width="100px" style="border-right-style: double; font-size: 20px">ID</th>
                    <th width="500px" style="border-right-style:  double; font-size: 20px">User Name</th>
                    <th  style="font-size: 20px"><input type="button" value="Edit" id="button" onclick="UserInf()"></th>
                    <th style="font-size: 20px"><input type="button" value="Delete" id="button" onclick=""></th>
                </tr>
            </table>
        </div>

       
        <c:forEach var="user" items="${it}">
           <div id="content" style=" width: 100px; margin-left: 200px">  <c:out value="${user.id}"/></div>
            <div id="content"style="width: 500px"><c:out value="${user.username}" /></div>

            <div id="content" style="width: 200px"><input type="radio"></div>
            <div id="content"><input type="checkbox"></div>
           
        </c:forEach>
        
            <div id="footer">
                copyright@smartitengineering ltd.
            </div>
        </div>
        <div class="hide"  id="edit">
            
                <form action="http://russel:9090/users" method="post" accept="application/json" enctype="" id="userform">

                        <div id="inner-left-1" align="right"><label>First Name:</label></div><div id="inner-right-1" align="left"><input type="text" name="name" size="40" value=""></div>

                        <div id="inner-left-2" align="right"><label>Middle Name:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                        <div id="inner-left-2" align="right"><label>Last Name:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                        <div id="inner-left-2" align="right"><label>Password:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                       <%-- <div id="inner-left-2" align="right"><label>Father Name:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                        <div id="inner-left-2" align="right"><label>Mother Name:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                        <div id="inner-left-2" align="right"><label>National ID:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>--%>

                        <div id="inner-left-2" align="right"><label>Cell Phone Number:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                       <%-- <div id="inner-left-3" align="right"><label>Street Address:</label></div><div id="inner-right-3" align="left"><input type="text" name="streetAddress" size="40" value=""></div>

                        <div id="inner-left-4" align="right"><label>City:</label></div><div id="inner-right-4" align="left"><input type="text" name="city" size="40" value=""></div>

                        <div id="inner-left-5" align="right"><label>State:</label></div><div id="inner-right-5" align="left"><input type="text" name="state" size="40" value=""></div>

                        <div id="inner-left-6" align="right"><label>Country:</label></div><div id="inner-right-6" align="left"><input type="text" name="country" size="40" value=""></div>

                        <div id="inner-left-7" align="right"><label>Zip:</label></div><div id="inner-right-7" align="left"><input type="text" name="zip" size="40" value=""></div>
--%>
        <div class="inner-right-8" align="center"><input type="submit" value="submit" name="submitBtn" ></div>
        <div id="formfooter">  copyright@smartitengineering ltd.</div>

                    </form>
           
            </div>
       
    </body>
</html>
