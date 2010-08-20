<%-- 
    Document   : OrgSecuredObjectsList
    Created on : Aug 5, 2010, 7:12:08 PM
    Author     : russel
--%>

<%@page import="java.util.Collection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.smartitengineering.user.domain.SecuredObject"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">



<c:if test="${param['lang']!=null}">
    <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Organization Secured Object</title>
        <link type="text/css" rel="stylesheet" href="/css/securedObjectList.css"/>
        <script type="text/javascript" src="/script/javascript_1.js"></script>
    </head>
    <body>
        <div class="show" id="showList" style="width: 80%;position: relative;clear: left">
            <div style="background-color: #77e445">
                <h2 id="header"><fmt:message key="org.titleSo"/></h2>
            </div>
            <div style="text-decoration: underline;border-color: #13200d">
                <div class="tableheadname">
                    <h4><fmt:message key="org.tablehead1"/></h4>
                </div>
                <div class="tableheadPO">
                    <h4> <fmt:message key="org.tableheadPO"/></h4>
                </div>
                <div class="tableheadorg">
                    <h4> <fmt:message key="org.tableheadOrg"/></h4>
                </div>
                <div class="tableheaddetails">
                    <h4> <fmt:message key="org.tableheadDt"/></h4>
                </div>
            </div>
        </div>
        <div>
            <a href="javascript: Orgpageselect()"><h4>Create</h4></a>
        </div>

        <div class="hide" id="create" align="center">
            <form action="http://russel:9090/orgs/privs" method="post" accept="application/json" enctype="" id="securedobjectform">
                <div id="inner-left-1" align="right">
                    <label><fmt:message key="org.inputlabel1"/></label></div><div id="inner-right-1" align="left"><input type="text" name="name" size="40"></div>
                <div id="inner-left-2" align="right"><label><fmt:message key="org.inputlabelOI"/></label></div><div id="inner-right-2" align="left"><input type="text" name="objectId" size="40" value=""></div>
                <div id="inner-left-3" align="right"><label><fmt:message key="org.inputlabelPO"/></label></div><div id="inner-right-3" align="left"><input type="text" name="parentObject" size="40" value=""></div>
                <div id="inner-left-4" align="right"><label><fmt:message key="org.inputlabelOrgs"/></label></div><div id="inner-right-4" align="left"><input type="text" name="organization" size="40" value=""></div>
                <div id="inner-left-6" align="right"><label><fmt:message key="org.inputlabelPOI"/></label></div><div id="inner-right-6" align="left"><input type="text" name="organizationId" size="40" value=""></div>
                <div id="inner-left-7" align="right"><label><fmt:message key="org.inputlabelLMD"/></label></div><div id="inner-right-7" align="left"><input type="text" name="lastModifiedDate" size="40" value=""></div>
                <fmt:message key="org.inputlabel" var="sbmitBtn"/>
                <div class="inner-right-8" align="center"><input type="submit" value="submit" name="${submitBtn}"></div>
            </form>
        </div>
    </body>
</html>
