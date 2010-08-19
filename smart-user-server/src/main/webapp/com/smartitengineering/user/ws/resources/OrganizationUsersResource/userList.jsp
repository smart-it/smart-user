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
<%--<%@taglib prefix="pg" uri="/WEB-INF/taglib139.tld" %>--%>
<%@page import="com.smartitengineering.user.domain.User"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"

   "http://www.w3.org/TR/html4/loose.dtd">





<c:if test="${param['lang']!=null}">
    <fmt:setLocale scope="session" value="${param['lang']}"/>
</c:if>



<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
        <title>
            <div id="header"><h1>User List</h1></div>
        </title>
        <script type="text/javascript" src="/script/javascript_1.js"></script>
        <script type="text/javascript" src="/script/jquery-1.4.2.js"></script>
        <script type="text/javascript" src="/script/jquery.validate.js"></script>
        <link rel="Stylesheet" href="/css/organization-style.css">
    </head>
    <script type="text/javascript">
        $(document).ready(function(){

                    $.ajax({
                        type: "GET",
                        url: "http://localhost:9090/orgs/SITEL/users",
                        datatype: "xml",

                        success: function(xml){
                            alert(xml)
                            var contenttitle="";
                            var contentid = "";
                            $(xml).find('entry').each(function(){
                                var title = $(this).find('title').text();

                                var id = $(this).find('id').text();
                                var link = $(this).find('link').attr('href');
                                
                                contenttitle += "<div class=\"title\"><a href="+ link +">" + title + "</a>";
                                //alert(contenttitle)
                                
                                contentid += "<div class=\"id\">" + id + "</div>";
                                
                            });
                            $("#teblecontentname").html(contenttitle);
                            $("#teblecontentid").html(contentid);

                            var linkvalue="";
                             $(xml).find('link').each(function(){

                                    var nextlink = $(this).attr("rel");

                                    if(nextlink=='next')
                                    {

                                        var href = $(this).attr("href");
                                        linkvalue += "<a href=" +href+ ">"+nextlink+"\t</a>";
                                        $("#teblecontentlink").html(linkvalue);

                                    }
                                    if(nextlink=='previous')
                                    {
                                        var href = $(this).attr("href");

                                        linkvalue += "<a href="+href+">\t"+nextlink+"</a>";
                                        $("#teblecontentlink").html(linkvalue);
                                    }
                                });
                           
                        }


                    });

                     $("#userform").validate({
                               rules: {
                                   name: "required",
                                   midName: "required",
                                   lastName: "required",
                                   
                                  password: {
                                      required: true,
                                      minlength: 6
                                  },

                                  confirmPassword: {
                                      equalTo: "#password"
                                  },
                                  uniqueShortName: "required"
                                   
                               },
                               messages: {

                                   password: "Password must be atleast of 6 characters"
                               }
                               
                           });

                           
});
    </script>
    

    <body>

        <div id="ff">

        </div>

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


                  
                    <%--<c:forEach var="user" items="${it}">--%>
                        <div id="individual_row">
                            <div>
                                <div id="teblecontentid"></div>
                            </div>

                             <div>
                                 <div id="teblecontentname"></div>
                            </div>

                        </div>
                   <%-- </c:forEach>--%>

                   <div id="teblecontentlink"></div>
                  

            <div class="tablecontent_label">
                <a href="javascript: Orgpageselect()">Create</a>
            </div>

        </div>
                  

        <fmt:message key="org.usrinput6" var="submitbtn"/>

        <div class="hide"  id="create">

            <form action="http://russel:9090/users" method="post" accept="application/json" enctype="" id="userform">

                <div id="inner-left-1" align="right"><label><fmt:message key="org.usrinput1"/></label></div><div id="inner-right-1" align="left"><input type="text" name="name" size="40" value=""></div>

                <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput2"/></label></div><div id="inner-right-2" align="left"><input type="text" name="midName" size="40" value=""></div>

                <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput3"/></label></div><div id="inner-right-2" align="left"><input type="text" name="lastName" size="40" value=""></div>

                <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput4"/></label></div><div id="inner-right-2" align="left"><input id="password" type="password" name="password" size="40" value=""></div>

                <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput7"/></label></div><div id="inner-right-2" align="left"><input id="confirmPassword" type="password" name="confirmPassword" size="40" value=""></div>

                <div id="inner-left-2" align="right"><label><fmt:message key="org.usrinput5"/></label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>


                <div class="inner-right-8" align="center"><input type="submit" value="${submitbtn}" name="submitBtn" ></div>

            </form>

        </div>
        <div id="footer">
        </div>


    </body>
</html>
