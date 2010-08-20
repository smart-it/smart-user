<%-- 
    Document   : userList
    Created on : Jul 22, 2010, 3:36:48 PM
    Author     : russel
--%>

<%@page import="javax.swing.text.Document"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Collection"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@taglib prefix="pg" uri="/WEB-INF/taglib139.tld" %>--%>
<%--<%@page import="com.smartitengineering.user.domain.User"%>--%>
<%@page import="com.smartitengineering.user.domain.Organization"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

 

        <div id="leftmenu">
          <div id="leftmenu_header">User-Creation</div>
            <div id="leftmenu_body">
              <ul>
                <li><a href="javascript: Orgpageselect()">Create</a></li>
              </ul>
            </div>
        </div>


        <c:if test="${param['lang']!=null}">
          <fmt:setLocale scope="session" value="${param['lang']}"/>
        </c:if>




<%--<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
        <title>
            <div id="header"><h1>User List</h1></div>
        </title>
        <script type="text/javascript" src="/script/javascript_1.js"></script>
        <script type="text/javascript" src="/script/jquery-1.4.2.js"></script>
        <script type="text/javascript" src="/script/jquery.validate.js"></script>
        <link rel="Stylesheet" href="/css/organization-style.css">
    </head>--%>
    
    

    <%--<body>--%>

        <div class="show" id="showList">
            <div id="title_of_organization">
                <label><fmt:message key="org.usrtitle"/></label>
            </div>


            <div id="top_row">

                <div id="userslist">

                    <%--<div id="title_of_organization_users">
                        <label><fmt:message key="org.usrtitle"/></label>
                    </div>
--%>
                    

                        <div class="tableheadname_user">
                            <label class="tablehead_label"><fmt:message key="org.usrtablehead1"/></label>
                        </div>

                        <div class="tableheadname_user">
                            <label class="tablehead_label"><fmt:message key="org.usrtablehead2"/></label>
                        </div>

                    </div>
                </div>




                <div id="individual_row">
                    <div>
                        <div id="teblecontentid"></div>
                    </div>

                    <div>
                        <div id="teblecontentname"></div>
                    </div>

                </div>


                <div id="teblecontentlink"></div>


            </div>


            

        
            <div class="hide"  id="create">


                <div id="header_organization_users">
                    <label>Users Entry</label>
                </div>

                
                <form action="" accept="application/json" enctype="" id="userform" method="post">

                    <div id="inner-left-1" align="right"><label><fmt:message key="org.usrinput1"/></label></div><div id="inner-right-1" align="left"><input type="text" name="name" id="fname" size="40" value=""></div>

                    <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput2"/></label></div><div id="inner-right-2" align="left"><input type="text" name="midName" id="mname" size="40" value=""></div>

                    <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput3"/></label></div><div id="inner-right-2" align="left"><input type="text" name="lastName" id="lname" size="40" value=""></div>

                    <div id="inner-left-1" align="right"><label><fmt:message key="org.usrtablehead2"/></label></div><div id="inner-right-1" align="left"><input type="text" name="userName" id="fname" size="40" value=""></div>

                    <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput4"/></label></div><div id="inner-right-2" align="left"><input id="password" type="password" name="password" size="40" value=""></div>

                    <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput7"/></label></div><div id="inner-right-2" align="left"><input id="confirmPassword" type="password" name="confirmPassword" size="40" value=""></div>

                    <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput5"/></label></div><div id="inner-right-2" align="left"><input type="text" name="phone" id="phone" size="40" value=""></div>

                    <div id="inner-left-2" align="center"><label><fmt:message key="org.usrinput6" var="submitbtn"/></label><input name="submitbtn"  type="submit" class="submit" value="submit"></div>
                </form>


            </div>
  

  
