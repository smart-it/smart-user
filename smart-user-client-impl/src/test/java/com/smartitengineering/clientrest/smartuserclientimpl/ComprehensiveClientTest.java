package com.smartitengineering.clientrest.smartuserclientimpl;

import com.smartitengineering.user.client.api.LoginResource;
import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.OrganizationsResource;
import com.smartitengineering.user.client.api.PrivilegeResource;
import com.smartitengineering.user.client.api.PrivilegesResource;
import com.smartitengineering.user.client.api.RoleResource;
import com.smartitengineering.user.client.api.RolesResource;
import com.smartitengineering.user.client.api.RootResource;
import com.smartitengineering.user.client.api.SecuredObjectResource;
import com.smartitengineering.user.client.api.SecuredObjectsResource;
import com.smartitengineering.user.client.api.UserGroup;
import com.smartitengineering.user.client.api.UserGroupPrivilegeResource;
import com.smartitengineering.user.client.api.UserGroupPrivilegesResource;
import com.smartitengineering.user.client.api.UserGroupResource;
import com.smartitengineering.user.client.api.UserGroupRoleResource;
import com.smartitengineering.user.client.api.UserGroupRolesResource;
import com.smartitengineering.user.client.api.UserGroupUserResource;
import com.smartitengineering.user.client.api.UserGroupUsersResource;
import com.smartitengineering.user.client.api.UserGroupsResource;
import com.smartitengineering.user.client.api.UserLinkResource;
import com.smartitengineering.user.client.api.UserPrivilegeResource;
import com.smartitengineering.user.client.api.UserPrivilegesResource;
import com.smartitengineering.user.client.api.UserResource;
import com.smartitengineering.user.client.api.UserRolesResource;
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
import com.smartitengineering.user.domain.GlobalRole;
import com.smartitengineering.util.rest.client.ApplicationWideClientFactoryImpl;
import com.sun.jersey.api.client.UniformInterfaceException;
import java.io.File;
import java.util.List;
import javax.ws.rs.core.Response.Status;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ComprehensiveClientTest {

  public static final String PASSWORD = "02040250204039";
  public static final String USERNAME = "smartadmin@smart-user";
  public final String CHITTAGONG = "Chittagong";
  public final String DHAKA = "Dhaka";
  public final String BANGLADESH = "Bangladesh";
  public final String SITEL_ORG_SHORT_NAME = "SITEL";
  public final String SITEL_ORG_NAME = "Smart IT EngineeringLtd.";
  public final String SITEL_ORG_ADMIN_USERNAME = "admin";
  public final String SITEL_ORG_USER_USERNAME = "modhu";
  public final String SITEL_ADMIN_USER_NAME = "admin";
  public final String SITEL_ADMIN_USER_PASSWORD = "adminadmin";
  public final String SITEL_USER_GROUP_NAME = "sitel-user-group-1";
  public final int ORGANIZATION_NUM_AT_BEGINNING = 1;
  public final int USER_NUM_AT_BEGINNING = 1;
  public final int ORG_PRIVILEGES_NUM_AT_BEGINNING = 2;
  public final int USER_PRIVILEGES_NUM_AT_BEGINNING = 1;
  public final int ADMIN_USER_PRIVILEGES_NUM_AT_BEGINNING = 2;
  public final int NUM_OF_USER_GROUPS_AT_BEGINNING = 0;
  public final int ZERO = 0;
  public final String ORGS_OID = "test-oid";
  public final String ORGS_OID_NAME = "Smart User Organizations";
  public final Integer PRIVILEGE_PERMISSION_MASK = 31;
  public final String USERS_OID_NAME = "smart-user-users";
  public final String USERS_OID = "test-user-oid";
  private final String SITEL_ADMIN_USER_PRIVILEGE_TEST_1 = "organization-admin-user-privilege-test-1";
  private final String SITEL_ADMIN_USER_PRIVILEGE_TEST_2 = "organization-admin-user-privilege-test-2";
  private static Server jettyServer;
  private static final int PORT = 9090;
  private static OrganizationsResource orgsResource;
  private static RootResource rootResource;
  private static RolesResource rolesResource;
  private static OrganizationResource sitelOrgResource;
  private static UsersResource sitelUsersResource;
  private static UserResource sitelUserResource;
  private static PrivilegesResource sitelPrivsResource;
  private static UserPrivilegesResource sitelUserPrivsResource;
  private static UserGroupsResource sitelUserGroupsResource;
  private static UserGroupResource sitelUserGroupResource;
  private static UserGroupUsersResource sitelUserGroupUsersResource;
  private static UserGroupRolesResource sitelUserGroupRolesResource;
  private static UserGroupPrivilegesResource sitelUserGroupPrivilegesResource;
  private LoginResource sitelAdminUserLoginResource;
  private RootResource sitelAdminRootResource;
  private static final Logger LOGGER = LoggerFactory.getLogger(ComprehensiveClientTest.class);

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
  }

  @AfterClass
  public static void shutdownServer()
      throws Exception {
    System.out.println("::: Stopping server :::");
    jettyServer.stop();
  }

  private RootResource login(String username, String password) {
    return RootResourceImpl.getInstance(username, password);
  }

  @Test
  public void testBootstraping() throws InterruptedException {
    Thread.sleep(4000);
    rootResource = login(USERNAME, PASSWORD);
    Assert.assertNotNull(rootResource);
    LoginResource loginResource = rootResource.getLoginResource();
    Assert.assertNotNull(loginResource);
    rolesResource = loginResource.getRolesResource();
    Assert.assertNotNull(rolesResource);
    OrganizationResource orgResource = loginResource.getOrganizationResource();
    Assert.assertNotNull(orgResource);
    orgsResource = loginResource.getOrganizationsResource();
    Assert.assertNotNull(orgsResource);
    System.out.println("---------------------------------------------------------------------------------Organization resource5 " + orgsResource.
        getLastReadStateOfEntity().getEntries().size());
    Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING, orgsResource.getOrganizationResources().size());
    UserResource userResource = loginResource.getUserResource();
    Assert.assertNotNull(userResource);
    UserRolesResource userRolesResource = userResource.getRolesResource();
    Assert.assertNotNull(userRolesResource);
    Assert.assertNotNull(userRolesResource.getUserRoleResources());
    Assert.assertEquals(1, userRolesResource.getUserRoleResources().size());
    Assert.assertEquals(userRolesResource.getUserRoleResources().get(0).getRoleResource().getRole().getName(), GlobalRole.ROLE_ADMIN.
        toString());


  }

