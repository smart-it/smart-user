<%-- 
    Document   : login
    Created on : Jul 20, 2010, 6:03:53 PM
    Author     : uzzal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<div id="inloginform">
    <div id="loginheader"><label>sitel secured login!</label></div>
        <form action="" method="">

            <div id="loginlabeldiv" align="right"><label id="loginlbl">Name: </label></div>
            <div id="loginboxdiv"><input type="text" name="name" id="loginnametextbox" align="left"></div>
            <div id="loginlabeldiv" align="right"><label id="loginlbl">Password:</label></div>
            <div id="loginboxdiv"><input type="password" name="password" id="loginpasstextbox" align="left"></div>
            <div style="clear: both"></div>

            <input type="hidden" name="companyName">

            <div class="btnlinkcheck"><input type="checkbox"> Keep me signed in</div>
            <div class="btnlinkcheck"><input type="submit" value="Sign In" name="btnSubmit" id="loginBtn"></div>
            <%--<div class="btnlinkcheck"><button type="submit"><img src="button-login.png" alt="Sign In"></button></div>--%>

            <div class="btnlinkcheck"><a href="#">Can't access your account?</a></div>

        </form>
</div>