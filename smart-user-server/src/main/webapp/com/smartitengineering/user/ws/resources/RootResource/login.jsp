<%-- 
    Document   : login
    Created on : Jul 20, 2010, 6:03:53 PM
    Author     : uzzal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">

<div id="inloginform" class="loginSection">
  <div id="loginheader" class="loginHeader"><label>sitel secured login!</label></div>
  <div class="clear"></div>
  <form action="" method="post">

    <div class="loginEntrySection">
      <div id="loginlabeldiv" class="loginLabelDiv"><label>Username:</label></div>
      <div id="loginboxdiv" class="loginTextBoxDiv"><input type="text" name="name" id="loginnametextbox" class="loginTextBox"></div>
      <div class="clear"></div>
      <div id="loginlabeldiv" class="loginLabelDiv"><label>Password:</label></div>
      <div id="loginboxdiv" class="loginTextBoxDiv"><input type="password" name="password" id="loginpasstextbox" class="loginTextBox"></div>
      <div class="clear"></div>

      <input type="hidden" name="companyName">
    </div>

    <div class="btnLinkCheckDiv">
      <div class="btnlinkcheck"><input type="checkbox"> Keep me signed in</div>
      <div class="btnDiv"><input type="submit" value="Sign In" name="btnSubmit" id="loginBtn"></div>
      <%--<div class="btnlinkcheck"><input type="image" src="more_btn.jpg" name="btnSubmit" id="loginBtn" alt="SUBMIT"></div>--%>

      <div class="btnlinkcheck"><a href="#">Can't access your account?</a></div>
    </div>

  </form>

</div>