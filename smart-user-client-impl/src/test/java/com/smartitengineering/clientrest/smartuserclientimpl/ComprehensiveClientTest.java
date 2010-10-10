package com.smartitengineering.clientrest.smartuserclientimpl;

import com.smartitengineering.user.client.api.LoginResource;
import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.OrganizationsResource;
import com.smartitengineering.user.client.api.RootResource;
import com.smartitengineering.user.client.api.UserResource;
import com.smartitengineering.user.client.api.UsersResource;
import com.smartitengineering.user.client.impl.RootResourceImpl;
import com.smartitengineering.user.client.impl.domain.Address;
import com.smartitengineering.user.client.impl.domain.BasicIdentity;
import com.smartitengineering.user.client.impl.domain.Name;
import com.smartitengineering.user.client.impl.domain.Organization;
import com.smartitengineering.user.client.impl.domain.Person;
import com.smartitengineering.user.client.impl.domain.User;
import com.smartitengineering.user.client.impl.domain.UserPerson;
import com.smartitengineering.user.client.impl.login.LoginCenter;
import com.smartitengineering.util.rest.client.ApplicationWideClientFactoryImpl;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
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

  public static final String CHITTAGONG = "Chittagong";
  public static final String DHAKA = "Dhaka";
  public static final String BANGLADESH = "Bangladesh";
  public static final String SITEL_ORG_SHORT_NAME = "SITEL";
  public static final String SITEL_ORG_NAME = "Smart IT Engineering Ltd.";
  public static final String SITEL_ORG_ADMIN_USERNAME = "admin";
  public static final String SITEL_ORG_USER_USERNAME = "modhu";

  private static Server jettyServer;
  private static final int PORT = 9090;
  private static OrganizationsResource orgsResource;
  private static OrganizationResource sitelOrgResource;

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
//    RootResource rootResource = RootResourceImpl.getInstance();
//    Assert.assertNotNull(rootResource);
//    LoginResource loginResource = rootResource.performAuthentication("smartadmin@smart-user", "02040250204039");
//    Assert.assertNotNull(loginResource);
//    OrganizationResource orgResource = loginResource.getOrganizationResource();
//    Assert.assertNotNull(orgResource);
//    orgsResource = loginResource.getOrganizationsResource();
//    Assert.assertNotNull(orgsResource);
  }

//Test Started by Uzzal
  @Test
  public void doTestCreateOrganization() {
//    Assert.assertNotNull(orgsResource);
//    Organization org = new Organization();
//    org.setName(SITEL_ORG_NAME);
//    org.setUniqueShortName(SITEL_ORG_SHORT_NAME);
//    Address address = new Address();
//    address.setCity(DHAKA);
//    address.setCountry(BANGLADESH);
//    address.setState(DHAKA);
//    address.setStreetAddress("23/S hazi chinu miah road, Mohammadpur");
//    address.setZip("1207");
//    org.setAddress(address);
//    OrganizationResource newOrgResource = orgsResource.create(org);
//    sitelOrgResource = newOrgResource;
//    Assert.assertNotNull(newOrgResource);
//    com.smartitengineering.user.client.api.Organization newlyCreatedOrg = newOrgResource.getOrganization();
//    Assert.assertEquals(org.getName(), newlyCreatedOrg.getName());
//    Assert.assertEquals(1, orgsResource.getOrganizationResources().size());
//    orgsResource.get();
//    Assert.assertEquals(2, orgsResource.getOrganizationResources().size());
  }

  @Test
  public void doTestUpdateOrganization() {
//    Assert.assertEquals(2, orgsResource.getOrganizationResources().size());
//    for (OrganizationResource orgIterResource : orgsResource.getOrganizationResources()) {
//      if (orgIterResource.getOrganization().getUniqueShortName().equals("SITEL")) {
//        com.smartitengineering.user.client.api.Organization organization = orgIterResource.getOrganization();
//        Assert.assertNotNull(organization);
//        Assert.assertNotNull(organization.getAddress());
//        Assert.assertFalse(CHITTAGONG.equals(organization.getAddress().getCity()));
//        organization.getAddress().setCity(CHITTAGONG);
//        orgIterResource.update();
//        organization = orgIterResource.getOrganization();
//        Assert.assertEquals(CHITTAGONG, organization.getAddress().getCity());
//      }
//    }
  }

  @Test
  public void doTestCreateUser() {
//    UsersResource sitelUsersResource = sitelOrgResource.getUsersResource();
//    Assert.assertNotNull(sitelUsersResource);
//    Assert.assertEquals(1, sitelUsersResource.getUserResources().size());
//    User user = new User();
//    user.setUsername("modhu");
//    user.setPassword("modhu123");
//    Person person = new Person();
//    BasicIdentity basicIdentity = new BasicIdentity();
//    Address address = new Address();
//    Name name = new Name();
//    name.setFirstName("S");
//    name.setLastName("Gupta");
//    name.setMiddleInitial("S");
//    basicIdentity.setName(name);
//    basicIdentity.setNationalID("1234567890");
//    person.setSelf(basicIdentity);
//    address.setCity(DHAKA);
//    address.setStreetAddress("Mohammadpur");
//    address.setCountry(BANGLADESH);
//    address.setZip("1207");
//    person.setAddress(address);
//    UserPerson userPerson = new UserPerson();
//    userPerson.setUser(user);
//    userPerson.setPerson(person);
//    UserResource userResource = sitelUsersResource.create(userPerson);
//    Assert.assertEquals(SITEL_ORG_USER_USERNAME, userResource.getUser().getUser().getUsername());
  }
  //Test Ended by Uzzal
  //Test Method Started by Atiqul
  //Test Ended by Atiqul
}
