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
        <link type="text/css" rel="stylesheet" href="/css/organizationlist.css">
        <title><c:out value="${it.name}"></c:out>
<!--            <script type="text/javascript" src="jquery.js"></script>
            <script type="text/javascript">
                $(document).ready(function(){
                    $("submit").click(function(){
                        $(this).hide();
                    });
                });
            </script>-->
        </title>
        <!--        <link type="text/javascript" href="/css/javascript_1.js">-->



    </head>
    <body>
        <h1><c:out value="${it.name}"></c:out></h1>


        <form method="POST" action ="http://russel:9090/orgs/shortname/${it.uniqueShortName}" accept="application/json" id="organizationform">

            <div id="inner-left-1" align="right"><label>Organization Name:</label></div><div id="inner-right-1" align="left"><input type="text" name="name" size="40" value="${it.name}"></div>

            <div id="inner-left-2" align="right"><label>Unique short Name:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value="${it.uniqueShortName}"></div>

            <div id="inner-left-3" align="right"><label>Street Address:</label></div><div id="inner-right-3" align="left"><input type="text" name="streetAddress" size="40" value="${it.address.streetAddress}"></div>

            <div id="inner-left-4" align="right"><label>City:</label></div><div id="inner-right-4" align="left"><input type="text" name="city" size="40" value="${it.address.city}"></div>

            <div id="inner-left-5" align="right"><label>State:</label></div><div id="inner-right-5" align="left"><input type="text" name="state" size="40" value="${it.address.state}"></div>

            <div id="inner-left-6" align="right"><label>Country:</label></div><div id="inner-right-6" align="left"><input type="text" name="country" size="40" value="${it.address.country}"></div>

            <div id="inner-left-6" align="right"></div><div id="inner-right-6" align="left"><input type="hidden" name="id" size="40" value="${it.id}"></div>

            <div id="inner-left-6" align="right"></div><div id="inner-right-6" align="left"><input type="hidden" name="version" size="40" value="${it.version}"></div>

            <div id="inner-left-7" align="right"><label>Zip:</label></div><div id="inner-right-7" align="left"><input type="text" name="zip" size="40" value=""></div>

            <div class="inner-right-8" align="center"><input type="submit" value="update" name="update"></div>

        </form>


    </body>
</html>
