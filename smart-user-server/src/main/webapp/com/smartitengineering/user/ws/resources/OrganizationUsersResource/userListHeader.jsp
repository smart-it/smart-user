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
