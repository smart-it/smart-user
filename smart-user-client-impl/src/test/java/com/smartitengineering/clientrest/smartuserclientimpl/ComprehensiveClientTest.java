package com.smartitengineering.clientrest.smartuserclientimpl;

import com.smartitengineering.user.client.api.LoginResource;
import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.OrganizationsResource;
import com.smartitengineering.user.client.api.PrivilegesResource;
import com.smartitengineering.user.client.api.RootResource;
import com.smartitengineering.user.client.api.SecuredObjectResource;
import com.smartitengineering.user.client.api.SecuredObjectsResource;
import com.smartitengineering.user.client.api.UserResource;
import com.smartitengineering.user.client.api.UsersResource;
import com.smartitengineering.user.client.impl.RootResourceImpl;
import com.smartitengineering.user.client.impl.domain.Address;
import com.smartitengineering.user.client.impl.domain.BasicIdentity;
import com.smartitengineering.user.client.impl.domain.Name;
import com.smartitengineering.user.client.impl.domain.Organization;
import com.smartitengineering.user.client.impl.domain.Person;
import com.smartitengineering.user.client.impl.domain.Privilege;
import com.smartitengineering.user.client.impl.domain.SecuredObject;
import com.smartitengineering.user.client.impl.domain.User;
import com.smartitengineering.user.client.impl.domain.UserPerson;
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

  public static final String CHITTAGONG = "Chittagong";
  public static final String DHAKA = "Dhaka";
  public static final String BANGLADESH = "Bangladesh";
  public static final String SITEL_ORG_SHORT_NAME = "SITEL";
  public static final String SITEL_ORG_NAME = "Smart IT EngineeringLtd.";
  public static final String SITEL_ORG_ADMIN_USERNAME = "admin";
  public static final String SITEL_ORG_USER_USERNAME = "modhu";
  public static String ORGS_OID = "test-oid";
  public static String ORGS_OID_NAME = "Smart User Organizations";
  public static Integer PRIVILEGE_PERMISSION_MASK = 31;
  public static String USERS_OID_NAME = "smart-user-users";
  public static String USERS_OID = "/users";
  private static Server jettyServer;
  private static final int PORT = 9090;
  private static OrganizationsResource orgsResource;
  private static OrganizationResource sitelOrgResource;
  private static UsersResource sitelUsersResource;
  private static UserResource sitelUserResource;

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
    orgsResource = loginResource.getOrganizationsResource();
    Assert.assertNotNull(orgsResource);
  }

//Test Started by Uzzal
  @Test
  public void doTestCreateOrganization() {
    Assert.assertNotNull(orgsResource);
    Organization org = new Organization();
    org.setName(SITEL_ORG_NAME);
    org.setUniqueShortName(SITEL_ORG_SHORT_NAME);
    Address address = new Address();
    address.setCity(DHAKA);
    address.setCountry(BANGLADESH);
    address.setState(DHAKA);
    address.setStreetAddress("23/B hazi chinu miah road, Mohammadpur");
    address.setZip("1207");
    org.setAddress(address);
    OrganizationResource newOrgResource = orgsResource.create(org);
    sitelOrgResource = newOrgResource;
    Assert.assertNotNull(newOrgResource);
    com.smartitengineering.user.client.api.Organization newlyCreatedOrg = newOrgResource.getOrganization();
    Assert.assertEquals(org.getName(), newlyCreatedOrg.getName());
    Assert.assertEquals(1, orgsResource.getOrganizationResources().size());
    orgsResource.get();
    Assert.assertEquals(2, orgsResource.getOrganizationResources().size());
  }

  @Test
  public void doTestUpdateOrganization() {
    Assert.assertEquals(2, orgsResource.getOrganizationResources().size());
    for (OrganizationResource orgIterResource : orgsResource.getOrganizationResources()) {
      if (orgIterResource.getOrganization().getUniqueShortName().equals("SITEL")) {
        com.smartitengineering.user.client.api.Organization organization = orgIterResource.getOrganization();
        Assert.assertNotNull(organization);
        Assert.assertNotNull(organization.getAddress());
        Assert.assertFalse(CHITTAGONG.equals(organization.getAddress().getCity()));
        organization.getAddress().setCity(CHITTAGONG);
        orgIterResource.update();
        organization = orgIterResource.getOrganization();
        Assert.assertEquals(CHITTAGONG, organization.getAddress().getCity());
      }
    }
  }

