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

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="/css/organizationlist.css">
        <title>            
            Organizations  
        </title>
        <script language="javascript" type ="text/javascript">
            function Orgpageselect()
                    {
                        var className=document.getElementById("showList");
                        //document.write(className.className);
                        if(className.className=="show")
                           {
                                document.getElementById("showList").className="hide";
                                document.getElementById("edit").className="show";
                            }
                        else
                            {
                                document.getElementById("showList").className="show";
                                document.getElementById("edit").className="hide";
                            }
                    }
//                    document.write("TEST");
        </script>
        

    </head>
    <body>

        <div class="show" id="showList" style="width: 80%;position: relative;clear: left">
                    <div style="background-color: #77e445">
                        <h1 id="header" >Organization</h1>
                    </div>

                    <div style="text-decoration: underline;border-color: #13200d">

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
                          <a href="organizations/${organization.uniqueShortName}" name=""><h4>Edit</h4></a>
                      </div>

                      <div class="teblecontentlink">
                            <h4><a href="Jstl_Core_Tags.jsp?valid=true&name=eric&mark=8">Delete</a></h4>
                      </div>

                      </div>
                    </c:forEach>
           
        </div>
        <div id="edit" class="hide">






            <form method="post" action="organizations/${uniqueName}">

                        <div id="inner-left-1" align="right"><label>Organization Name:</label></div><div id="inner-right-1" align="left"><input type="text" name="name" size="40" value=""></div>

                        <div id="inner-left-2" align="right"><label>Unique short Name:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                        <div id="inner-left-3" align="right"><label>Street Address:</label></div><div id="inner-right-3" align="left"><input type="text" name="streetAddress" size="40" value=""></div>

                        <div id="inner-left-4" align="right"><label>City:</label></div><div id="inner-right-4" align="left"><input type="text" name="city" size="40" value=""></div>

                        <div id="inner-left-5" align="right"><label>State:</label></div><div id="inner-right-5" align="left"><input type="text" name="state" size="40" value=""></div>

                        <div id="inner-left-6" align="right"><label>Country:</label></div><div id="inner-right-6" align="left"><input type="text" name="country" size="40" value=""></div>

                        <div id="inner-left-7" align="right"><label>Zip:</label></div><div id="inner-right-7" align="left"><input type="text" name="zip" size="40" value=""></div>

                        <div class="inner-right-8" align="center"><input type="submit" value="submit" name="submitBtn"></div>

                    </form>

            </div>





        
    </body>
</html>
