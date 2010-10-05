package com.smartitengineering.clientrest.smartuserclientimpl;

import com.smartitengineering.user.client.api.LoginResource;
import com.smartitengineering.user.client.api.OrganizationResource;
import com.smartitengineering.user.client.api.OrganizationsResource;
import com.smartitengineering.user.client.api.PrivilegeResource;
import com.smartitengineering.user.client.api.PrivilegesResource;
import com.smartitengineering.user.client.api.RootResource;
import com.smartitengineering.user.client.api.SecuredObjectResource;
import com.smartitengineering.user.client.api.SecuredObjectsResource;
import com.smartitengineering.user.client.api.UserGroup;
import com.smartitengineering.user.client.api.UserGroupResource;
import com.smartitengineering.user.client.api.UserGroupUserResource;
import com.smartitengineering.user.client.api.UserGroupUsersResource;
import com.smartitengineering.user.client.api.UserGroupsResource;
import com.smartitengineering.user.client.api.UserPrivilegeResource;
import com.smartitengineering.user.client.api.UserPrivilegesResource;
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
  public static final String SITEL_ADMIN_USER_NAME = "admin";
  public static final String SITEL_ADMIN_USER_PASSWORD = "adminadmin";
  public static final String SITEL_USER_GROUP_NAME = "sitel-user-group-1";
  public static final int ORGANIZATION_NUM_AT_BEGINNING = 1;
  public static final int USER_NUM_AT_BEGINNING = 1;
  public static final int ORG_PRIVILEGES_NUM_AT_BEGINNING = 1;
  public static final int USER_PRIVILEGES_NUM_AT_BEGINNING = 1;
  public static final int NUM_OF_USER_GROUPS_AT_BEGINNING = 1;
  public static final int ZERO = 0;
  public static String ORGS_OID = "test-oid";
  public static String ORGS_OID_NAME = "Smart User Organizations";
  public static Integer PRIVILEGE_PERMISSION_MASK = 31;
  public static String USERS_OID_NAME = "smart-user-users";
  public static String USERS_OID = "test-user-oid";
  private static Server jettyServer;
  private static final int PORT = 9090;
  private static OrganizationsResource orgsResource;
  private static OrganizationResource sitelOrgResource;
  private static UsersResource sitelUsersResource;
  private static UserResource sitelUserResource;
  private static PrivilegesResource sitelPrivsResources;
  private static UserPrivilegesResource sitelUserPrivsResource;
  private static UserGroupsResource sitelUserGroupsResource;
  private static UserGroupResource sitelUserGroupResource;
  private static UserGroupUsersResource sitelUserGroupUsersResource;
  private static UserGroupUserResource sitelUserGroupUserResource;

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
    OrganizationResource newOrgResource = null;
    try {
      newOrgResource = orgsResource.create(org);
    }
    catch (Exception e) {
      Assert.fail("Failed to create new organization");
    }
    sitelOrgResource = newOrgResource;
    Assert.assertNotNull(newOrgResource);
    com.smartitengineering.user.client.api.Organization newlyCreatedOrg = newOrgResource.getOrganization();
    Assert.assertEquals(org.getName(), newlyCreatedOrg.getName());
    Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING, orgsResource.getOrganizationResources().size());
    try {
      orgsResource.get();
      Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING + 1, orgsResource.getOrganizationResources().size());
    }
    catch (Exception e) {
      Assert.fail("Expected Organization number didn't match with the actual number");
    }
