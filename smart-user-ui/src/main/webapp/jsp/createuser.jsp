<%-- 
    Document   : createuser
    Created on : Jul 21, 2010, 4:09:25 PM
    Author     : atiqul
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Registration</title>
         <LINK REL=StyleSheet href="../css/organization-style.css" TYPE="text/css" MEDIA=screen>

    </head>
    <body>
        <h1>Register Your Account</h1>

        <div style="background-color: #B6CEE8">
             <form action="http://russel:9090/users" method="post" accept="application/json" enctype="" id="userform">

                        <div id="inner-left-1" align="right"><label>First Name:</label></div><div id="inner-right-1" align="left"><input type="text" name="name" size="40" value=""></div>

                        <div id="inner-left-2" align="right"><label>Middle Name:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>
                        
                        <div id="inner-left-2" align="right"><label>Last Name:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                        <div id="inner-left-2" align="right"><label>Password:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                       <%-- <div id="inner-left-2" align="right"><label>Father Name:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                        <div id="inner-left-2" align="right"><label>Mother Name:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                        <div id="inner-left-2" align="right"><label>National ID:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>--%>

                        <div id="inner-left-2" align="right"><label>Cell Phone Number:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                       <%-- <div id="inner-left-3" align="right"><label>Street Address:</label></div><div id="inner-right-3" align="left"><input type="text" name="streetAddress" size="40" value=""></div>

                        <div id="inner-left-4" align="right"><label>City:</label></div><div id="inner-right-4" align="left"><input type="text" name="city" size="40" value=""></div>

                        <div id="inner-left-5" align="right"><label>State:</label></div><div id="inner-right-5" align="left"><input type="text" name="state" size="40" value=""></div>

                        <div id="inner-left-6" align="right"><label>Country:</label></div><div id="inner-right-6" align="left"><input type="text" name="country" size="40" value=""></div>

                        <div id="inner-left-7" align="right"><label>Zip:</label></div><div id="inner-right-7" align="left"><input type="text" name="zip" size="40" value=""></div>
--%>
<div class="inner-right-8" align="center"><input type="submit" value="submit" name="submitBtn" ></div>

                    </form>
            
      </div>
  
    </body>
</html>
