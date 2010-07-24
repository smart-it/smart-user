<%-- 
    Document   : organization-creation
    Created on : Jul 17, 2010, 1:56:52 PM
    Author     : uzzal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


        <div id="maindivisionoforganization">


            <div id="header_organization" align="center">
                <label id="headerogorganization">Organization Entry</label>
            </div>


            <div id="form_organizationentry" align="center">

                    <form action="http://russel:9090/organizations" method="post" accept="application/json" enctype="" id="organizationform">

                        <div id="inner-left-1" align="right"><label>Organization Name:</label></div><div id="inner-right-1" align="left"><input type="text" name="name" size="40" value=""></div>

                        <div id="inner-left-2" align="right"><label>Unique short Name:</label></div><div id="inner-right-2" align="left"><input type="text" name="uniqueShortName" size="40" value=""></div>

                        <div id="inner-left-3" align="right"><label>Street Address:</label></div><div id="inner-right-3" align="left"><input type="text" name="streetAddress" size="40" value=""></div>

                        <div id="inner-left-4" align="right"><label>City:</label></div><div id="inner-right-4" align="left"><input type="text" name="city" size="40" value=""></div>

                        <div id="inner-left-5" align="right"><label>State:</label></div><div id="inner-right-5" align="left"><input type="text" name="state" size="40" value=""></div>

                        <div id="inner-left-6" align="right"><label>Country:</label></div><div id="inner-right-6" align="left"><input type="text" name="country" size="40" value=""></div>

                        <div id="inner-left-7" align="right"><label>Zip:</label></div><div id="inner-right-7" align="left"><input type="text" name="zip" size="40" value=""></div>

                        <div class="inner-right-8" align="center"><input type="submit" value="submit" name="submitBtn"></div>

                    </form>
                
            </div>
            

          </div>
