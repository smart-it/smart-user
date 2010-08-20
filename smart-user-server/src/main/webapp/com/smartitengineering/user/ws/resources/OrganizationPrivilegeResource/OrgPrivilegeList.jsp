<%-- 
    Document   : OrgPrivilegeList
    Created on : Aug 5, 2010, 5:15:18 PM
    Author     : saumitra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Collection"%>
<%@page import="com.smartitengineering.user.domain.Privilege"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

            <div class="show" id="showList">

                <div id="title_of_organization">
                    <h1 id="header" >Privileges</h1>
                </div>

                <div id="top_row">

                    <div class="table_privs_headers">
                      <label>Display Name</label>
                    </div>

                    <div class="table_privs_headers">
                      <label>Permission Mask</label>
                    </div>

                    <div class="table_privs_headers">
                      <label>Secured Object ID</label>
                    </div>

                    <div class="table_privs_headers">
                      <label>Details</label>
                    </div>

                </div>



              <c:forEach var="privilege" items="${it}">

                <div id="individual_row">
                        <div class="table_privs_contents">
                          <label><c:out value="${privilege.displayName}" /></label>
                        </div>

                        <div class="table_privs_contents">
                            <script type="text/javascript">
                                PM=${privilege.permissionMask};
                                PM=PM+' ';
                            </script>
                                <label><c:out value="${privilege.permissionMask}" /></label>

                        </div>

                        <div class="table_privs_contents">

                          <label><c:out value="${privilege.securedObject}"/></label>

                        </div>
                        <div class="table_privs_contents">
                          <label><a href="orgs/privs/${privilege.name}">Details</a></label>
                        </div>

                    </div>

                </c:forEach>

                <div>
                    <a href="javascript: Orgpageselect()">Create</a>
                </div>

            </div>

          
            <div id="create" class="hide">

                <form action="http://russel:9090/orgs/privs" method="post" accept="application/json" enctype="" id="privilegeform">

                    <div class="inner-left"><label>Name:</label></div>
                    <div class="inner-right"><input type="text" name="name"></div>

                    <div class="inner-left"><label>Display Name:</label></div>
                    <div class="inner-right"><input type="text" name="displayName"></div>

                    <div class="inner-left"><label>Short Description:</label></div>
                    <div class="inner-right"><input type="text" name="shortdes"></div>

                    <div class="inner-left"><label>Secure Object:</label></div>
                    <div class="inner-right"><input type="text" name="secureObject"></div>

                    <div class="inner-left"><label>Permission Mask:</label></div>
                    <div class="inner-right"><input type="text" name="permissionMask"></div>

                    <div class="inner-left"><label>Parent Organization Id:</label></div>
                    <div class="inner-right"><input type="text" name="parentOrganizationId"></div>

                    <div class="inner-left"><label>Secure Object Id:</label></div>
                    <div class="inner-right"><input type="text" name="secureObjectId"></div>

                    <div class="inner-left"><label>Last Modified Date:</label></div>
                    <div class="inner-right"><input type="text" name="lastModified"></div>

                    <div><input type="submit" value="submit" name="submitBtn"></div>
                </form>

            </div>