//Test Started by Uzzal
  @Test
  public void doTestCreateOrganization() throws InterruptedException {
    System.out.println("---------------------------------------------------------------------------------Organization resource4 " + orgsResource.
        getLastReadStateOfEntity().getEntries().size());
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
    OrganizationResource newOrgResource = null;
    System.out.println("---------------------------------------------------------------------------------Organization resource3 " + orgsResource.
        getLastReadStateOfEntity().getEntries().size());
    try {
      newOrgResource = orgsResource.create(org);
      Thread.sleep(1500);

      System.out.println("Organization created");
    }
    catch (Exception e) {
      Assert.fail("Failed to create new organization");
    }
    System.out.println("---------------------------------------------------------------------------------Organization resource2 " + orgsResource.
        getLastReadStateOfEntity().getEntries().size());
    sitelOrgResource = newOrgResource;
    Assert.assertNotNull(newOrgResource);
    com.smartitengineering.user.client.api.Organization newlyCreatedOrg = newOrgResource.getOrganization();
    Assert.assertEquals(org.getName(), newlyCreatedOrg.getName());

    System.out.println("---------------------------------------------------------------------------------Organization resource1 " + orgsResource.
        getLastReadStateOfEntity().getEntries().size());

    Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING, orgsResource.getOrganizationResources().size());
    try {
      orgsResource.get();
      Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING + 1, orgsResource.getOrganizationResources().size());
    }
    catch (Exception e) {
      Assert.fail("Expected Organization number didn't match with the actual number");
    }
    Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING + 1, orgsResource.getOrganizationResources().size());

    Organization org1 = new Organization();
    org1.setName("Agoora Limited");
    org1.setUniqueShortName("AGOORA");
    Address address1 = new Address();
    address1.setCity(DHAKA);
    address1.setCountry(BANGLADESH);
    address1.setState(DHAKA);
    address1.setStreetAddress("15 Dhanmondi, Dhaka");
    address1.setZip("1201");
    org1.setAddress(address1);
    try {
      newOrgResource = orgsResource.create(org1);
      Thread.sleep(1500);
    }
    catch (Exception e) {
      Assert.fail("Failed to create new organization");
    }
    com.smartitengineering.user.client.api.Organization newlyCreatedOrg1 = newOrgResource.getOrganization();
    Assert.assertEquals(org1.getName(), newlyCreatedOrg1.getName());
    Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING + 1, orgsResource.getOrganizationResources().size());
    try {

      orgsResource.get();
      Assert.assertEquals(org1.getName(), newlyCreatedOrg1.getName());
      Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING + 2, orgsResource.getOrganizationResources().size());
    }
    catch (Exception e) {
      Assert.fail("Expected organizations number and newly created organization name didn't match with the actual");
    }

  }


  @Test
  public void doTestUpdateOrganization() throws InterruptedException {
    Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING + 2, orgsResource.getOrganizationResources().size());
    for (OrganizationResource orgIterResource : orgsResource.getOrganizationResources()) {
      Organization updatableOrganization = (Organization) orgIterResource.getOrganization();
      LOGGER.info(updatableOrganization.getUniqueShortName());
      if (updatableOrganization.getUniqueShortName().equals("SITEL")) {
        com.smartitengineering.user.client.api.Organization organization = orgIterResource.getOrganization();
        Assert.assertNotNull(organization);
        Assert.assertNotNull(organization.getAddress());
        Assert.assertFalse(CHITTAGONG.equals(organization.getAddress().getCity()));
        organization.getAddress().setCity(CHITTAGONG);
        try {
          orgIterResource.update();
        }
        catch (Exception e) {
          Assert.fail("Exception due to failure of updating particular orgnization");
        }
        try {
          orgIterResource.get();
          organization = orgIterResource.getOrganizationReloaded();
          Assert.assertEquals(CHITTAGONG, organization.getAddress().getCity());
        }
        catch (Exception e) {
          Assert.fail("Expected city doesn't match with the actual");
        }
      }
    }
  }
  

  @Test
  public void doInitialTest() throws InterruptedException {
    LOGGER.info("starting getting user resource");
    sitelUsersResource = sitelOrgResource.getUsersResource();
    LOGGER.info("the total number users: " + sitelUsersResource.getUserResources().size());
    List<UserResource> userResources = sitelUsersResource.getUserResources();
    Assert.assertNotNull(orgsResource);
    Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING + 2, orgsResource.getOrganizationResources().size());
    Assert.assertNotNull(sitelUsersResource);
    Assert.assertEquals(USER_NUM_AT_BEGINNING, sitelUsersResource.getUserResources().size());
    Assert.assertEquals(SITEL_ADMIN_USER_NAME, userResources.get(0).getUser().getUser().getUsername());
    Assert.assertEquals(SITEL_ADMIN_USER_PASSWORD, userResources.get(0).getUser().getUser().getPassword());
    sitelPrivsResource = sitelOrgResource.getPrivilegesResource();
    Assert.assertNotNull(sitelPrivsResource);
    Assert.assertEquals(ORG_PRIVILEGES_NUM_AT_BEGINNING, sitelPrivsResource.getPrivilegeResources().size());
  }

  @Test
  public void doTestCreateUser() throws InterruptedException {
    sitelUsersResource = sitelOrgResource.getUsersResource();
    Assert.assertNotNull(sitelUsersResource);
    Assert.assertEquals(USER_NUM_AT_BEGINNING, sitelUsersResource.getUserResources().size());
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
    UserResource userResource = null;
    try {
      userResource = sitelUsersResource.create(userPerson);
      Assert.fail("Should not be created without primary Email ");
    }
    catch (UniformInterfaceException e) {
      Assert.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), e.getResponse().getStatus());
    }
    person.setPrimaryEmail("subrata@smartitengineering.com");
    userPerson.setPerson(person);
    userResource = sitelUsersResource.create(userPerson);
    Thread.sleep(1500);
    sitelUserResource = userResource;    
    Assert.assertEquals(SITEL_ORG_USER_USERNAME, userResource.getUserReloaded().getUser().getUsername());


    OrganizationResource organizationResource = sitelUserResource.getOrganizationResource();
    Assert.assertNotNull(organizationResource);
    Assert.assertTrue(organizationResource.getOrganization().getUniqueShortName().equals(sitelOrgResource.
        getOrganization().getUniqueShortName()));


  }  
  

  @Test
  public void doTestCreateAnotherUser() {
    sitelUsersResource = sitelOrgResource.getUsersResource();
    Assert.assertNotNull(sitelUsersResource);
    Assert.assertEquals(USER_NUM_AT_BEGINNING + 1, sitelUsersResource.getUserResources().size());
    User user = new User();
    user.setUsername("russel");
    user.setPassword("russel123");
    Person person = new Person();
    BasicIdentity basicIdentity = new BasicIdentity();
    Address address = new Address();
    Name name = new Name();
    name.setFirstName("Mohiuddin");
    name.setLastName("Russel");
    name.setMiddleInitial("K");
    basicIdentity.setName(name);
    basicIdentity.setNationalID("9876543210");
    person.setSelf(basicIdentity);
    address.setCity(DHAKA);
    address.setStreetAddress("Mohammadpur");
    address.setCountry(BANGLADESH);
    address.setZip("1207");
    person.setAddress(address);
    person.setPrimaryEmail("russel@smartitengineering.com");
    UserPerson userPerson = new UserPerson();
    userPerson.setUser(user);
    userPerson.setPerson(person);
    UserResource userResource = null;
    try {
      userResource = sitelUsersResource.create(userPerson);
      Thread.sleep(1500);
    }
    catch (Exception e) {
      Assert.fail("Exception due to failure of creating an userperson");
    }
    Assert.assertEquals("russel", userResource.getUserReloaded().getUser().getUsername());
  }
  //  @Test
  //  public void doUserAuthentication() {
  //    RootResource rootResource = RootResourceImpl.getInstance();
  //    Assert.assertNotNull(rootResource);
  //    LoginResource loginResource = rootResource.performAuthentication("modhu", "123modhu");
  //    Assert.assertNull(loginResource);
  //    OrganizationResource orgResource = loginResource.getOrganizationResource();
  //    Assert.assertNotNull(orgResource);
  //    orgsResource = loginResource.getOrganizationsResource();
  //    Assert.assertNotNull(orgsResource);
  //  }

  @Test
  public void doTestUpdateUser() throws InterruptedException {
    try {
      Thread.sleep(1500);
      sitelUsersResource = sitelOrgResource.getUsersResource();
      Assert.assertNotNull(sitelUsersResource);
      Assert.assertEquals(USER_NUM_AT_BEGINNING + 2, sitelUsersResource.getUserResources().size());
    }
    catch (Exception e) {
      Assert.fail("Expected number of users doesn't match with the actual");
    }
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
        try {
          userIterResource.update();
          Thread.sleep(5000);
        }
        catch (Exception e) {
          Assert.fail("Exception due to failure of updating particular user information");
        }        
      }
    }
    try {      
      sitelUsersResource = sitelOrgResource.getUsersResource();
      Assert.assertNotNull(sitelUsersResource);
      Assert.assertEquals(USER_NUM_AT_BEGINNING + 2, sitelUsersResource.getUserResources().size());
    }
    catch (Exception e) {
      Assert.fail("Expected number of users doesn't match with the actual");
    }
    for (UserResource userIterResource : sitelUsersResource.getUserResources()) {
      if (userIterResource.getUser().getUser().getUsername().equals("modhu")) {
        UserPerson userPerson = (UserPerson) userIterResource.getUser();
        Assert.assertEquals("123modhu", userPerson.getUser().getPassword());
        Assert.assertEquals("Subrata", userPerson.getPerson().getSelf().getName().getFirstName());
        Assert.assertEquals("Gupta", userPerson.getPerson().getSelf().getName().getLastName());
        Assert.assertEquals("5 Hazi Chinu Miah Road, Mohammadpur",
                            userPerson.getPerson().getAddress().getStreetAddress());
        Assert.assertEquals("1261", userPerson.getPerson().getAddress().getZip());
      }
    }
    System.out.println("end of do test update user");
  }

  @Test
  public void doTestUserUpdateSelf() throws InterruptedException {
    System.out.println("starting do test update user self");

    RootResource modhuRootResource = login("modhu@SITEL", "123modhu");
    LoginResource modhuLoginResource = modhuRootResource.getLoginResource();
    UserResource userResource = modhuLoginResource.getUserResource();
    User user = (User) userResource.getUser().getUser();
    Assert.assertEquals(user.getUsername(), "modhu");
    userResource.getUser().getUser().setPassword("modhu123updated");
    try {
      userResource.update();
    }
    catch (Exception e) {
      Assert.fail("Should not throw any exception");
    }
    Thread.sleep(7000);

    modhuRootResource = login("modhu@SITEL", "modhu123updated");
    UserResource updatedUserResource = modhuRootResource.getLoginResource().getUserResource();
    Assert.assertEquals("modhu123updated", updatedUserResource.getUser().getUser().getPassword());
    System.out.println("end of do test update user self");
  }

  @Test
  public void doTestCreatePrivilegesofOrganization() {
    System.out.println("starting do test create privileges of organization");
    try {
      sitelPrivsResource = sitelOrgResource.getPrivilegesResource();
      Assert.assertNotNull(sitelPrivsResource);
      Assert.assertEquals(ORG_PRIVILEGES_NUM_AT_BEGINNING + 2, sitelPrivsResource.getPrivilegeResources().size());
    }
    catch (Exception e) {
      Assert.fail("Expected number of privileges doesn't match with the actual number");
    }
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
    privilege.setName(SITEL_ADMIN_USER_PRIVILEGE_TEST_1);
    privilege.setPermissionMask(PRIVILEGE_PERMISSION_MASK);
    privilege.setShortDescription("This admin privilege contains the authority to do any of the CRUD options");
    privilege.setSecuredObject(securedObjectApi);
    com.smartitengineering.user.client.api.PrivilegeResource orgPrivilegesResource = null;
    try {
      orgPrivilegesResource = sitelPrivsResource.create(
          privilege);
      Thread.sleep(1500);
    }
    catch (Exception e) {
      Assert.fail("Exception due to failure of creating organization privileges");
    }
    try {
      sitelPrivsResource.get();
      Assert.assertEquals(ORG_PRIVILEGES_NUM_AT_BEGINNING + 3, sitelPrivsResource.getPrivilegeResources().size());
      Assert.assertNotNull(orgPrivilegesResource);
      Assert.assertEquals("Smart User Adminstration", privilege.getDisplayName());
    }
    catch (Exception e) {
      Assert.fail("Exception because number of expected privileges didn't match with the actual");
    }
  }

  @Test
  public void doTestAddPrivilegesToUser() {
    sitelPrivsResource = sitelOrgResource.getPrivilegesResource();
    sitelUserPrivsResource = sitelUserResource.getPrivilegesResource();
    Assert.assertNotNull(sitelUserPrivsResource);
    Assert.assertEquals(USER_PRIVILEGES_NUM_AT_BEGINNING, sitelUserPrivsResource.getUserPrivilegeResources().size());
    SecuredObjectsResource securedObjectsResource = sitelOrgResource.getSecuredObjectsResource();
    SecuredObject securedObjectOrganization = new SecuredObject();
    SecuredObject securedObjectUser = new SecuredObject();
    securedObjectUser.setName(USERS_OID_NAME);
    securedObjectUser.setObjectID(USERS_OID);
    securedObjectUser.setParentObjectID(securedObjectOrganization.getObjectID());
    SecuredObjectResource securedObjectUserResource = securedObjectsResource.create(securedObjectUser);
    com.smartitengineering.user.client.api.SecuredObject securedObjectUserApi = securedObjectUserResource.
        getSecuredObjcet();
    Assert.assertEquals(USERS_OID_NAME, securedObjectUserApi.getName());
    Assert.assertEquals(USERS_OID, securedObjectUserApi.getObjectID());
    Assert.assertEquals(securedObjectOrganization.getObjectID(), securedObjectUserApi.getParentObjectID());
    Privilege privilegeUser = new Privilege();
    privilegeUser.setDisplayName("Admin User Profile Privilege");
    privilegeUser.setName(SITEL_ADMIN_USER_PRIVILEGE_TEST_2);
    privilegeUser.setPermissionMask(PRIVILEGE_PERMISSION_MASK);
    privilegeUser.setShortDescription(
        "This privilege contains the authority to change the password and profile of the organization admin.");
    privilegeUser.setSecuredObject(securedObjectUserApi);
    PrivilegeResource privilegeResource = null;
    try {
      privilegeResource = sitelPrivsResource.create(privilegeUser);
      Thread.sleep(1500);
    }
    catch (Exception e) {
      Assert.fail("Exception due to failure of creating privileges for user");
    }
    try {
      sitelUserPrivsResource.add(privilegeResource.getPrivilege());
    }
    catch (Exception e) {
      Assert.fail("Exception due to failure of adding privileges to user");
    }
    sitelUserPrivsResource.get();
    privilegeUser = (Privilege) privilegeResource.getPrivilege();
    Assert.assertEquals(USER_PRIVILEGES_NUM_AT_BEGINNING + 1,
                        sitelUserPrivsResource.getUserPrivilegeResources().size());
    Assert.assertEquals("Admin User Profile Privilege", privilegeUser.getDisplayName());
    Assert.assertEquals(USERS_OID, privilegeUser.getSecuredObject().getObjectID());
  }

  @Test
  public void doTestRemoveUserPrivilegeFromUser() throws InterruptedException {
    sitelUserPrivsResource = sitelUserResource.getPrivilegesResource();
    List<UserPrivilegeResource> userPrivilegeResources = sitelUserPrivsResource.getUserPrivilegeResources();
    for (UserPrivilegeResource userPrivilegeResource : userPrivilegeResources) {
      if (userPrivilegeResource.getPrivilegeResource().getPrivilege().getName().equals(
          SITEL_ADMIN_USER_PRIVILEGE_TEST_2)) {
        try {
          userPrivilegeResource.delete();
        }
        catch (Exception e) {
          Assert.fail("Exception due to failure of deleting the user privilege resource");
        }
      }
    }
    Thread.sleep(5500);
    try {
      sitelUserPrivsResource.get();
      Assert.assertEquals(USER_PRIVILEGES_NUM_AT_BEGINNING, sitelUserPrivsResource.getUserPrivilegeResources().size());
      Assert.assertNotNull(sitelUserPrivsResource);
    }
    catch (Exception e) {
      Assert.fail("Exception because expected number of privileges doesn't match with the actual number");
    }
  }

  @Test
  public void doTestCreateUserGroup() {
    sitelUserGroupsResource = sitelOrgResource.getUserGroupsResource();
    Assert.assertNotNull(sitelUserGroupsResource);
    UserGroupResource userGroupResource = null;
    UserGroup userGroup = new com.smartitengineering.user.client.impl.domain.UserGroup();
    userGroup.setName(SITEL_USER_GROUP_NAME);
    try {
      userGroupResource = sitelUserGroupsResource.create(userGroup);
      Thread.sleep(1500);
    }
    catch (Exception e) {
      Assert.fail("Exception due to failure of creation user group");
    }
    sitelUserGroupResource = userGroupResource;
    sitelUserGroupsResource.get();
    Assert.assertNotNull(sitelUserGroupsResource);
    Assert.assertEquals(NUM_OF_USER_GROUPS_AT_BEGINNING + 1, sitelUserGroupsResource.getUserGroupResources().size());
    Assert.assertEquals(SITEL_USER_GROUP_NAME, sitelUserGroupResource.getUserGroup().getName());
  }

  @Test
  public void doTestAddUsersToUserGroup() {
    sitelUserGroupUsersResource = sitelUserGroupResource.getUserGroupUsersResource();
    Assert.assertEquals(ZERO, sitelUserGroupUsersResource.getUserGroupUserResources().size());
    UsersResource usersResource = sitelOrgResource.getUsersResource();
    List<UserResource> userResources = usersResource.getUserResources();

    for (UserGroupResource userGroupResource : sitelUserGroupsResource.getUserGroupResources()) {
      if (userGroupResource.getUserGroup().getName().equals(SITEL_USER_GROUP_NAME)) {
        for (UserResource userResource : userResources) {
          com.smartitengineering.user.client.api.User user = userResource.getUser().getUser();
          if (user.getUsername().equals(SITEL_ORG_USER_USERNAME)) {
            sitelUserGroupUsersResource.add(user);
          }
        }
      }
    }
    sitelUserGroupUsersResource.get();
    Assert.assertEquals(1, sitelUserGroupUsersResource.getUserGroupUserResources().size());
  }

  @Test
  public void doTestRemoveUserFromUserGroup() throws InterruptedException {
    List<UserGroupUserResource> userGroupUserResources = sitelUserGroupUsersResource.getUserGroupUserResources();
    for (UserGroupUserResource userGroupUserResource : userGroupUserResources) {
      com.smartitengineering.user.client.api.UserPerson user = userGroupUserResource.getUserResource().getUser();
      if (user.getUser().getUsername().equals(SITEL_ORG_USER_USERNAME)) {
        userGroupUserResource.delete();
      }
    }
    Thread.sleep(5500);
    sitelUserGroupUsersResource.get();
    Assert.assertEquals(0, sitelUserGroupUsersResource.getUserGroupUserResources().size());
  }

  @Test
  public void doTestAddPrivilegesToUserGroup() throws InterruptedException {
    sitelUserGroupPrivilegesResource = sitelUserGroupResource.getUserGroupPrivilegesResource();
    Assert.assertEquals(ZERO, sitelUserGroupPrivilegesResource.getUserGroupPrivilegeResources().size());

    PrivilegesResource privilegesResource = sitelOrgResource.getPrivilegesResource();
    List<PrivilegeResource> privilegeResources = privilegesResource.getPrivilegeResources();

    for (UserGroupResource userGroupResource : sitelUserGroupsResource.getUserGroupResources()) {
      if (userGroupResource.getUserGroup().getName().equals(SITEL_USER_GROUP_NAME)) {
        for (PrivilegeResource privilegeResource : privilegeResources) {
          com.smartitengineering.user.client.api.Privilege privilege = privilegeResource.getPrivilege();
          LOGGER.info("%%%%%%%Privilege Name" + privilege.getName());
          if (privilege.getName().equals(SITEL_ADMIN_USER_PRIVILEGE_TEST_2)) {
            sitelUserGroupPrivilegesResource.add(privilege);
          }
        }
      }
    }
    Thread.sleep(1500);
    sitelUserGroupPrivilegesResource.get();
    Assert.assertEquals(1, sitelUserGroupPrivilegesResource.getUserGroupPrivilegeResources().size());
  }

  @Test
  public void doTestRemovePrivilegeFromUserGroup() {
    List<UserGroupPrivilegeResource> userGroupPrivilegeResources = sitelUserGroupPrivilegesResource.
        getUserGroupPrivilegeResources();
    for (UserGroupPrivilegeResource userGroupPrivilegeResource : userGroupPrivilegeResources) {
      com.smartitengineering.user.client.api.Privilege privilege = userGroupPrivilegeResource.getPrivilegeResource().
          getPrivilege();
      if (privilege.getName().equals(SITEL_ADMIN_USER_PRIVILEGE_TEST_2)) {
        userGroupPrivilegeResource.delete();
      }
    }
    sitelUserGroupPrivilegesResource.get();
    Assert.assertEquals(0, sitelUserGroupPrivilegesResource.getUserGroupPrivilegeResources().size());
  }

  @Test
  public void doTestAddRoleToUserGroup() {
    sitelUserGroupRolesResource = sitelUserGroupResource.getUserGroupRolesResource();
    Assert.assertEquals(ZERO, sitelUserGroupRolesResource.getUserGroupRoleResources().size());


    List<RoleResource> roleResources = rolesResource.getRoleResources();

    for (UserGroupResource userGroupResource : sitelUserGroupsResource.getUserGroupResources()) {
      if (userGroupResource.getUserGroup().getName().equals(SITEL_USER_GROUP_NAME)) {
        for (RoleResource roleResource : roleResources) {
          com.smartitengineering.user.client.api.Role role = roleResource.getRole();
          if (role.getName().equals(GlobalRole.ROLE_READ.name())) {
            sitelUserGroupRolesResource.add(role);
          }
        }
      }
    }
    sitelUserGroupRolesResource.get();
    Assert.assertEquals(1, sitelUserGroupRolesResource.getUserGroupRoleResources().size());
  }

  @Test
  public void doTestRemoveRoleFromUserGroup() {
    List<UserGroupRoleResource> userGroupRoleResources = sitelUserGroupRolesResource.getUserGroupRoleResources();
    for (UserGroupRoleResource userGroupRoleResource : userGroupRoleResources) {
      com.smartitengineering.user.client.api.Role role = userGroupRoleResource.getRoleResource().getRole();
      if (role.getName().equals(GlobalRole.ROLE_READ.name())) {
        userGroupRoleResource.delete();
      }
    }
    sitelUserGroupRolesResource.get();
    Assert.assertEquals(0, sitelUserGroupRolesResource.getUserGroupRoleResources().size());
  }
