<%-- 
    Document   : organizationList
    Created on : Jul 22, 2010, 2:43:43 PM
    Author     : russel
--%>

<%@page import="java.util.Collection"%>
<%@page import="com.smartitengineering.user.domain.Organization"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">


        <div class="show" id="showList">

            <div style="background-color: #77e445; border: 1px solid;">
                <h1 id="header" >Organization</h1>
            </div>

            <div style="border: 1px solid">

                <div id="tableheadid">
                    <h4> ID</h4>
                </div>

                <div class="tableheadname">
                    <h4>Organization name</h4>
                </div>

                <div class="tableheadname">
                    <h4>Organization</h4>
                </div>


                <div class="tableheadlink">
                    <h4>Edit</h4>
                </div>

                <div class="tableheadlink">
                    <h4> Delete</h4>
                </div>

            </div>


            <c:forEach var="organization" items="${it}">
                <div style="border: 1px solid">
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
                        <form method="POST" name="${organization.uniqueShortName}" action="http://russel:9090/orgs/shortname/${organization.uniqueShortName}">
                    <div class="teblecontentlink">
                        <h4><a href="javascript: submitform()" name="del">Delete</a></h4>
                    </div>
                     </form>

                </div>
            </c:forEach>

        </div>


            <div style="border: 1px solid">
                <a href="javascript: Orgpageselect()">Create</a>
            </div>
        


        <div class="hide" id="create">
  <%--          <form action="http://russel:9090/organizations" method="post" accept="application/json" enctype="" id="organizationform">

                        <div id="inner-left-1" align="right"><label>Organization Name:</label></div><div id="inner-right-1" align="left"><input type="text" name="name" size="40" value=""></div>

                        <div id="inner-left-2" align="right"><label>Unique short Name:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                        <div id="inner-left-3" align="right"><label>Street Address:</label></div><div id="inner-right-3" align="left"><input type="text" name="streetAddress" size="40" value=""></div>

                        <div id="inner-left-4" align="right"><label>City:</label></div><div id="inner-right-4" align="left"><input type="text" name="city" size="40" value=""></div>

                        <div id="inner-left-5" align="right"><label>State:</label></div><div id="inner-right-5" align="left"><input type="text" name="state" size="40" value=""></div>

                        <div id="inner-left-6" align="right"><label>Country:</label></div><div id="inner-right-6" align="left"><input type="text" name="country" size="40" value=""></div>

                        <div id="inner-left-7" align="right"><label>Zip:</label></div><div id="inner-right-7" align="left"><input type="text" name="zip" size="40" value=""></div>

                        <div class="inner-right-8" align="center"><input type="submit" value="submit" name="submitBtn"></div>

             </form>--%>

                    <div id="header_organization" align="center">
                        <label id="headerogorganization">Organization Entry Form</label>
                    </div>


                    <div id="form_organizationentry" align="center">

                        <form action="http://russel:9090/organizations" method="post" accept="application/json" enctype="" id="organizationform">

                            <div class="inner-left" align="left"><label>Organization Name:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="name" value="" class="textField"></div>

                            <div class="inner-left" align="left"><label>Unique short Name:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="uniqueShortName" value="" class="textField"></div>

                            <div class="inner-left"></div><div class="inner-right" align="left"><label>!!!! already exists!!! try another!!!</label></div>

                            <div class="inner-left" align="left"><label>Street Address:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="streetAddress" value="" class="textField"></div>

                            <div class="inner-left" align="left"><label>City:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="city" value="" class="textField"></div>

                            <div class="inner-left" align="left"><label>State:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="state" value="" class="textField"></div>

                            <div class="inner-left" align="left"><label>Country:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="country" value="" class="textField"></div>

                            <div class="inner-left" align="left"><label>Zip:</label></div>
                            <div class="inner-right" align="left"><input type="text" name="zip" value="" class="textField"></div>

                            <div style="clear: both">
                            </div>
                            <div id="btnfield" align="center"><input type="submit" value="submit" name="submitBtn"></div>
                            <div style="clear: both">
                            </div>

                        </form>

                    </div>
            </div>