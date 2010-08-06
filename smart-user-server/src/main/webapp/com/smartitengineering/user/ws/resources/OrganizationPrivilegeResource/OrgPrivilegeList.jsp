<%-- 
    Document   : OrgPrivilegeList
    Created on : Aug 5, 2010, 5:15:18 PM
    Author     : saumitra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Collection"%>
<%@page import="com.smartitengineering.user.domain.Privilege"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Organization Privileges</title>
        <link type="text/css" rel="stylesheet" href="/css/privilegeList.css"/>
        <script type="text/javascript" src="/script/javascript_1.js"></script>
    </head>
    <body>
        <div>
            <div class="show" id="showList" style="width: 80%;position: relative;clear: left">
                <div style="background-color: #77e445">
                    <h1 id="header" >Privileges</h1>
                </div>

                <div style="text-decoration: underline;border-color: #13200d">


                    <div class="tableheadname">
                        <h4>Display Name</h4>
                    </div>

                    <div class="tableheadPM">
                        <h4> permission Mask</h4>
                    </div>
                    <div class="tableheadSOI">
                        <h4> Secured Object ID</h4>
                    </div>
                    <div class="tableheaddetails">
                        <h4> Details</h4>
                    </div>
                </div>




                <div>
                    <c:forEach var="privilege" items="${it}">
                        <div class="teblecontentname">
                            <h4><c:out value="${privilege.displayName}" /></h4>
                        </div>

                        <div class="teblecontentPM">
                            <script type="text/javascript">
                                PM=${privilege.permissionMask};
                                PM=PM+' ';
                            </script>
                            <h4><c:out value="${privilege.permissionMask}" /></h4>

                        </div>

                        <div class="teblecontentSOI">

                            <h4><c:out value="${privilege.securedObject}"/></h4>

                        </div>
                        <div class="teblecontentdetails">
                            <a href="orgs/privs/${privilege.name}"><h4>Details</h4></a>
                        </div>



                    </div>

                </c:forEach>
                <div>
                    <a href="javascript: Orgpageselect()"><h4>Create</h4></a>
                </div>

            </div>
            <div id="create" align="center" class="show">

                <form action="http://russel:9090/orgs/privs" method="post" accept="application/json" enctype="" id="privilegeform">

                    <div  align="left"><label>Name:</label></div><div  align="left"><input type="text" name="name" size="40"></div>

                    <div  align="left"><label>Display Name:</label></div><div align="left"><input type="text" name="displayName" size="40" value=""></div>

                    <div  align="left"><label>Short Description:</label></div><div  align="left"><input type="text" name="shortdes" size="40" value=""></div>

                    <div  align="left"><label>Secure Object:</label></div><div  align="left"><input type="text" name="secureObject" size="40" value=""></div>

                    <div  align="left"><label>Permission Mask:</label></div><div  align="left"><input type="text" name="permissionMask" size="40" value=""></div>

                    <div  align="left"><label>Parent Organization Id:</label></div><div align="left"><input type="text" name="parentOrganizationId" size="40" value=""></div>

                    <div  align="left"><label>Secure Object Id:</label></div><div align="left"><input type="text" name="secureObjectId" size="40" value=""></div>

                    <div  align="left"><label>Last Modified Date:</label></div><div  align="left"><input type="text" name="lastModified" size="40" value=""></div>

                    <div  align="left"><input type="submit" value="submit" name="submitBtn"></div>
                </form>

            </div>
        </div>

    </body>
</html>
