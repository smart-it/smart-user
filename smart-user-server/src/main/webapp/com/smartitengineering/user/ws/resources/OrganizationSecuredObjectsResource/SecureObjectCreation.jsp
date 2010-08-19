<%-- 
    Document   : SecureObjectCreation
    Created on : Aug 5, 2010, 5:46:00 PM
    Author     : saumitra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div id="form_secureobject" align="center">

            <form action="http://russel:9090/orgs/privs" method="post" accept="application/json" enctype="" id="privilegeform">

                <div id="inner-left-1" align="right"><label>Name:</label></div><div id="inner-right-1" align="left"><input type="text" name="name" size="40"></div>

                <div id="inner-left-2" align="right"><label>Object ID:</label></div><div id="inner-right-2" align="left"><input type="text" name="objectId" size="40" value=""></div>

                <div id="inner-left-3" align="right"><label>Parent Object:</label></div><div id="inner-right-3" align="left"><input type="text" name="parentObject" size="40" value=""></div>

                <div id="inner-left-4" align="right"><label>Organization:</label></div><div id="inner-right-4" align="left"><input type="text" name="organization" size="40" value=""></div>

                <div id="inner-left-6" align="right"><label>Parent Organization Id:</label></div><div id="inner-right-6" align="left"><input type="text" name="organizationId" size="40" value=""></div>

                <div id="inner-left-7" align="right"><label>Last Modified Date:</label></div><div id="inner-right-7" align="left"><input type="text" name="lastModifiedDate" size="40" value=""></div>

                <div class="inner-right-8" align="center"><input type="submit" value="submit" name="submitBtn"></div>

            </form>

        </div>
    </body>
</html>
