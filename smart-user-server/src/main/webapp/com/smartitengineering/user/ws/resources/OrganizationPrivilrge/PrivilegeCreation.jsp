<%-- 
    Document   : PrivilegeCreation
    Created on : Aug 5, 2010, 5:22:27 PM
    Author     : saumitra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="org.privCreationTitle" /></title>
    </head>
    <body>

        <div id="form_privilege" align="center">

            <form action="<c:url value="/orgs/prives"/>" method="post" accept="application/json" enctype="" id="privilegeform">

                <div id="inner-left-1" align="right"><label><fmt:message key="org.privLevel1" /></label></div><div id="inner-right-1" align="left"><input type="text" name="name" size="40"></div>

                <div id="inner-left-2" align="right"><label><fmt:message key="org.privLevel2" /></label></div><div id="inner-right-2" align="left"><input type="text" name="displayName" size="40" value=""></div>

                <div id="inner-left-3" align="right"><label><fmt:message key="org.privLevel3" /></label></div><div id="inner-right-3" align="left"><input type="text" name="shortdes" size="40" value=""></div>

                <div id="inner-left-4" align="right"><label><fmt:message key="org.privLevel4" /></label></div><div id="inner-right-4" align="left"><input type="text" name="secureObject" size="40" value=""></div>

                <div id="inner-left-5" align="right"><label><fmt:message key="org.privLevel5" /></label></div><div id="inner-right-5" align="left"><input type="text" name="permissionMask" size="40" value=""></div>

                <div id="inner-left-6" align="right"><label><fmt:message key="org.privLevel6" /></label></div><div id="inner-right-6" align="left"><input type="text" name="parentOrganizationId" size="40" value=""></div>

                <div id="inner-left-7" align="right"><label><fmt:message key="org.privLevel7" /></label></div><div id="inner-right-7" align="left"><input type="text" name="secureObjectId" size="40" value=""></div>

                <div id="inner-left-7" align="right"><label><fmt:message key="org.privLevel1" /></label></div><div id="inner-right-7" align="left"><input type="text" name="lastModified" size="40" value=""></div>

                <div class="inner-right-8" align="center"><input type="submit" value="<fmt:message key="org.privSubmitBtn"/>" name="submitBtn"></div>

            </form>

        </div>
    </body>
</html>
