<%-- 
    Document   : template
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

    <link rel="Stylesheet" href="/css/style.css">
    <link rel="Stylesheet" href="/css/dashboardstyle.css">
    <link rel="Stylesheet" href="/css/organization-style.css">
    <link rel="Stylesheet" href="/css/user-style.css">

    <script type="text/javascript" src="/script/javascript_1.js"></script>
    <script type="text/javascript" src="/script/jquery-1.4.2.js"></script>
    <script type="text/javascript">
          
      $(document).ready(function(){
        $.ajax({
          type: "GET",
          url: "http://localhost:9090/orgs",
          dataType: "xml",
          success: function(xml) {

            var contentId="";
            var contentTitle="";

            $(xml).find('entry').each(function(){
              var id = $(this).find('id').text();
      <%--alert(id)--%>
                var title = $(this).find('title').text();
      <%--alert(title)--%>
                contentId +=  id ;
                contentTitle += title;

<%--      		$('<div class="items" id="link_'+id+'"></div>').html('<a href="'+url+'">'+title+'</a>').appendTo('#page-wrap');
    $(this).find('entry').each(function(){
      var brief = $(this).find('brief').text();
      var l = $(this).find('long').text();
      $('<div class="brief"></div>').html(brief).appendTo('#link_'+id);
      $('<div class="long"></div>').html(l).appendTo('#link_'+id);
    });--%>
            });

            $("#tableContentId").html(contentId);
            $("#tableContentTitle").html(contentTitle);
               
            var linkvalue="";

            $(xml).find('link').each(function(){
              var nextlink = $(this).attr("rel");
              if(nextlink=='next')
              {
                var href = $(this).attr("href");
                linkvalue += "<a href=\""+href+"\">"+href+"</a>";
                $("#teblecontentlink").html(linkvalue);
              }
            })

          }

        });
      });

    </script>

    <script type="text/javascript">

      $( init );

      function init() {

        $('#right').append( $('#rightmenu') );
      }

    </script>


  </head>
  <body>
    <div id="main" >
      <div id="header"><label>aponn.com</label></div>

      <div id="options">


        <%--<form action="#" method="post">
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

      <div style="clear: both"></div>

      <%--<div id="right">Right</div>--%>

      <div id="content">
        <%--<jsp:include page="superadminaccess.jsp"></jsp:include>--%>
        <%--<jsp:include page="orgsadminaccess.jsp"></jsp:include>--%>
        <%--<jsp:include page="enduseraccess.jsp"></jsp:include>--%>
        <jsp:include page="${templateContent}"></jsp:include>
      </div>

      <div id="right"></div>

      <div style="clear: both">
      </div>

      <div id="footer">Footer</div>

    </div>

  </body>
</html>