//    Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING + 1, orgsResource.getOrganizationResources().size());

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
  public void doTestUpdateOrganization() {
    Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING + 2, orgsResource.getOrganizationResources().size());
    for (OrganizationResource orgIterResource : orgsResource.getOrganizationResources()) {
      if (orgIterResource.getOrganization().getUniqueShortName().equals("SITEL")) {
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
          organization = orgIterResource.getOrganization();
          Assert.assertEquals(CHITTAGONG, organization.getAddress().getCity());
        }
        catch (Exception e) {
          Assert.fail("Expected city doesn't match with the actual");
        }
      }
    }
  }

  @Test
  public void doInitialTest() {
    sitelUsersResource = sitelOrgResource.getUsersResource();
    List<UserResource> userResources = sitelUsersResource.getUserResources();
    Assert.assertNotNull(orgsResource);
    Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING + 2, orgsResource.getOrganizationResources().size());
    Assert.assertNotNull(sitelUsersResource);
    Assert.assertEquals(USER_NUM_AT_BEGINNING, sitelUsersResource.getUserResources().size());
    Assert.assertEquals(SITEL_ADMIN_USER_NAME, userResources.get(0).getUser().getUser().getUsername());
    Assert.assertEquals(SITEL_ADMIN_USER_PASSWORD, userResources.get(0).getUser().getUser().getPassword());
    sitelPrivsResources = sitelOrgResource.getPrivilegesResource();
    Assert.assertNotNull(sitelPrivsResources);
    Assert.assertEquals(ORG_PRIVILEGES_NUM_AT_BEGINNING, sitelPrivsResources.getPrivilegeResources().size());
  }

  @Test
  public void doTestCreateUser() {
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
    sitelUserResource = userResource;
    Assert.assertEquals(SITEL_ORG_USER_USERNAME, userResource.getUser().getUser().getUsername());
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
    }
    catch (Exception e) {
      Assert.fail("Exception due to failure of creating an userperson");
    }
    Assert.assertEquals("russel", userResource.getUser().getUser().getUsername());
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
  public void doTestUpdateUser() {
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
        }
        catch (Exception e) {
          Assert.fail("Exception due to failure of updating particular user information");
        }
        try {
          userPerson = userIterResource.getUser();
        }
        catch (Exception e) {
          Assert.fail("Exception due to not getting the user");
        }
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
  public void doTestCreatePrivilegesofOrganization() {
    try {
      sitelPrivsResources = sitelOrgResource.getPrivilegesResource();
      Assert.assertNotNull(sitelPrivsResources);
      Assert.assertEquals(ORG_PRIVILEGES_NUM_AT_BEGINNING + 2, sitelPrivsResources.getPrivilegeResources().size());
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
    privilege.setName("Organization-admin-user-privilege-test-1");
    privilege.setPermissionMask(PRIVILEGE_PERMISSION_MASK);
    privilege.setShortDescription("This admin privilege contains the authority to do any of the CRUD options");
    privilege.setSecuredObject(securedObjectApi);
    com.smartitengineering.user.client.api.PrivilegeResource orgPrivilegesResource = null;
    try {
      orgPrivilegesResource = sitelPrivsResources.create(
          privilege);
    }
    catch (Exception e) {
      Assert.fail("Exception due to failure of creating organization privileges");
    }
    try {
      sitelPrivsResources.get();
      Assert.assertEquals(ORG_PRIVILEGES_NUM_AT_BEGINNING + 3, sitelPrivsResources.getPrivilegeResources().size());
      Assert.assertNotNull(orgPrivilegesResource);
      Assert.assertEquals("Smart User Adminstration", privilege.getDisplayName());
    }
    catch (Exception e) {
      Assert.fail("Exception because number of expected privileges didn't match with the actual");
    }
  }

  @Test
  public void doTestAddPrivilegesToUser() {
    sitelPrivsResources = sitelOrgResource.getPrivilegesResource();
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
    privilegeUser.setName("organization-admin-user-privilege-test-2");
    privilegeUser.setPermissionMask(PRIVILEGE_PERMISSION_MASK);
    privilegeUser.setShortDescription(
        "This privilege contains the authority to change the password and profile of the organization admin.");
    privilegeUser.setSecuredObject(securedObjectUserApi);
    PrivilegeResource privilegeResource = null;
    try {
      privilegeResource = sitelPrivsResources.create(privilegeUser);
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
//    Assert.assertEquals(USER_PRIVILEGES_NUM_AT_BEGINNING + 1, sitelUserPrivsResource.getUserPrivilegeResources().size());
    Assert.assertEquals("Admin User Profile Privilege", privilegeUser.getDisplayName());
  }

//  @Test
//  public void doTestCreateUserGroup() {
//    sitelUserGroupsResource = sitelOrgResource.getUserGroupsResource();
//    Assert.assertNotNull(sitelUserGroupsResource);
//    UserGroupResource userGroupResource = null;
//    UserGroup userGroup = new com.smartitengineering.user.client.impl.domain.UserGroup();
//    userGroup.setName(SITEL_USER_GROUP_NAME);
//    try {
//      userGroupResource = sitelUserGroupsResource.create(userGroup);
//    }
//    catch (Exception e) {
//      Assert.fail("Exception due to failure of creation user group");
//    }
//    sitelUserGroupResource = userGroupResource;
//    sitelUserGroupsResource.get();
//    Assert.assertNotNull(sitelUserGroupsResource);
//    Assert.assertEquals(NUM_OF_USER_GROUPS_AT_BEGINNING, sitelUserGroupsResource.getUserGroupResources().size());
//    Assert.assertEquals(SITEL_USER_GROUP_NAME, sitelUserGroupResource.getUserGroup().getName());
//  }
//
//  @Test
//  public void doTestAddUsersToUserGroup() {
//    sitelUserGroupUsersResource = sitelUserGroupResource.getUserGroupUsersResource();
//    Assert.assertEquals(ZERO, sitelUserGroupUsersResource.getUserGroupUserResources().size());
////    sitelUserGroupUserResource = sitelUserGroupUsersResource.getUserGroupUserResources();
//    UsersResource usersResource = sitelOrgResource.getUsersResource();
//    List<UserResource> userResources = usersResource.getUserResources();
//
//    for (UserGroupResource userGroupResource : sitelUserGroupsResource.getUserGroupResources()) {
//      if (userGroupResource.getUserGroup().getName().equals("SITEL_USER_GROUP_NAME")) {
//        for (UserResource user : userResources) {
//          if (user.getUser().getUser().getUsername().equals("modhu")) {
//            sitelUserGroupUsersResource.add(user.getUser().getUser());
//          }
//        }
//      }
//    }
//    Assert.assertEquals(1, sitelUserGroupUsersResource.getUserGroupUserResources().size());
//  }
  @Test
  public void doTestRemoveUserPrivilege() {
    sitelUserPrivsResource = sitelUserResource.getPrivilegesResource();
    List<UserPrivilegeResource> userPrivilegeResources = sitelUserPrivsResource.getUserPrivilegeResources();
    for (UserPrivilegeResource userPrivilegeResource : userPrivilegeResources) {
      if (userPrivilegeResource.getPrivilegeResource().getPrivilege().getName().equals(
          "organization-admin-user-privilege-test-2")) {
        try {
          userPrivilegeResource.delete();
        }
        catch (Exception e) {
          Assert.fail("Exception due to failure of deleting the user privilege resource");
        }
      }
    }
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
  public void doTestDeleteOrganization() {
    Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING + 2, orgsResource.getOrganizationResources().size());
    Assert.assertNotNull(sitelUsersResource);
    Assert.assertEquals(USER_NUM_AT_BEGINNING + 2, sitelUsersResource.getUserResources().size());
    Assert.assertNotNull(sitelPrivsResources);
    Assert.assertEquals(ORG_PRIVILEGES_NUM_AT_BEGINNING + 3, sitelPrivsResources.getPrivilegeResources().size());
    try {
      sitelUsersResource = sitelOrgResource.getUsersResource();
      sitelPrivsResources = sitelOrgResource.getPrivilegesResource();
      sitelUserPrivsResource = sitelUserResource.getPrivilegesResource();
    }
    catch (Exception e) {
      Assert.fail("Exception due to failure to get userresource and privilegesresource");
    }
    for (OrganizationResource orgIterResource : orgsResource.getOrganizationResources()) {
      if (orgIterResource.getOrganization().getUniqueShortName().equals("SITEL")) {
        try {
          orgIterResource.delete();
        }
        catch (Exception e) {
          Assert.fail("Exception due to failure of deleting the organization");
        }
      }
    }
  }

  @Test
  public void doTestUsersAndPrivsAfterRemovingOrg() {
    orgsResource.get();
    Assert.assertEquals(ORGANIZATION_NUM_AT_BEGINNING + 1, orgsResource.getOrganizationResources().size());
    try {
      sitelUsersResource.get();
      Assert.fail("Should have thrown exception");
    }
    catch (UniformInterfaceException e) {
      Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), e.getResponse().getStatus());
    }
    catch (Exception e) {
      Assert.fail("Should have thrown UniformInterface exception");
    }
    try {
      sitelPrivsResources.get();
      Assert.fail("Exception Should be thrown");
    }
    catch (UniformInterfaceException e) {
      Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), e.getResponse().getStatus());
    }
    catch (Exception e) {
      Assert.fail("Should have thrown UniformInterface exception");
    }

    try {
      sitelUserPrivsResource.get();
      Assert.fail("Should have thrown exception");
    }
    catch (UniformInterfaceException e) {
      Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), e.getResponse().getStatus());
    }
    catch (Exception e) {
      Assert.fail("Should have thrown UniformInterface exception");
    }
  }
  //Test Ended by Uzzal 
}
