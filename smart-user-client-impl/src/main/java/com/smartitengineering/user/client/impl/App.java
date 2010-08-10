package com.smartitengineering.user.client.impl;

import com.smartitengineering.user.client.api.OrganizationsResource;
import com.smartitengineering.user.client.api.RootResource;
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

        RootResource rootResource = RootResourceImpl.getInstance();

        Link loginLink = rootResource.getLoginLink();

        LoginResourceImpl loginResource = new LoginResourceImpl("russel", "russel", loginLink);

        OrganizationsResource orgs = loginResource.getOrganizationsResource();
        

        
        
    }
}
