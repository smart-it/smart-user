<%-- 
    Document   : OrganizationDetails
    Created on : Jul 24, 2010, 12:55:21 PM
    Author     : russel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.smartitengineering.user.domain.Organization" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.Collection"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">

<div id="leftmenu_orgdetails_1" class="leftmenu">
  <div id="leftmenu_header_orgdetails_1" class="leftmenu_header"><label>Organization</label></div>

  <div id="leftmenu_body_orgdetails_1" class="leftmenu_body">
  <ul>

    <li><a href="javascript: Orgpageselect()">Edit</a></li>
    <li><a href="#">Delete</a></li>
    <li><a href="/orgs/${it.uniqueShortName}/users">UserList</a></li>

  </ul>
  </div>

</div>


<div id="showList" class="show">

  <c:if test="${param['lang']!=null}">
    <fmt:setLocale scope="session" value="${param['lang']}"/>
  </c:if>


  <div id="individual_org_details_header"><label><c:out value="${it.name}"></label></c:out></div>

  <div id="individual_org_details_content">

    <div class="individual_org_label"><label><fmt:message key="org.tablehead2"/></label></div>
    <div class="individual_org_data"><label>${it.name}</label></div>
    <div class="clear"></div>

    <div class="individual_org_label"><label><fmt:message key="org.tablehead3"/></label></div>
    <div class="individual_org_data"><label>${it.uniqueShortName}</label></div>
    <div class="clear"></div>

    <div class="individual_org_label"><label><fmt:message key="org.inputlabel3"/></label></div>
    <div class="individual_org_data"><label>${it.address.streetAddress}</label></div>
    <div class="clear"></div>

    <div class="individual_org_label"><label><fmt:message key="org.inputlabel4"/></label></div>
    <div class="individual_org_data"><label>${it.address.city}</label></div>
    <div class="clear"></div>

    <div class="individual_org_label"><label><fmt:message key="org.inputlabel5"/></label></div>
    <div class="individual_org_data"><label>${it.address.state}</label></div>
    <div class="clear"></div>

    <div class="individual_org_label"><label><fmt:message key="org.inputlabel6"/></label></div>
    <div class="individual_org_data"><label>${it.address.country}</label></div>
    <div class="clear"></div>

    <div class="individual_org_label"><label><fmt:message key="org.inputlabel7"/></label></div>
    <div class="individual_org_data"><label>${it.address.zip}</label></div>
    <div class="clear"></div>

  </div>

</div>



<div id="create" class="hide">

  <div id="header_organization"><label id="headerogorganization"><c:out value="${it.name}"></c:out></label></div>

  <fmt:message key="org.usrinput6" var="submitbtn"/>

  <div id="form_organizationentry">

    <form method="POST" action ="http://localhost:9090/orgs/shortname/${it.uniqueShortName}" accept="application/json" id="organizationform">

      <div class="inner-left"><label><fmt:message key="org.tablehead2"/></label></div>
      <div class="inner-right"><input type="text" name="name" value="${it.name}" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left"><label><fmt:message key="org.tablehead3"/></label></div>
      <div class="inner-right"><input type="text" name="uniqueShortName" value="${it.uniqueShortName}" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left"><label><fmt:message key="org.inputlabel3"/></label></div>
      <div class="inner-right"><input type="text" name="streetAddress" value="${it.address.streetAddress}" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left"><label><fmt:message key="org.inputlabel4"/></label></div>
      <div class="inner-right"><input type="text" name="city" value="${it.address.city}" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left"><label><fmt:message key="org.inputlabel5"/></label></div>
      <div class="inner-right"><input type="text" name="state" value="${it.address.state}" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left"><label><fmt:message key="org.inputlabel6"/></label></div>
      <div class="inner-right"><input type="text" name="country" value="${it.address.country}" class="textField"></div>
      <div class="clear"></div>

      <div class="inner-left"><label><fmt:message key="org.inputlabel7"/></label></div>
      <div class="inner-right"><input type="text" name="zip"  class="textField" value="${it.address.zip}"></div>
      <div class="clear"></div>


      <div></div>
      <div><input type="hidden" name="id" value="${it.id}"></div>
      <div class="clear"></div>

      <div></div>
      <div><input type="hidden" name="version" value="${it.version}"></div>

      <div class="clear"></div>
      <div class="btnfield"><input type="submit" value="DELETE" name="submitbtn" class="submitbtn"></div>
      <div class="btnfield"><input type="submit" value="UPDATE" name="submitbtn" class="submitbtn"></div>
      <div class="clear"></div>

    </form>

  </div>

</div>