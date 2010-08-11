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
<%@page import="com.smartitengineering.user.domain.User"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">




<c:if test="${param['lang']!=null}">
    <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
        <title>User List</title>
        <script type="text/javascript" src="/script/javascript_1.js"></script>
        <link rel="Stylesheet" href="/css/organization-style.css">

    </head>
    <body>

        <div class="show" id="showList">
            <div id="title_of_organization">
                <label><fmt:message key="org.usrtitle"/></label>
            </div>

            <div id="top_row">

                <div id="tableheadid">
                    <label class="tablehead_label"><fmt:message key="org.usrtablehead1"/></label>
                </div>

                <div class="tableheadname">
                    <label class="tablehead_label"><fmt:message key="org.usrtablehead2"/></label>
                </div>

            </div>
                   


            <c:forEach var="user" items="${it}">
                <div id="individual_row">
                    <div id="teblecontentid">
                        <label class="tablecontent_label"> <a href="users/username/${user.username}" ><label><c:out value="${user.id}" /></label></a></label>
                    </div>

                    <div class="teblecontentname">
                        <label class="tablecontent_label"><a href="users/username/${user.username}" ><label><c:out value="${user.username}"/></label></a></label>
                    </div>

                </div>
            </c:forEach>


            <div class="tablecontent_label">
                <a href="javascript: Orgpageselect()">Create</a>
            </div>



        </div>
        <fmt:message key="org.usrinput6" var="submitbtn"/>

        <div class="hide"  id="create">

            <form action="http://russel:9090/users" method="post" accept="application/json" enctype="" id="userform">

                <div id="inner-left-1" align="right"><label><fmt:message key="org.usrinput1"/></label></div><div id="inner-right-1" align="left"><input type="text" name="name" size="40" value=""></div>

                <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput2"/></label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput3"/></label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput4"/></label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput5"/></label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>


                <div class="inner-right-8" align="center"><input type="submit" value="${submitbtn}" name="submitBtn" ></div>

            </form>

        </div>
        <div id="footer">
        </div>

    </body>
</html>
