/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.filter;

/**
 *
 * @author russel
 */
public class OrganizationFilter {

    private String organizationUniqueShortName;

    public String getOrganizationUniqueShortName() {
        if(organizationUniqueShortName == null)
            organizationUniqueShortName = "";
        return organizationUniqueShortName;
    }

    public void setOrganizationUniqueShortName(String organizationUniqueShortName) {
        this.organizationUniqueShortName = organizationUniqueShortName;
    }
}
