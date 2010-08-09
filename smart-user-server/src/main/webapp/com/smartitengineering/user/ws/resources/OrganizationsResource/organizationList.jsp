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


        <div class="show" id="showList">
            <div id="title_of_organization">
                <label><fmt:message key="org.title"/></label>
            </div>

            <div id="top_row">

                <div id="tableheadid">
                    <label class="tablehead_label"><fmt:message key="org.tablehead1"/></label>
                </div>

                <div class="tableheadname">
                    <label class="tablehead_label"><fmt:message key="org.tablehead2"/></label>
                </div>

                <div class="tableheadname">
                    <label class="tablehead_label"><fmt:message key="org.tablehead3"/></label>
                </div>


                <div class="tableheadlink">
                    <label class="tablehead_label"><fmt:message key="org.tablehead4"/></label>
                </div>

                <div class="tableheadlink">
                    <label class="tablehead_label"><fmt:message key="org.tablehead5"/></label>
                </div>

            </div>


            <c:forEach var="organization" items="${it}">
                <div id="individual_row">
                    <div id="teblecontentid">
                        <label class="tablecontent_label"><c:out value="${organization.id}" /></label>
                    </div>

                    <div class="teblecontentname">
                        <label class="tablecontent_label"><c:out value="${organization.name}" /></label>
                    </div>

                    <div class="teblecontentname">
                        <label class="tablecontent_label"><c:out value="${organization.uniqueShortName}"/></label>
                        <c:set value="${organization.uniqueShortName}" var="uniqueName"></c:set>

                    </div>

                    <div class="teblecontentlink">
                        <a href="orgs/shortname/${organization.uniqueShortName}" ><label class="tablecontent_label">Edit</label></a>
                    </div>

                    <div class="teblecontentlink">
                        <a href="javascript: postwith(http://russel:9090/org)" name="del" onclick="submitform()"><label class="tablecontent_label">Delete</label></a>
                    </div>


                </div>
            </c:forEach>


            <div class="tablecontent_label">
                <a href="javascript: Orgpageselect()">Create</a>
            </div>

        </div>


                <fmt:message key="org.inputlabel1" var="update"/>

        


        <div class="hide" id="create">


                    <div id="header_organization" align="center">
                        <label id="headerogorganization">Organization Entry Form</label>
                    </div>


                    <div id="form_organizationentry" align="center">

                        <form action="http://localhost:9090/orgs" method="post" accept="application/json" enctype="" id="organizationform">

                            <div class="inner-left" ><label>Organization Name:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="name" value="" class="textField"></div>

                            <div class="inner-left"><label>Unique short Name:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="uniqueShortName" value="" class="textField"></div>

                            <div class="inner-left"></div><div class="inner-right" align="left"><label>!!!! already exists!!! try another!!!</label></div>

                            <div class="inner-left"><label>Street Address:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="streetAddress" value="" class="textField"></div>

                            <div class="inner-left"><label>City:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="city" value="" class="textField"></div>

                            <div class="inner-left"><label>State:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="state" value="" class="textField"></div>

                            <div class="inner-left"><label>Country:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="country" value="" class="textField"></div>

                            <div class="inner-left"><label>Zip:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="zip" value="" class="textField"></div>

                            <div style="clear: both">
                            </div>
                            <div id="btnfield" align="center"><input type="submit" value="submit" name="submitBtn"></div>
                            <div style="clear: both">
                            </div>

                        </form>

                    </div>
            </div>