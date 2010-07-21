<%-- 
    Document   : login
    Created on : Jul 20, 2010, 6:03:53 PM
    Author     : uzzal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<div>sitel secured login!</div>
<div>
    <form action="" method="">

        <label id="loginlbl">Name: </label><input type="text" name="name" id="loginnametextbox" align="left" ><br>
        <label id="loginlbl">Password:</label><input type="password" name="password" id="loginpasstextbox" align="left"><br>

        <input type="checkbox"> <label>Keep me signed in </label>

        <input type="hidden" name="companyName"><br>
        <input type="submit" value="Sign In" name="btnSubmit" id="loginBtn">

</form>
</div>
<div></div>