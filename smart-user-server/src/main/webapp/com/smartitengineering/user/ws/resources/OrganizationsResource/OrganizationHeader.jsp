<%-- 
    Document   : OrganizationHeader
    Created on : Aug 23, 2010, 1:43:28 PM
    Author     : saumitra
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

    var url = "http://localhost:9090/orgs/frags${qParam}";
    $("#tablecontentid").pagination(url, "paginationLinks");
    $("#wrong").hide();

  $("#uniqueShortName").blur(function(){    
      var usn =$("#uniqueShortName").val();
      $.ajax({
        type: "GET",
        url: "http://localhost:9090/orgs/sn/"+usn,
        dataType: "xml",
        success: function(xhr){
          $("#wrong").show();
          $("#alertlabel").html('Short Name is not unique: try another');
        },
        error: function(xhr){
          $("#wrong").hide();
          $("#alertlabel").html('');
        }
      });
    });

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