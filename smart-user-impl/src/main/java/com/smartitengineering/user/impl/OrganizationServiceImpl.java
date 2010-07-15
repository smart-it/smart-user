/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.impl;

import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.filter.OrganizationFilter;
import com.smartitengineering.user.service.OrganizationService;
import java.util.Collection;

/**
 *
 * @author modhu7
 */
public class OrganizationServiceImpl implements OrganizationService{

    @Override
    public void save(Organization organization) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Organization organization) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Organization organization) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Organization> search(OrganizationFilter organizationFilter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Organization> getAllOrganization() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Organization getOrganizationByOrganizationName(String organizationName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void validateOrganization(Organization organization) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
