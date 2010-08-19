<%-- 
    Document   : dashboard
    Created on : Aug 4, 2010, 11:19:15 AM
    Author     : uzzal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>aponn for you</title>
        <link rel="Stylesheet" href="../css/style.css">
        <link rel="Stylesheet" href="../css/dashboardstyle.css">
        <link rel="Stylesheet" href="../css/organization-style.css">
        <%--<link rel="Stylesheet" href="../css/organizationlist.css">--%>

        <script type="text/javascript" src="/script/javascript_1.js"></script>
        <script type="text/javascript" src="/script/jquery-1.4.2.js"></script>
        <script>
            $(document).ready(function(){
                
                $.ajax({
                    type: "GET",
                    url: "http://localhost:9090/orgs",
                    dataType: "xml",
                    success: function(xml) {
                    
                        var contentid="";
                        var contentname="";
                        $(xml).find('entry').each(function(){
                            var id = $(this).find('id').text();
                                
                            var title = $(this).find('title').text();
                            var link = $(this).find('link').attr('href');
                            // alert(link);
                            contentid += "<div class=\"id\"><a href="  + link +">"+id+"</a></div>";
                            contentname += "<div class=\"title\"><a href=" + link + ">" +title+"</a></div>";

                        });
                        $("#teblecontentid").html(contentid);
                        $("#teblecontentname").html(contentname);
                        

                            var linkvalue="";
                            $(xml).find('link').each(function(){

                                var nextlink = $(this).attr("rel");


                                if(nextlink=='next')
                                {

                                    var href = $(this).attr("href");
                                    linkvalue += "<a href=\""+href+"\">"+nextlink+"</a>";
                                    $("#teblecontentlink").html(linkvalue);

                                }

                                if(nextlink=='previous')
                                {
                                    var href = $(this).attr("href");

                                    linkvalue += "<a href=\""+href+"\">"+nextlink+"</a>";
                                    $("#teblecontentlink").html(linkvalue);

                                }
                            });


                    }
               
                });



                $("#organizationform").validate({
                    rules: {
                        name: "required",// simple rule, converted to {required:true}
                        uniqueShortName: "required",
                        country: "required"
                    },
                    messages: {
                        comment: "Please enter a comment."
                    }
                });

            });


        </script>
    </head>

    <body>
        <div id="main" >
            <div id="header"><label>aponn.com</label></div>

            <div id="options">
                <%--            <form action="#" method="post">
                                <table>
                                    <tr>
                                        <td>
                                             <input type="text" id="searchbox" name="search" size="75">
                                        </td>

                        <td>
                             <input type="submit" id="btnSearch" value="search">
                             <input type="image" src="../images/butup.gif" id="btnSearch" value="Search" alt="[Submit]" name="submit">
                        </td>
                    </tr>
                </table>
                </form>--%>

            </div>

            <div style="clear: both">
            </div>

            <div id="content">
                <%--<jsp:include page="superadminaccess.jsp"></jsp:include>--%>
                <%--<jsp:include page="orgsadminaccess.jsp"></jsp:include>--%>
                <%--<jsp:include page="enduseraccess.jsp"></jsp:include>--%>
                <jsp:include page="${templateContent}"></jsp:include>
                <%--<jsp:include page="OrganizationDetails.jsp"></jsp:include>--%>

            </div>

            <div id="right">Right</div>

            <div style="clear: both">
            </div>

            <div id="footer">Footer</div>

        </div>

    </body>
</html>