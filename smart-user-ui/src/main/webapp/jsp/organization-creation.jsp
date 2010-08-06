<%-- 
    Document   : organization-creation
    Created on : Jul 17, 2010, 1:56:52 PM
    Author     : uzzal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


        <div id="maindivisionforganization">


            <div id="header_organization" align="center">
                <label id="headerogorganization">Organization Entry Form</label>
            </div>


            <div id="form_organizationentry" align="center">

                    <form action="http://russel:9090/organizations" method="post" accept="application/json" enctype="" id="organizationform">

                        <div class="inner-left" align="left"><label>Organization Name:</label></div>
                        <div class="inner-right" align="left"><input type="text" name="name" value="" class="textField"></div>

                        <div class="inner-left" align="left"><label>Unique short Name:</label></div>
                        <div class="inner-right" align="left"><input type="text" name="uniqueShortName" value="" class="textField"></div>

                        <div class="inner-left"></div><div class="inner-right" align="left"><label>!!!! already exists!!! try another!!!</label></div>

<%--                        <div style="clear: both">
                        </div>

                        <div id="alertfield"><label>!!!! already exists!!! try another!!!</label></div>

                        <div style="clear: both">
                        </div>--%>

                        <div class="inner-left" align="left"><label>Street Address:</label></div>
                        <div class="inner-right" align="left"><input type="text" name="streetAddress" value="" class="textField"></div>

                        <div class="inner-left" align="left"><label>City:</label></div>
                        <div class="inner-right" align="left"><input type="text" name="city" value="" class="textField"></div>

                        <div class="inner-left" align="left"><label>State:</label></div>
                        <div class="inner-right" align="left"><input type="text" name="state" value="" class="textField"></div>

                        <div class="inner-left" align="left"><label>Country:</label></div>
                        <div class="inner-right" align="left"><input type="text" name="country" value="" class="textField"></div>

                        <div class="inner-left" align="left"><label>Zip:</label></div>
                        <div class="inner-right" align="left"><input type="text" name="zip" value="" class="textField"></div>

                        <div style="clear: both">
                        </div>
                        <div id="btnfield" align="center"><input type="submit" value="submit" name="submitBtn"></div>
                        <div style="clear: both">
                        </div>

                    </form>
                
            </div>
            

          </div>