//  @Test
//  public void doTestDeleteOrganization() {
//    Assert.assertEquals(2, orgsResource.getOrganizationResources().size());
//    for (OrganizationResource orgIterResource : orgsResource.getOrganizationResources()) {
//      if (orgIterResource.getOrganization().getUniqueShortName().equals("SITEL")) {
//
//      }
//    }
//  }
  @Test
  public void doTestCreateUser() {
    sitelUsersResource = sitelOrgResource.getUsersResource();
    Assert.assertNotNull(sitelUsersResource);
    Assert.assertEquals(1, sitelUsersResource.getUserResources().size());
    User user = new User();
    user.setUsername("modhu");
    user.setPassword("modhu123");
    Person person = new Person();
    BasicIdentity basicIdentity = new BasicIdentity();
    Address address = new Address();
    Name name = new Name();
    name.setFirstName("S");
    name.setLastName("G");
    name.setMiddleInitial("S");
    basicIdentity.setName(name);
    basicIdentity.setNationalID("1234567890");
    person.setSelf(basicIdentity);
    address.setCity(DHAKA);
    address.setStreetAddress("Mohammadpur");
    address.setCountry(BANGLADESH);
    address.setZip("1207");
    person.setAddress(address);
    UserPerson userPerson = new UserPerson();
    userPerson.setUser(user);
    userPerson.setPerson(person);
    UserResource userResource = sitelUsersResource.create(userPerson);
    sitelUserResource = userResource;
    Assert.assertEquals(SITEL_ORG_USER_USERNAME, userResource.getUser().getUser().getUsername());
  }

  @Test
  public void doUserAuthentication() {
    RootResource rootResource = RootResourceImpl.getInstance();
    Assert.assertNotNull(rootResource);
    LoginResource loginResource = rootResource.performAuthentication("modhu", "123modhu");
    Assert.assertNull(loginResource);
    OrganizationResource orgResource = loginResource.getOrganizationResource();
    Assert.assertNotNull(orgResource);
    orgsResource = loginResource.getOrganizationsResource();
    Assert.assertNotNull(orgsResource);
  }

  @Test
  public void doTestUpdateUser() {
    sitelUsersResource = sitelOrgResource.getUsersResource();
    Assert.assertNotNull(sitelUsersResource);
    Assert.assertEquals(2, sitelUsersResource.getUserResources().size());
    for (UserResource userIterResource : sitelUsersResource.getUserResources()) {
      if (userIterResource.getUser().getUser().getUsername().equals("modhu")) {
        com.smartitengineering.user.client.api.UserPerson userPerson = userIterResource.getUser();
        Assert.assertNotNull(userPerson);
        Assert.assertNotNull(userPerson.getUser().getPassword());
        Assert.assertFalse("123modhu".equals(userPerson.getUser().getPassword()));
        userPerson.getUser().setPassword("123modhu");
        Assert.assertNotNull(userPerson.getPerson().getSelf());
        Assert.assertNotNull(userPerson.getPerson().getSelf().getName().getFirstName());
        Assert.assertFalse("Subrata".equals(userPerson.getPerson().getSelf().getName().getFirstName()));
        userPerson.getPerson().getSelf().getName().setFirstName("Subrata");
        Assert.assertNotNull(userPerson.getPerson().getSelf().getName().getLastName());
        Assert.assertFalse("Gupta".equals(userPerson.getPerson().getSelf().getName().getLastName()));
        userPerson.getPerson().getSelf().getName().setLastName("Gupta");
        Assert.assertNotNull(userPerson.getPerson().getAddress());
        Assert.assertNotNull(userPerson.getPerson().getAddress().getStreetAddress());
        Assert.assertFalse("5 Hazi Chinu Miah Road, Mohammadpur".equals(userPerson.getPerson().getAddress().
            getStreetAddress()));
        userPerson.getPerson().getAddress().setStreetAddress("5 Hazi Chinu Miah Road, Mohammadpur");
        Assert.assertFalse("1261".equals(userPerson.getPerson().getAddress().getZip()));
        userPerson.getPerson().getAddress().setZip("1261");
        userIterResource.update();
        userPerson = userIterResource.getUser();
        Assert.assertEquals("123modhu", userPerson.getUser().getPassword());
        Assert.assertEquals("Subrata", userPerson.getPerson().getSelf().getName().getFirstName());
        Assert.assertEquals("Gupta", userPerson.getPerson().getSelf().getName().getLastName());
        Assert.assertEquals("5 Hazi Chinu Miah Road, Mohammadpur",
                            userPerson.getPerson().getAddress().getStreetAddress());
        Assert.assertEquals("1261", userPerson.getPerson().getAddress().getZip());
      }
    }
  }
  @Test
  public void doTestCreatePrevileges() {
    PrivilegesResource sitelPrivResource = sitelOrgResource.getPrivilegesResource();
    PrivilegesResource sitelUserPrivResource = sitelUserResource.getPrivilegesResource();
    Assert.assertNotNull(sitelPrivResource);
    Assert.assertEquals(2, sitelPrivResource.getPrivilegeResources().size());
    Assert.assertNotNull(sitelUserPrivResource);
    Assert.assertEquals(1, sitelUserPrivResource.getPrivilegeResources().size());

    SecuredObjectsResource securedObjectsResource = sitelOrgResource.getSecuredObjectsResource();
//    List<SecuredObjectResource> securedObjectResources = securedObjectsResource.getSecuredObjectResources();

    SecuredObject securedObjectOrganization = new SecuredObject();
    securedObjectOrganization.setName(ORGS_OID_NAME);
    securedObjectOrganization.setObjectID(ORGS_OID);
    SecuredObjectResource securedObjectResource = securedObjectsResource.create(securedObjectOrganization);
    com.smartitengineering.user.client.api.SecuredObject securedObjectApi = securedObjectResource.getSecuredObjcet();
    Assert.assertEquals(ORGS_OID_NAME, securedObjectApi.getName());

    Privilege privilege = new Privilege();
    privilege.setDisplayName("Smart User Adminstration");
    privilege.setName("Organization-admin-user-privilege");
    privilege.setPermissionMask(PRIVILEGE_PERMISSION_MASK);
    privilege.setShortDescription("This admin privilege contains the authority to do any of the CRUD options");
    privilege.setSecuredObject(securedObjectApi);
//    com.smartitengineering.user.client.api.PrivilegeResource userPrivilegesResource = sitelPrivResource.create(privilege);
//    Assert.assertNotNull(userPrivilegesResource);
//    sitelUserPrivResource.create(userPrivilegesResource.getPrivilege());
//    SecuredObject securedObjectUser = new SecuredObject();
//    securedObjectUser.setName(USERS_OID_NAME);
//    securedObjectUser.setObjectID(USERS_OID);
//    securedObjectUser.setParentObjectID(securedObjectOrganization.getObjectID());
//    SecuredObjectResource securedObjectUserResource = securedObjectsResource.create(securedObjectUser);
//    com.smartitengineering.user.client.api.SecuredObject securedObjectUserApi = securedObjectUserResource.getSecuredObjcet();
//    Assert.assertEquals(USERS_OID_NAME, securedObjectUserApi.getName());
//    Assert.assertEquals(USERS_OID, securedObjectUserApi.getObjectID());
//    Assert.assertEquals(securedObjectOrganization.getObjectID(), securedObjectUserApi.getParentObjectID());
//
//    Privilege privilegeUser = new Privilege();
//    privilegeUser.setDisplayName("Admin User Profile Privilege");
//    privilegeUser.setName("organization-admin-user-privilege");
//    privilegeUser.setPermissionMask(PRIVILEGE_PERMISSION_MASK);
//    privilegeUser.setShortDescription("This privilege contains the authority to change the password and profile of the organization admin.");
//    privilegeUser.setSecuredObject(securedObjectUserApi);
//    //Assert.assertEquals("modhu",sitelUserResource.getUser().getUser().getUsername());
//    sitelUserPrivResource.create(privilegeUser);
    Assert.assertEquals("Smart User Adminstration", privilege.getDisplayName());
//    Assert.assertEquals("Admin User Profile Privilege", privilegeUser.getDisplayName());
  }
  //Test Ended by Uzzal
  //Test Method Started by Atiqul
  //Test Ended by Atiqul
}
