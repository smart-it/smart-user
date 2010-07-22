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
        <LINK REL=StyleSheet href="organizationlist.css" TYPE="text/css" MEDIA=screen>
    </head>
    <body>




        <div style="width: 80%;position: relative;clear: left">
        <div style="background-color: #77e445">
            <h1 id="header" >Organization</h1>
        </div>

        <div style="text-decoration: underline;border-color: #13200d">

        <div id="tableheadid">
         <h4> ID</h4>
        </div>

        <div id="tableheadname">
        <h4>Organization name</h4>
        </div>

        <div class="tableheadlink">
        <h4>Edit</h4>
        </div>

        <div class="tableheadlink">
        <h4> Delete</h4>
        </div>

        </div>


        
        
        <c:forEach var="organization" items="${it}">
         <div>
          <div id="teblecontentid">
              <h4>  <c:out value="${organization}" /></h4>
          </div>

          <div id="teblecontentname">
              <h4><c:out value="${organization.name}" /></h4>
          </div>

          <div class="teblecontentlink">
              <h4><a href="organization-edit.jsp">Edit</a></h4>
          </div>

          <div class="teblecontentlink">
                <h4><a href="Jstl_Core_Tags.jsp?valid=true&name=eric&mark=8">Delete</a></h4>
          </div>

          </div>
        </c:forEach>
           
        </div>
    </body>
</html>
