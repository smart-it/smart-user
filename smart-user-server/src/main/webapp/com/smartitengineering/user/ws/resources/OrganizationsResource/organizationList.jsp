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

<div id="rightmenu">
  <div id="rightmenu_header">Organization-Creation</div>
  <div id="rightmenu_body">
    <ul>
      <li><a href="javascript: Orgpageselect()">Create</a></li>
    </ul>
  </div>
</div>

<c:if test="${param['lang']!=null}">
  <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>


<div class="show" id="showList">

  <div id="title_of_organization">
    <label><fmt:message key="org.title"/></label>
  </div>

  <div id="top_row">

    <div class="tableheadname">
      <label class="tablehead_label"><fmt:message key="org.tablehead3"/></label>
    </div>


    <div class="tableheadname">
      <label class="tablehead_label"><fmt:message key="org.tablehead2"/></label>
    </div>

  </div>


  <c:forEach var="organization" items="${it}">
    <div id="individual_row">

      <div class="tablecontentname">
        <a href="orgs/shortname/${organization.uniqueShortName}" target="_blank"><label class="tablecontent_label" id=""></label></a>
      </div>

      <div class="tablecontentname">
        <a href="orgs/shortname/${organization.uniqueShortName}" target="_blank"><label class="tablecontent_label" id="tableContentTitle"></label></a>
      </div>

    </div>
  </c:forEach>

</div>

<fmt:message key="org.inputlabel1" var="update"/>


<div class="hide" id="create">

  <div id="header_organization">
    <label>Organization Entry Form</label>
  </div>

  <div id="form_organizationentry">

    <form action="http://localhost:9090/orgs" method="post" accept="application/json" enctype="" id="organizationform">

      <div class="inner-left" ><label><fmt:message key="org.tablehead2"/></label></div>
      <div class="inner-right"><input type="text" name="name" value="" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left"><label><fmt:message key="org.tablehead3"/></label></div>
      <div class="inner-right"><input type="text" name="uniqueShortName" value="" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left"></div><div class="inner-right" align="left"><label>!!!! already exists!!! try another!!!</label></div>

      <div class="inner-left"><label><fmt:message key="org.inputlabel3"/></label></div>
      <div class="inner-right"><input type="text" name="streetAddress" value="" class="textField"></div>
      <div class="clear"></div>


      <div class="inner-left"><label><fmt:message key="org.inputlabel4"/></label></div>
      <div class="inner-right"><input type="text" name="city" value="" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left"><label><fmt:message key="org.inputlabel5"/></label></div>
      <div class="inner-right"><input type="text" name="state" value="" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left"><label><fmt:message key="org.inputlabel6"/></label></div>
      <div class="inner-right"><input type="text" name="country" value="" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left"><label><fmt:message key="org.inputlabel7"/></label></div>
      <div class="inner-right"><input type="text" name="zip" value="" class="textField"></div>
      <div class="clear"></div>

      <div id="btnfield"><input type="submit" value="submit" name="submitBtn"></div>
      <div class="clear"></div>

    </form>

  </div>
</div>