//  @Test
//  public void doTestAuthorization() {
//    doTestAuthoriztionForAdminUser();
//    doTestAuthorizationForUser();
//    doTestAuthorizationForUserGroup();
//  }

  @Test
  public void doTestAuthoriztionForAdminUser() {
    sitelAdminRootResource = login("admin@SITEL", "adminadmin");
    sitelAdminUserLoginResource = sitelAdminRootResource.getLoginResource();
    Assert.assertNotNull(sitelAdminUserLoginResource);
    try {
      sitelAdminUserLoginResource.getOrganizationsResource();
      Assert.fail("Should not be able to get the resource");
    }
    catch (UniformInterfaceException e) {
      Assert.assertEquals(Status.FORBIDDEN.getStatusCode(), e.getResponse().getStatus());
    }
    catch (Exception e) {
      Assert.fail("Should not throw any other exception");
    }
    try {
      sitelOrgResource = sitelAdminUserLoginResource.getOrganizationResource();
      Assert.assertEquals(SITEL_ORG_SHORT_NAME, sitelOrgResource.getOrganization().getUniqueShortName());
    }
    catch (Exception e) {
      Assert.fail("Should not throw any exception");
    }
    try {
      sitelUsersResource = sitelOrgResource.getUsersResource();
      Assert.assertEquals(USER_NUM_AT_BEGINNING + 2, sitelUsersResource.getUserResources().size());
    }
    catch (Exception e) {
      Assert.fail("Should not throw any exception");
    }
    try {
      sitelPrivsResource = sitelOrgResource.getPrivilegesResource();
    }
    catch (Exception e) {
      Assert.fail("Should not throw any exception");
    }
    for (PrivilegeResource privilegeResource : sitelPrivsResource.getPrivilegeResources()) {
      if (privilegeResource.getPrivilege().getName().equals(SITEL_ADMIN_USER_PRIVILEGE_TEST_2)) {
        com.smartitengineering.user.client.api.Privilege privilege = privilegeResource.getPrivilege();
        privilege.setDisplayName(privilege.getDisplayName() + "_updated");
        privilegeResource.update();
      }
    }

    UserResource sitelAdminUserResource = sitelAdminUserLoginResource.getUserResource();
    UserPrivilegesResource sitelAdminUserPrivilegesResource = sitelAdminUserResource.getPrivilegesResource();
    Assert.assertEquals(ADMIN_USER_PRIVILEGES_NUM_AT_BEGINNING, sitelAdminUserPrivilegesResource.
        getUserPrivilegeResources().size());


    List<PrivilegeResource> privilegeResources = sitelPrivsResource.getPrivilegeResources();
    for (PrivilegeResource privilegeResource : privilegeResources) {
      com.smartitengineering.user.client.api.Privilege privilege = privilegeResource.getPrivilege();
      if (privilege.getName().equals(SITEL_ADMIN_USER_PRIVILEGE_TEST_2)) {
        sitelAdminUserPrivilegesResource.add(privilege);
      }
    }
    sitelAdminUserPrivilegesResource.get();
    Assert.assertEquals(ADMIN_USER_PRIVILEGES_NUM_AT_BEGINNING + 1, sitelAdminUserPrivilegesResource.
        getUserPrivilegeResources().size());

    User user = new User();
    user.setUsername("saumitra");
    user.setPassword("saumitra123");
    Person person = new Person();
    person.setPrimaryEmail("saumitra@smartitengineering.com");
    UserPerson userPerson = new UserPerson();
    userPerson.setUser(user);
    userPerson.setPerson(person);
    UserResource saumitraUserResource = sitelUsersResource.create(userPerson);
    Assert.assertEquals("saumitra", saumitraUserResource.getUser().getUser().getUsername());

    UserPrivilegesResource saumitraPrivilegesResource = saumitraUserResource.getPrivilegesResource();
    for (PrivilegeResource privilegeResource : privilegeResources) {
      com.smartitengineering.user.client.api.Privilege privilege = privilegeResource.getPrivilege();
      if (privilege.getName().contains("SITEL-admin")) {
        saumitraPrivilegesResource.add(privilege);
      }
    }
  }

  @Test
  public void doTestGetUser() {
    rootResource = login("smartadmin@smart-user", "02040250204039");
    Assert.assertNotNull(rootResource);
    LoginResource loginResource = rootResource.getLoginResource();
    Assert.assertNotNull(loginResource);
    UserLinkResource userLinkResource = loginResource.getUserLinkResource("smartadmin@smart-user");
    Assert.assertNotNull(loginResource);
    com.smartitengineering.user.client.api.User user = userLinkResource.getUserResource().getUser().getUser();
    Assert.assertTrue(user.getUsername().equals("smartadmin"));
    Assert.assertTrue(user.getPassword().equals("02040250204039"));
  }

  @Test
  public void doTestAuthorizationForUser() throws InterruptedException {
    Thread.sleep(3400);
    RootResource saumitraRootResource = login("saumitra@SITEL", "saumitra123");
    verifyAdminPrivilege(saumitraRootResource);
    sitelOrgResource = saumitraRootResource.getLoginResource().getOrganizationResource();

    List<UserGroupResource> userGroupResources = sitelOrgResource.getUserGroupsResource().getUserGroupResources();
    for (UserGroupResource userGroupResource : userGroupResources) {
      if (userGroupResource.getUserGroup().getName().equals(SITEL_USER_GROUP_NAME)) {
        sitelUserGroupUsersResource = userGroupResource.getUserGroupUsersResource();
        sitelUserGroupPrivilegesResource = userGroupResource.getUserGroupPrivilegesResource();
      }
    }
    Assert.assertEquals(ZERO, sitelUserGroupUsersResource.getUserGroupUserResources().size());
    Assert.assertEquals(ZERO, sitelUserGroupPrivilegesResource.getUserGroupPrivilegeResources().size());
    UsersResource usersResource = sitelOrgResource.getUsersResource();
    List<UserResource> userResources = usersResource.getUserResources();
    PrivilegesResource privilegesResource = sitelOrgResource.getPrivilegesResource();
    List<PrivilegeResource> privilegeResources = privilegesResource.getPrivilegeResources();

    UserGroupUserResource sitelUserGroupUserResource;
    UserGroupPrivilegeResource sitelUserGroupPrivilegeResource;

    for (UserGroupResource userGroupResource : sitelUserGroupsResource.getUserGroupResources()) {
      if (userGroupResource.getUserGroup().getName().equals(SITEL_USER_GROUP_NAME)) {
        for (UserResource userResource : userResources) {
          com.smartitengineering.user.client.api.User user = userResource.getUser().getUser();
          if (user.getUsername().equals(SITEL_ORG_USER_USERNAME)) {
            try {
              sitelUserGroupUserResource = sitelUserGroupUsersResource.add(user);
            }
            catch (Exception e) {
              Assert.fail("should not throw any exception");
            }

          }
        }
        for (PrivilegeResource privilegeResource : privilegeResources) {
          com.smartitengineering.user.client.api.Privilege privilege = privilegeResource.getPrivilege();
          if (privilege.getName().contains("SITEL-admin")) {
            try {
              sitelUserGroupPrivilegeResource = sitelUserGroupPrivilegesResource.add(privilege);
            }
            catch (Exception e) {
              Assert.fail("should not throw any exception");
            }
          }
        }
      }
    }
  }

  @Test
  public void doTestAuthorizationForUserGroup() {
    RootResource modhuRootResource = login("modhu@SITEL", "modhu123updated");
    verifyAdminPrivilege(modhuRootResource);
  }

  @Test
  public void doTestDeleteOrganization() throws InterruptedException {
    rootResource = login(USERNAME, PASSWORD);
    System.out.println("------------------------------------------------------------------Smart admin re login");
    LoginResource loginResource = rootResource.getLoginResource();
    orgsResource = loginResource.getOrganizationsResource();
    UsersResource usersResource;
    PrivilegesResource privsResource;
    List<OrganizationResource> organizationResources =
                               loginResource.getOrganizationsResource().getOrganizationResources();
    for (OrganizationResource organizationResource : organizationResources) {
      if (organizationResource.getOrganization().getUniqueShortName().equals("SITEL")) {
        usersResource = organizationResource.getUsersResource();
        Assert.assertNotNull(usersResource);
        privsResource = organizationResource.getPrivilegesResource();
        Assert.assertNotNull(privsResource);
        sitelUsersResource = usersResource;
        sitelPrivsResource = privsResource;
      }
    }

    sitelUserPrivsResource.get();
    Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING + 2, orgsResource.getOrganizationResources().size());
    Assert.assertNotNull(sitelUsersResource);
    Assert.assertEquals(USER_NUM_AT_BEGINNING + 3, sitelUsersResource.getUserResources().size());
    Assert.assertNotNull(sitelPrivsResource);
    Assert.assertEquals(ORG_PRIVILEGES_NUM_AT_BEGINNING + 5, sitelPrivsResource.getPrivilegeResources().size());
    try {
      sitelUsersResource = sitelOrgResource.getUsersResource();
      sitelPrivsResource = sitelOrgResource.getPrivilegesResource();
      sitelUserPrivsResource = sitelUserResource.getPrivilegesResource();
    }
    catch (Exception e) {
      Assert.fail("Exception due to failure to get userresource and privilegesresource");
    }
    System.out.println("-------------------Start");
    for (OrganizationResource orgIterResource : orgsResource.getOrganizationResources()) {
      System.out.println("------------------------ orgname " + orgIterResource.getOrganization().getUniqueShortName());
      if (orgIterResource.getOrganization().getUniqueShortName().equals("SITEL")) {
        try {
          orgIterResource.delete();
        }
        catch (Exception e) {
          Assert.fail("Exception due to failure of deleting the organization");
        }
      }
    }
    Thread.sleep(5500);
  }

  @Test
  public void doTestUsersAndPrivsAfterRemovingOrg() throws InterruptedException {
    RootResource adminRootResource = login(USERNAME, PASSWORD);
    orgsResource = adminRootResource.getLoginResource().getOrganizationsResource();
    List<OrganizationResource> organizationResources = orgsResource.getOrganizationResources();
    for (OrganizationResource organizationResource : organizationResources) {
      if (organizationResource.getOrganization().getUniqueShortName().equals("SITEL")) {
        Assert.fail("SITEL organization should not exist");
      }
    }
  }

  public void verifyAdminPrivilege(RootResource adminPrivilegeTestRootResource) {

    LoginResource loginResource = adminPrivilegeTestRootResource.getLoginResource();
    Assert.assertNotNull(loginResource);
    OrganizationResource orgResource;


    try {
      loginResource.getOrganizationsResource();
      Assert.fail("Should not be able to get the resource");
    }
    catch (UniformInterfaceException e) {
      Assert.assertEquals(Status.FORBIDDEN.getStatusCode(), e.getResponse().getStatus());
    }
    catch (Exception e) {
      Assert.fail("Should not throw any other exception");
    }

    try {
      Thread.sleep(5500);
      loginResource.getOrganizationResource();
    }
    catch (Exception e) {
      Assert.fail("Should not throw any exception");
    }

    try {
      orgResource = loginResource.getOrganizationResource();
    }
    catch (Exception e) {
      Assert.fail("Should not throw any exception");
    }

  }  
  //Test Ended by Uzzal {
}
