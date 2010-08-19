<%-- 
    Document   : organizationList
    Created on : Jul 22, 2010, 2:43:43 PM
    Author     : russel
--%>

<%@page import="java.util.Collection"%>
<%--<%@page import="com.smartitengineering.user.domain.Organization"%>--%>
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

       

        <div class="tableheadname">
            <label class="tablehead_label"><fmt:message key="org.tablehead2"/></label>
        </div>

        <div class="tableheadname">
            <label class="tablehead_label"><fmt:message key="org.tablehead3"/></label>
        </div>

    </div>

    
        <div id="individual_row">

            <div>
                <div id="teblecontentname"></div>
            </div>

            <div>
                <div    id="teblecontentid"></div>
                <c:set value="${organization.uniqueShortName}" var="uniqueName"></c:set>

            </div>

        </div>


    <div id="teblecontentlink" ></div>



    


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
        <fmt:message key="org.usrinput6" var="submitbtn"/>
        <form action="http://russel:9090/orgs" method="post" accept="application/json" enctype="" id="organizationform">

            <div class="inner-left" ><label><fmt:message key="org.inputlabel1"/></label></div>
            <div class="inner-right" align="left"><input type="text" name="name" value="" class="textField"></div>

            <div class="inner-left"><label><fmt:message key="org.inputlabel2"/></label></div>
            <div class="inner-right" align="left"><input type="text" name="uniqueShortName" Id="uniqueShortName" value="" class="textField"></div>

            <div class="inner-left"></div><div class="inner-right" align="left"><label>!!!! already exists!!! try another!!!</label></div>

            <div class="inner-left"><label><fmt:message key="org.inputlabel3"/></label></div>
            <div class="inner-right" align="left"><input type="text" name="streetAddress" value="" class="textField"></div>


            <div class="inner-left"><label><fmt:message key="org.inputlabel4"/></label></div>
            <div class="inner-right" align="left"><input type="text" name="city" value="" class="textField"></div>

            <div class="inner-left"><label><fmt:message key="org.inputlabel5"/></label></div>
            <div class="inner-right" align="left"><input type="text" name="state" value="" class="textField"></div>

            <div class="inner-left"><label><fmt:message key="org.inputlabel6"/></label></div>
            <div class="inner-right" align="left"><input type="text" name="country" value="" class="textField"></div>

            <div class="inner-left"><label><fmt:message key="org.inputlabel7"/></label></div>
            <div class="inner-right" align="left"><input type="text" name="zip" value="" class="textField"></div>

            <div style="clear: both">
            </div>
            <div id="btnfield" align="center"><input type="submit" value="submit" name="submitbtn" onclick="isEmpty()" onmouseover="onmouse_over()" id="submit"></div>
            <div style="clear: both">
            </div>

        </form>

    </div>
</div>