package com.smartitengineering.clientrest.smartuserclientimpl;

import com.smartitengineering.user.client.api.LoginResource;
import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.OrganizationsResource;
import com.smartitengineering.user.client.api.RootResource;
import com.smartitengineering.user.client.impl.RootResourceImpl;
import com.smartitengineering.user.client.impl.domain.Address;
import com.smartitengineering.user.client.impl.domain.Organization;
import com.smartitengineering.user.client.impl.login.LoginCenter;
import com.smartitengineering.util.rest.client.ApplicationWideClientFactoryImpl;
import java.io.File;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ComprehensiveClientTest {

  private static Server jettyServer;
  private static final int PORT = 9090;

  @BeforeClass
  public static void setup()
      throws Exception {
    setupServer();
    setupClient();
  }

  protected static void setupServer() throws IllegalStateException, Exception {
    System.out.println("::: Starting server :::");
    jettyServer = new Server(PORT);
    final String webapp = "./target/smartuser/";
    if (!new File(webapp).exists()) {
      throw new IllegalStateException("WebApp file/dir does not exist!");
    }
    Handler webAppHandler = new WebAppContext(webapp, "/");
    jettyServer.setHandler(webAppHandler);
    jettyServer.start();
  }

  protected static void setupClient() throws BeansException {
    ClassPathXmlApplicationContext classPathXmlApplicationContext =
                                   new ClassPathXmlApplicationContext("config-context.xml");
    System.setProperty(ApplicationWideClientFactoryImpl.TRACE, Boolean.TRUE.toString());
    LoginCenter.setUsername("smartadmin@smart-user");
    LoginCenter.setPassword("02040250204039");
  }

  @AfterClass
  public static void shutdownServer()
      throws Exception {
    System.out.println("::: Stopping server :::");
    jettyServer.stop();
  }

  @Test
  public void testBootstraping() {
    RootResource rootResource = RootResourceImpl.getInstance();
    Assert.assertNotNull(rootResource);
    LoginResource loginResource = rootResource.performAuthentication("smartadmin@smart-user", "02040250204039");
    Assert.assertNotNull(loginResource);
    OrganizationResource orgResource = loginResource.getOrganizationResource();
    Assert.assertNotNull(orgResource);    
  }

//Test Started by Uzzal
  @Test
  public void doTestCreateOrganization() {
    RootResource rootResource = RootResourceImpl.getInstance();
    Assert.assertNotNull(rootResource);
    LoginResource loginResource = rootResource.performAuthentication("smartadmin@smart-user", "02040250204039");
    Assert.assertNotNull(loginResource);
    OrganizationResource orgResource = loginResource.getOrganizationResource();
    Assert.assertNotNull(orgResource);
    OrganizationsResource orgsResource = loginResource.getOrganizationsResource();
    Organization org = new Organization();

    org.setName("Smart It Engineering");
    org.setUniqueShortName("SITEL");
    Address address = new Address();
    address.setCity("Dhaka");
    address.setCountry("Bangladesh");
    address.setState("Dhaka");
    address.setStreetAddress("23/S hazi chinu miah road, Mohammadpur");
    address.setZip("1207");    
    org.setAddress(address);
    OrganizationResource newOrgResource = orgsResource.create(org);
    Assert.assertNotNull(newOrgResource);
    com.smartitengineering.user.client.api.Organization newlyCreatedOrg = newOrgResource.getOrganization();
    Assert.assertEquals(org.getName(), newlyCreatedOrg.getName());
    Assert.assertEquals(1, orgsResource.getOrganizationResources().size());
    orgsResource.get();
    Assert.assertEquals(2, orgsResource.getOrganizationResources().size());
  }

  @Test
  public void doDeleteOrganization(){
  }

  @Test
  public void doTestUpdateOrganization() {
  }
  //Test Ended by Uzzal
  //Test Method Started by Atiqul
  //Test Ended by Atiqul
}
