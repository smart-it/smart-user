<%-- 
    Document   : rootpage
    Created on : Jul 20, 2010, 5:43:58 PM
    Author     : uzzal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>home page</title>
        <link rel="Stylesheet" href="../css/default.css">
        <link rel="Stylesheet" href="../css/login.css">
    </head>
    <body>

        <div id="maindivision">

<%--This is Header portion--%>

            <div id="header"><h1>aponn.com</h1>
            </div>

<%--Header ends here--%>
            <div style="clear: both">
            </div>
<%--This is left side or content portion--%>

            <div id="content">
            </div>

<%--Content portion ends here--%>

<%--This is right side or login section--%>
        <div id="right" align="center">
            <div id="login">
                <jsp:include page="login.jsp"></jsp:include>
            </div>

            <div id="signup">
                <jsp:include page="signup.jsp"></jsp:include>
            </div>
        </div>


<%--login or right portion ends here--%>
            <div style="clear: both">
            </div>
 <%--This is Footer portion--%>

 <div id="footer"><h3>copyright@smartitengineering</h3>
            </div>
 <%--Footer portion ends here--%>

        </div>
    </body>
</html>