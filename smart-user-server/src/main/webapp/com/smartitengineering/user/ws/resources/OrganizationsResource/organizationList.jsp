<%-- 
    Document   : organizationList
    Created on : Jul 22, 2010, 2:43:43 PM
    Author     : russel
--%>

<%@page import="java.util.Collection"%>
<%@page import="com.smartitengineering.user.domain.Organization"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">



<c:if test="${param['lang']!=null}">
    <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>




<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link type="text/css" rel="stylesheet" href="/css/organizationlist.css"/>
        <script type="text/javascript" src="/script/javascript_1.js">
        </script>

        <title>            
            Organizations
        </title>





    </head>
    <body>

        <div class="show" id="showList" style="width: 80%;position: relative;clear: left;border: 1px solid;">
            <div style="background-color: #77e445">
            <h1><fmt:message key="org.title"/></h1>
            </div>

            <div style="text-decoration: underline;border-color: #13200d">

                <div id="tableheadid">
                    <h4><fmt:message key="org.tablehead1"/></h4>
                </div>

                <div class="tableheadname">
                    <h4><fmt:message key="org.tablehead2"/></h4>
                </div>

                <div class="tableheadname">
                    <h4><fmt:message key="org.tablehead3"/></h4>
                </div>


                <div class="tableheadlink">
                    <h4><fmt:message key="org.tablehead4"/></h4>
                </div>

                <div class="tableheadlink">
                    <h4><fmt:message key="org.tablehead5"/></h4>
                </div>

            </div>

            
<!--                <input type="hidden" name="id" size="40">-->
           



            <c:forEach var="organization" items="${it}">
                <div>
                    <div id="teblecontentid">
                        <h4><c:out value="${organization.id}" /></h4>
                    </div>

                    <div class="teblecontentname">
                        <h4><c:out value="${organization.name}" /></h4>

                    </div>
                    <div class="teblecontentname">
                        <h4><c:out value="${organization.uniqueShortName}"/></h4>
                        <c:set value="${organization.uniqueShortName}" var="uniqueName"></c:set>

                    </div>

                    <div class="teblecontentlink">
                        <a href="orgs/shortname/${organization.uniqueShortName}" ><h4>Edit</h4></a>
                    </div>
<!--                        <form method="POST" name="deleteform" action="http://russel:9090/org/shortname/${organization.uniqueShortName}">-->
                    <div class="teblecontentlink">
<!--                        <input type="text" name="delete" value="delete">-->
                        <h4><a href="javascript: postwith(http://russel:9090/org)" name="del" onclick="submitform()">Delete</a></h4>
                    </div>
<!--                     </form>-->

                </div>
            </c:forEach>

            <div>
            <h4><a href="javascript: Orgpageselect()">Create</a></h4>
        </div>
        </div>
                <fmt:message key="org.inputlabel1" var="update"/>
        
<!--        <div class="hide" id="create">
            <form action="http://russel:9090/orgs" method="post" accept="application/json" enctype="" id="organizationform">

                        <div id="inner-left-1" align="right"><label><fmt:message key="org.inputlabel1"/></label></div><div id="inner-right-1" align="left"><input type="text" name="name" size="40" value=""></div>

                        <div id="inner-left-2" align="right"><label><fmt:message key="org.inputlabel2"/></label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                        <div id="inner-left-3" align="right"><label><fmt:message key="org.inputlabel3"/></label></div><div id="inner-right-3" align="left"><input type="text" name="streetAddress" size="40" value=""></div>

                        <div id="inner-left-4" align="right"><label><fmt:message key="org.inputlabel4"/>:</label></div><div id="inner-right-4" align="left"><input type="text" name="city" size="40" value=""></div>

                        <div id="inner-left-5" align="right"><label><fmt:message key="org.inputlabel5"/></label></div><div id="inner-right-5" align="left"><input type="text" name="state" size="40" value=""></div>

                        <div id="inner-left-6" align="right"><label><fmt:message key="org.inputlabel6"/></label></div><div id="inner-right-6" align="left"><input type="text" name="country" size="40" value=""></div>

                        <div id="inner-left-7" align="right"><label><fmt:message key="org.inputlabel7"/></label></div><div id="inner-right-7" align="left"><input type="text" name="zip" size="40" value=""></div>

                        <div class="inner-right-8" align="center"><input type="submit" value="updatebt" name="submit"></div>

              </form>
        </div>-->
    </body>
</html>
