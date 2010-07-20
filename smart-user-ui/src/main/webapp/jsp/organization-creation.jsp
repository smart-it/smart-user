<%-- 
    Document   : organization-creation
    Created on : Jul 17, 2010, 1:56:52 PM
    Author     : uzzal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>create organization</title>

<%--           <script type="text/javascript" src="jquery-1.4.2.js"></script>
           <script type="text/javascript" src="json2.js"></script>
           <script type="text/javascript" src="jquery.form.js"></script>

             <script type="text/javascript">
        // wait for the DOM to be loaded
            $(document).ready(function() {
            // bind 'myForm' and provide a simple callback function
            $('#organizationform').ajaxForm(function() {
                alert("Thank you for your comment!");
            });
            });
            </script>--%>

           <LINK REL=StyleSheet href="../css/organization-style.css" TYPE="text/css" MEDIA=screen>

    </head>

    <body>
        <center>

        <div id="main">


            <div id="header_organization">
                <h2>Organization Entry</h2>
            </div>

            <div id="form_organizationentry" align="center">

                    <form action="http://russel:9090/organizations" method="post" accept="application/json" enctype="" id="organizationform">

                        <div class="inner-left" align="right"><label>Organization Name:</label></div><div class="inner-right" align="left"><input type="text" name="name" size="40" value=""></div>

                        <div class="inner-left" align="right"><label>Unique short Name:</label></div><div class="inner-right" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                        <div class="inner-left" align="right"><label>Address:</label></div><div class="inner-right" align="left"><textarea name="address" cols="46" rows="5"></textarea></div>

                        <div class="inner-right" align="left"><input type="submit" value="submit" name="submitBtn"></div>

                    </form>
                
            </div>
            

          </div>
            </center>


    </body>
</html>
