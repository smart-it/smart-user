<%-- 
    Document   : OrganizationList
    Created on : Jul 17, 2010, 3:54:38 PM
    Author     : saumitra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<head>
<LINK REL=StyleSheet href="../css/organizationlist.css" TYPE="text/css" MEDIA=screen>
</head>


<body>

<!--request for organization list-->

<c:import url="russel:9090/organizations" var="organizationList" scope="session"/>


<!--Table header-->
    <div style="width: 80%;position: relative;clear: left">
    <div style="background-color: #77e445">
            <h1 id="header" >Organization</h1>
    </div>
            
    <div style="text-decoration: underline;border-color: #13200d">

    <div id="tableheadid">
        <h4> ID</h4>
    </div>

    <div id="tableheadname">     
        <h4>Organization name</h4>
    </div>

    <div class="tableheadlink">   
        <h4>Edit</h4>
    </div>

   <div class="tableheadlink">
        <h4> Delete</h4>
   </div>

   </div>



<!--Table details of Organization with edit and delete option-->
  
    <c:forEach var="i"  begin="1" end="listsize" step="1" varStatus="status">
          <div>

          <div id="teblecontentid">
              <h4>  <c:out value="$organization" /></h4>
          </div>

          <div id="teblecontentname">
              <h4><c:out value="${organization.name}" /></h4>
          </div>

          <div class="teblecontentlink">
              <h4><a href="organization-edit.jsp">Edit</a></h4>
          </div>

          <div class="teblecontentlink">
                <h4><a href="Jstl_Core_Tags.jsp?valid=true&name=eric&mark=8">Delete</a></h4>
          </div>

          </div>
     </c:forEach>
    </div>
</body>
<c:forEach var="organization" items="${it}">
            <tr>
                <td>
                    <c:out value="${organization.name}" />

                </td>
                <td>
                    <c:out value="${organization.uniqueShortName}" />
                </td>
                <td>
                    <c:out value="${organization.address.streetAddress}" />
                </td>
                <td>
                    <c:out value="${organization.address.city}" />
                </td>
                <td>
                    <c:out value="${organization.address.state}" />
                </td>
                <td>
                    <c:out value="${organization.address.country}" />
                </td>
            </tr>
        </c:forEach>

