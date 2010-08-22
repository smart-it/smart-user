<%-- 
    Document   : template
    Created on : Aug 4, 2010, 11:19:15 AM
    Author     : uzzal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"

  <%--Uzzal--%>

  <html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

  <title>aponn for you</title>

  <link rel="Stylesheet" href="/css/style.css">
  <link rel="Stylesheet" href="/css/dashboardstyle.css">
  <link rel="Stylesheet" href="/css/organization-style.css">
  <link rel="Stylesheet" href="/css/user-style.css">
  <link rel="Stylesheet" href="/css/smart-menu.css">

  <script type="text/javascript" src="/script/javascript_1.js"></script>
  <script type="text/javascript" src="/script/jquery-1.4.2.js"></script>
  <script type="text/javascript" src="/script/jquery.validate.js"></script>
  <script type="text/javascript" src="/script/siteljquerylib.js"></script>

  <script type="text/javascript">

    $( init );

    function init() {

      $('#right').append( $('#leftmenu') );
    }

  </script>

  <script type="text/javascript">

    $(document).ready(function () {
      $("#leftmenu_body ul li").hover(function () {
        $(this).css({'background-color' : '#F7F5FE','color' : 'Black'});
      }, function () {
        var cssObj = {
          'background-color' : '',
          'color' : '#23819C'
        }
        $(this).css(cssObj);
      });

    });

    $(document).ready(function () {
      $("#leftmenu_body ul li a").hover(function () {
        $(this).css({'color' : 'Black', 'font-weight' : 'bolder', 'font-size':'11pt'});
      }, function () {
        var cssObj = {
          'background-color' : '',
          'font-weight' : '',
          'font-size':'10pt',
          'color' : '#23819C'
        }
        $(this).css(cssObj);
      });

    });

  </script>


  <!--saumitra-->

  <script type="text/javascript">
     

    $(document).ready(function(){
       



    url=window.location;
    $("#tablecontentid").pagination(url,'#tablecontentid');
    $("#uniqueShortName").blur(function(){

      var usn =$("#uniqueShortName").val();

      $.ajax({
        type: "GET",
        url: "http://localhost:9090/orgs/shortname/"+usn,
        dataType: "xml",
        success: function(xhr){
          //                            alert('Short Name is not unique')
          $("#alertlabel").html('Short Name is not unique: try another');
        },
        error: function(xhr){
          $("#alertlabel").html('Perfect: Carry On');
                        
        }

      });
    });
    //            $("#submit").click(function(){
    //
    //                alert(window.location);
    //                var usn =$("#uniqueShortName").val()
    //                window.location.replace = "http://localhost:9090/orgs/shortname/"+usn;
    //
    //            });




    $("#organizationform").validate({
      rules: {
        name: "required",
        streetAddress: "required",
        city: "required",
        state: "required",
        country:"required",
        zip:"required",         
        uniqueShortName: "required"

      }
                        


    });

  });


  </script>
  <!--saumitra-->

  <!-- ROCKY -->
<!--  <script type="text/javascript">
  $(document).ready(function(){

    $.ajax({



        <!-- ROCKY -->
         <script type="text/javascript">
           $(document).ready(function(){

            url: window.location;
            $("#userlist").pagination(url,"#userlist");



             $("#userform").validate({
               rules: {
                 name: "required",
                 midName: "required",
                 lastName: "required",
                 userName: "required",
                 confirmPassword: {
                   equalTo: "#password"
                 },
                 uniqueShortName: "required"

               },
               messages: {

                 password: "Password must be atleast of 6 characters"
               }

             });


             $(".submit").click(function(){

               var fname = $("input#fname");
               var mname = $("input#mname");
               var lname = $("input#lname");
               var password = $("input#password");
               var phone = $("input#phone")
               var datastring = 'name='+ fname + '&midName='+ mname + '&lastName='+ lname + '&password='+ password + '&phone='+ phone;


             });






           });
  </script>

  <script type="text/javascript">
  $(document).ready(function(){
    $(".submit").click(function(){
      var id = $("input#id");
      var name = $("input#name");
      var password = $("input#password");
      var version = $("input#version");

      var datastring = 'id='+ id +'version='+ version + 'name='+ name + '&password='+ password;


    });

    $.ajax({
      type: "POST",
      url: window.location,
      data: datastring,
      success: function(){
    <%--alert ("oooo")
    $('#form_organizationentry').html("<div id='message'></div>");
    $('#message').html("<h2>Form submitted successfully</h2")
    alert("kkk")
    .hide()
    .fade(1500, function(){
        $('#message');
    });--%>
          }
        });
        return false;
      })
  </script>-->
  <!-- ROCKY -->
</head>

<body>
  <div id="main" >
    <div id="header"><div id="sitel_logo"><img src="/images/site ultimate build 1.0.0.5.jpg" alt="sitel" id="img-sitel-logo"></div><div id="sitel_slogan"><label>IT for smarter living</label></div></div>

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