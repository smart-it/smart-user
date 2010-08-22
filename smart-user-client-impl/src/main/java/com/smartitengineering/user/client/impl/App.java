package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.LoginResource;
import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.OrganizationsResource;
import com.smartitengineering.user.client.api.RootResource;
import com.smartitengineering.user.client.impl.login.LoginCenter;
import com.smartitengineering.user.domain.Organization;
import java.util.List;
import org.apache.abdera.model.Link;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        System.out.println();

        LoginCenter.setUsername("smartadmin@smart-user");
        LoginCenter.setPassword("02040250204039");


        RootResource rootResource = RootResourceImpl.getInstance();

        

        LoginResource loginResource = rootResource.performAuthentication("smartadmin@smart-user", "russel");

        loginResource.getAclAuthorizationResource("smartadmin", "smart-user", "/orgs", 4);

        OrganizationsResource organizationsResource = loginResource.getOrganizationsResource();
        
        com.smartitengineering.user.client.impl.domain.Organization organization = new com.smartitengineering.user.client.impl.domain.Organization();

        organization.setName("Smart IT Engineering Limited");
        organization.setUniqueShortName("SITEL");

        //organizationsResource.create(organization);
        List<OrganizationResource> orgsResources = organizationsResource.getOrganizationResources();

        OrganizationResource orgResource = orgsResources.get(1);
        orgResource.update();

        //organizationsResource.getOrganizationResources();

        

        OrganizationsResource organizationResources = loginResource.getOrganizationsResource();

        

        
        
    }
}
