<%-- 
    Document   : template
    Created on : Aug 4, 2010, 11:19:15 AM
    Author     : uzzal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%--Uzzal--%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>aponn for you</title>

        <link rel="Stylesheet" href="/css/style.css">
        <link rel="Stylesheet" href="/css/dashboardstyle.css">
        <link rel="Stylesheet" href="/css/organization-style.css">
        <link rel="Stylesheet" href="/css/user-style.css">

        <script type="text/javascript" src="/script/javascript_1.js"></script>
        <script type="text/javascript" src="/script/jquery-1.4.2.js"></script>

        <script type="text/javascript">

            $( init );

            function init() {

                $('#right').append( $('#rightmenu') );
            }

  

            $(document).ready(function(){
                

                $.ajax({
                    type: "GET",
                    url: window.location,
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
                        
                        $("#tablecontentid").html(contentid);
                        $("#tablecontentname").html(contentname);

                        var linkvalue_next="";
                        var linkvalue_prev="";
                        $(xml).find('link').each(function(){

                            var nextlink = $(this).attr("rel");
                            


                            if(nextlink=='next')
                            {

                                var href = $(this).attr("href");
                                
                                linkvalue_next += "<a href=\""+href+"\">"+nextlink+"</a>";
                                $("#tablecontentlink_of_next").html(linkvalue_next);

                            }

                            if(nextlink=='previous')
                            {
                                var href = $(this).attr("href");

                                linkvalue_prev += "<a href=\""+href+"\">"+nextlink+"</a>";
                                $("#tablecontentlink_of_previous").html(linkvalue_prev);

                            }
                        });


                    }

                });



                $("#uniqueShortName2").click(function(){

                    var usn =$("#uniqueShortName").val();

                    $.ajax({
                        type: "GET",
                        url: "http://localhost:9090/orgs/shortname/"+usn,
                        dataType: "xml",
                        success: function(xhr){
                            //                            alert('Short Name is not unique')
                            $("#alertlabel").html('Short Name is not unique');
                        },
                        error: function(xhr){
                            $("#alertlabel").html('Perfect');
                        }

                    });


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

            <div class="clear"></div>

            <%--<div id="right">Right</div>--%>

            <div id="content">
                <%--<jsp:include page="superadminaccess.jsp"></jsp:include>--%>
                <%--<jsp:include page="orgsadminaccess.jsp"></jsp:include>--%>
                <%--<jsp:include page="enduseraccess.jsp"></jsp:include>--%>
                <jsp:include page="${templateContent}"></jsp:include>
            </div>

            <div id="right"></div>

            <div class="clear"></div>

            <div id="footer">Footer</div>

        </div>

    </body>
</html>
<%--Uzzal--%>