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
    </head>
    <body>

        <div id="maindivision">

<%--This is Header portion--%>

            <div id="header">Header
            </div>

<%--Header ends here--%>
                <div style="clear: both">
                </div>
<%--This is left side or content portion--%>

            <div id="content">BODY
            </div>

<%--Content portion ends here--%>

<%--This is right side or login section--%>
            <div id="login"><jsp:include page="login.jsp"></jsp:include>
            </div>

<%--login or right portion ends here--%>
                <div style="clear: both">
                </div>
 <%--This is Footer portion--%>

            <div id="footer">Footer
            </div>
 <%--Footer portion ends here--%>

        </div>
    </body>
</html>
