<%-- 
    Document   : userList
    Created on : Jul 22, 2010, 3:36:48 PM
    Author     : russel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
  <c:when test="${empty param.count}">
    <c:set var="qParam" value="" />
  </c:when>
  <c:otherwise>
    <c:set var="qParam" value="?count=${param.count}" />
  </c:otherwise>
</c:choose>

<script type="text/javascript">
  $(document).ready(function(){
    var url = "/orgs/${orgInitial}/users/frags${qParam}";
    $("#tablecontentid").pagination(url,"linkcontainer");
    
    $(".submit").click(function(){
      alert(1);
      var fname = $("input#fname");
      var mname = $("input#mname");
      var lname = $("input#lname");
      var password = $("input#password");
      var phone = $("input#phone");
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
      var datastring = 'name='+ fname + '&midName='+ mname + '&lastName='+ lname + '&password='+ password + '&phone='+ phone;
      $.ajax({
        type: "POST",
        url: window.location,
        data: datastring,
        success: function(){
          alert("bom");
          $('#form_userentry').html("<div id='message'></div>");
          $('#message').html("<h2>Contact Form Submitted!</h2>")
          .append("<p>We will be in touch soon.</p>")
          .hide()
          .fadeIn(1500, function() {            
            $('#message').append("<img id='checkmark' src='images/check.png' />");
          });
        }
      });

    });
  });
</script>