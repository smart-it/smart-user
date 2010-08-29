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
<script type="text/javascript" src="/script/user-validation.js"></script>
<script type="text/javascript">
  $(document).ready(function(){
    var url = "/orgs/${orgInitial}/users/frags${qParam}";
    $("#tablecontentid").pagination(url,"linkcontainer");
    $("#wrong").hide();
    $("#uname").blur(function(){
    var usn =$("#uname").val();
    $.ajax({
      type: "GET",
      url: "http://localhost:9090/orgs/${orgInitial}/users/username/"+usn,
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
