<%-- 
    Document   : userList
    Created on : Jul 22, 2010, 3:36:48 PM
    Author     : russel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link id="usrUrl"href="<c:url value="/orgs/sn/${orgInitial}/users/frags${qParam}"/>">
<link id="usrNameCheck" href="<c:url value="/orgs/sn/${orgInitial}/users/un/"/>">

<c:choose>
  <c:when test="${empty param.count}">
    <c:set var="qParam" value="" />
  </c:when>
  <c:otherwise>
    <c:set var="qParam" value="?count=${param.count}" />
  </c:otherwise>
</c:choose>
<script type="text/javascript" src="<c:url value="/script/user-validation.js"/>"></script>
<script type="text/javascript">
  $(document).ready(function(){
    var usrUri = $("#usrUrl").attr('href');
    $("#tablecontentid").pagination(usrUri,"linkcontainer");
    $("#wrong").hide();
    $("#uname").blur(function(){
      var usn =$("#uname").val();
      var ajaxUrl =$("#usrNameCheck").attr('href')+usn ;      
      $.ajax({
        type: "GET",
        url: ajaxUrl,
        dataType: "xml",
        success: function(xhr){          
          $("#wrong").show();
          $("#alertlabel").html('User Name is not unique: try another');
        },
        error: function(xhr){          
          $("#wrong").hide();
          $("#alertlabel").html('');
        }
      });
    });
  });
</script>
