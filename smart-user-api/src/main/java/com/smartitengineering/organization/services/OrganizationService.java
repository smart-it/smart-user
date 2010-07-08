/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.organization.services;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.filter.OrganizationFilter;
import java.util.Collection;

/**
 *
 * @author russel
 */
public interface OrganizationService {

    public void update(Organization organization);
    public void delete(Organization organization);
    public Collection<Organization> search(OrganizationFilter organizationFilter);
    public Collection<Organization> getAllOrganization();
    public Organization getOrganizationByOrganizationName(String organizationName);
    public void validateOrganization(Organization organization);

}
