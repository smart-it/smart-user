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

    private String organizationName;

    public String getOrganizationFilter() {
        if(organizationName == null)
            organizationName = "";
        return organizationName;
    }

    public void setOrganizationFilter(String organizationName) {
        this.organizationName = organizationName;
    }
}
