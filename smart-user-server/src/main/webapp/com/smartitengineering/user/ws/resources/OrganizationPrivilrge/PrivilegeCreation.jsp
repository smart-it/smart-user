<%-- 
    Document   : PrivilegeCreation
    Created on : Aug 5, 2010, 5:22:27 PM
    Author     : saumitra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>privilege Creation</title>
    </head>
    <body>

        <div id="form_privilege" align="center">

            <form action="http://russel:9090/orgs/privs" method="post" accept="application/json" enctype="" id="privilegeform">

                <div id="inner-left-1" align="right"><label>Name:</label></div><div id="inner-right-1" align="left"><input type="text" name="name" size="40"></div>

                <div id="inner-left-2" align="right"><label>Display Name:</label></div><div id="inner-right-2" align="left"><input type="text" name="displayName" size="40" value=""></div>

                <div id="inner-left-3" align="right"><label>Short Description:</label></div><div id="inner-right-3" align="left"><input type="text" name="shortdes" size="40" value=""></div>

                <div id="inner-left-4" align="right"><label>Secure Object:</label></div><div id="inner-right-4" align="left"><input type="text" name="secureObject" size="40" value=""></div>

                <div id="inner-left-5" align="right"><label>Permission Mask:</label></div><div id="inner-right-5" align="left"><input type="text" name="permissionMask" size="40" value=""></div>

                <div id="inner-left-6" align="right"><label>Parent Organization Id:</label></div><div id="inner-right-6" align="left"><input type="text" name="parentOrganizationId" size="40" value=""></div>

                <div id="inner-left-7" align="right"><label>Secure Object Id:</label></div><div id="inner-right-7" align="left"><input type="text" name="secureObjectId" size="40" value=""></div>

                <div id="inner-left-7" align="right"><label>Last Modified Date:</label></div><div id="inner-right-7" align="left"><input type="text" name="lastModified" size="40" value=""></div>

                <div class="inner-right-8" align="center"><input type="submit" value="submit" name="submitBtn"></div>

            </form>

        </div>
    </body>
</html>
