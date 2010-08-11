<%-- 
    Document   : OrganizationUserDetails
    Created on : Jul 24, 2010, 1:30:52 PM
    Author     : russel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.smartitengineering.user.domain.User" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><c:out value="${it.username}"></c:out></title>
        <link rel="Stylesheet" href="/css/organization-style.css">
        <script type="text/javascript" src="/script/javascript_1.js"></script>
    </head>
    <body>
        

        
  <h1><c:out value="${it.username}"></c:out></h1>



  <div id="showList" class="show">
         <div class=""><label>Name:</label></div><label>${it.username}</label><div class=""></div>
    <div class=""><label>password:</label></div><div class=""><label>${it.password}</label></div>
  </div>
    <div><a href="javascript: Orgpageselect()">Edit</a></div>



     <div id="create" class="hide">


        <div id="form_organizationentry" align="center">

        <form method="POST" action ="http://russel:9090/orgs/shortname/${it.username}" accept="application/json" id="organizationform">

            <div class="inner-left"><label>Name:</label></div>
            <div class="inner-right" align="left"><input type="text" name="name" value="${it.username}" class="textField"></div>

            <div class="inner-left"><label>password:</label></div>
            <div class="inner-right" align="left"><input type="text" name="uniqueShortName" value="${it.password}" class="textField"></div>

            

            <div style="clear: both"></div>
            <div id="btnfield" align="center"><input type="submit" value="UPDATE" name="submitBtn"></div>
            <div style="clear: both"></div>

        </form>

        </div>

     </div>

    </body>
</html>
