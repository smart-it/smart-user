/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.domain.BasicIdentity;
import com.smartitengineering.user.domain.Name;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.filter.PersonFilter;
import com.smartitengineering.user.filter.UserFilter;
import com.smartitengineering.user.filter.UserPersonFilter;
import com.smartitengineering.user.rest.client.exception.SmartException;
import com.smartitengineering.user.rest.client.login.LoginCenter;
import com.smartitengineering.user.rest.client.login.LoginService;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.service.UserPersonService;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.service.UserServiceFactory;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.ws.rs.core.UriBuilder;
import junit.framework.TestCase;
import org.glassfish.embed.GlassFish;
import org.glassfish.embed.ScatteredWar;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author modhu7
 */
public class ClientTest extends TestCase {

    private Properties properties = new Properties();
    private static String connectionUri;
    private static String connectionPort;
    private static String warname;
    private static String server;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    private static int getPort(int defaultPort) {
        String port = System.getenv("JERSEY_HTTP_PORT");
        if (null != port) {
            try {
                return Integer.parseInt(port);
            } catch (NumberFormatException e) {
            }
        }
        return defaultPort;
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri(connectionUri).port(getPort(new Integer(
                connectionPort))).path(warname).build();
    }
    private GlassFish glassfish;

    public ClientTest(String testName) throws IOException {
        super(testName);
//        try {
//            properties.load(getClass().getClassLoader().getResourceAsStream(
//                    "testConfiguration.properties"));
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null,
//                    ex);
//        }

        new ClassPathXmlApplicationContext(
                "config-context.xml");
        final TestConnectionConfig testConnectionConfig =
                TestConfigFactory.getInstance().getTestConnectionConfig();


//        connectionUri = properties.getProperty("uri");
//        connectionPort = properties.getProperty("port");
//        warname = properties.getProperty("warname");
        connectionUri = testConnectionConfig.getBasicUrl();
        Integer port = testConnectionConfig.getPort();
        connectionPort = port.toString();
        warname = testConnectionConfig.getContextPath();
        server = testConnectionConfig.getServer();
    }

    @Override
    protected void setUp()
            throws Exception {
        super.setUp();
        // Start Glassfish        
        if (server.equalsIgnoreCase("glassfish")) {
            glassfish = new GlassFish(getBaseURI().getPort());
            // Deploy Glassfish referencing the web.xml
            ScatteredWar war = new ScatteredWar(getBaseURI().getRawPath(),
                    new File("../smart-user-server/src/main/webapp"),
                    new File("../smart-user-server/src/main/webapp/WEB-INF/web.xml"),
                    Collections.singleton(new File("target/classes").toURI().
                    toURL()));
            System.out.println(war.name);

            glassfish.deploy(war);
        }
    }

    @Override
    protected void tearDown()
            throws Exception {
        super.tearDown();

        //String server = properties.getProperty("server");
        if (server.equalsIgnoreCase("glassfish")) {
            glassfish.stop();
        }
    }

    public void testResources() {
        doTestLogin();
        LoginService.login("superadmin", "superadmin");        
        doTestPersonService();
        doTestPrivilegeService();
        doTestRoleService();
        doTestUserPersonService();
        doTestUserService();
        doTestDeleteAll();
        doTestServiceAggregator();
    }

    private void doTestDeleteAll() {
        PersonService personService = WebServiceClientFactory.getPersonService();
        UserService userService = WebServiceClientFactory.getUserService();
        UserPersonService userPersonService = WebServiceClientFactory.getUserPersonService();
        PrivilegeService privilegeService = WebServiceClientFactory.getPrivilegeService();
        RoleService roleService = WebServiceClientFactory.getRoleService();

        userPersonService.delete(userPersonService.getUserPersonByUsername(
                "modhu7"));
        userPersonService.delete(userPersonService.getUserPersonByUsername(
                "imran"));

        Set<User> users = new HashSet<User>(userService.getAllUser());

        //Delete all user except superadmin

        for (User user : users) {
            if(!user.getUsername().equals("superadmin"))
                userService.delete(user);
        }

        //Delete all person Super Administrator
        Set<Person> persons = new HashSet<Person>(personService.getAllPerson());
        for (Person person : persons) {
            if(person.getSelf().getName().getFirstName().equals("Super"))
                personService.delete(person);
        }

        //Delete all test created roles
        Set<Role> roles = new HashSet<Role>(roleService.getRolesByName("Role-"));
        for (Role role : roles) {            
            roleService.delete(role);
        }


        Set<Privilege> privileges = new HashSet<Privilege>(privilegeService.getPrivilegesByName("P"));
        for (Privilege privilege : privileges) {
            privilegeService.delete(privilege);
        }
    }

    private void doTestGetUserPersonByUserName() {
        UserService userService = WebServiceClientFactory.getUserService();
        UserPersonService userPersonService = WebServiceClientFactory.getUserPersonService();
        UserPerson userPerson = userPersonService.getUserPersonByUsername(
                "modhu7");
        System.out.println(userPerson.getPerson().getPrimaryEmail());
        System.out.println(userPerson.getPerson().getSelf().getName().
                getFirstName());
        System.out.println(userPerson.getUser().getRoles().size());
    }

    private void doTestLogin() {
        //wrong password
        //should not be able to login
        try {
            LoginService.login("modhu", "xxx");
            fail("Should be failed");
        } catch (SmartException ex) {
            assertTrue(ex.getStatus() == 401);
        }

        //imyousuf is a valid user
        //imyousuf should get 404
        // imyousuf should be able to login
        try {
            LoginService.login("imyousuf", "imyousuf");
            assertTrue(LoginCenter.getUsername().equals("imyousuf"));
            assertTrue(LoginCenter.getPassword().equals("imyousuf"));
        } catch (SmartException ex) {
        }
        try {
            LoginService.login("modhu", "modhu");
            assertTrue(LoginCenter.getUsername().equals("modhu"));
            assertTrue(LoginCenter.getPassword().equals("modhu"));
        } catch (SmartException ex) {
            fail("should not throw exception");
        }
    }

    private void doTestPersonService() {
        doTestCreatePerson();
        doTestReadPerson();
        doTestGetPersonByEmail();
        doTestSearchPerson();
        doTestUpdatePerson();
    }

    private void doTestPrivilegeService() {
        doTestCreatePrivilege();
        doTestReadPrivilege();
        doTestUpdatePrivilege();
    }

    private void doTestRoleService() {
        doTestCreateRole();
        doTestReadRole();
        doTestUpdateRole();
    }

    private void doTestSearchUser() {
        UserService userService = WebServiceClientFactory.getUserService();
        UserFilter userFilter = new UserFilter();
        userFilter.setUsername("modhu7");
        Set<User> setUser = new HashSet<User>(userService.search(userFilter));
        assertEquals(1, setUser.size());//this search returns unique results in set

    }

    private void doTestSearchUserPerson() {
        UserService userService = WebServiceClientFactory.getUserService();
        UserPersonFilter userPersonFilter = new UserPersonFilter();
        userPersonFilter.setUsername("modhu7");
        UserPersonService userPersonService = WebServiceClientFactory.getUserPersonService();
        Set<UserPerson> setUser = new HashSet<UserPerson>(userPersonService.search(userPersonFilter));
        assertEquals(1, setUser.size());//this search returns unique results in set

    }

    private void doTestUserService() {
        doTestReadUser();
        doTestSearchUser();
        doTestUpdateUser();
    }

    private void doTestUpdateUserPerson() {
        UserPersonService userPersonService = WebServiceClientFactory.getUserPersonService();
        UserService userService = WebServiceClientFactory.getUserService();
        UserPerson userPerson = userPersonService.getUserPersonByUsername(
                "modhu7");
        userPerson.getUser().setPassword("new" + userPerson.getUser().
                getPassword());
        userPersonService.update(userPerson);

        try {
            userPersonService.update(userPerson);
        } catch (SmartException e) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    e.getMessage());
            assertEquals(ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION,
                    exception);
            assertEquals(e.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.OTHER.name());
        } catch (Exception e) {
            fail("Should have failed!");
        }
        userPerson.getUser().setUsername("imran");
        try {
            userPersonService.update(userPerson);
            fail("Should have failed");
        } catch (SmartException e) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    e.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(e.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.USER_USERNAME.name());
        } catch (Exception e) {
            fail("Should have failed!");
        }
    }

    private void doTestUserPersonService() {
        doTestCreateUserPerson();
        doTestReadUserPerson();
        doTestSearchUserPerson();
        doTestGetUserPersonByUserName();
        doTestUpdateUserPerson();
    }

    private void doTestCreateUserPerson() {
        UserService userService = WebServiceClientFactory.getUserService();
        PersonService personService = WebServiceClientFactory.getPersonService();
        UserPersonService userPersonService = WebServiceClientFactory.getUserPersonService();
        RoleService roleService = WebServiceClientFactory.getRoleService();
        Set<Role> roles = new HashSet<Role>();
        roles.add(roleService.getRoleByName("Role-5"));
        roles.add(roleService.getRoleByName("Role-9"));
        UserPerson userPerson = new UserPerson();
        userPerson.setPerson(personService.getPersonByEmail("email-1@email.com"));
        userPerson.getUser().setUsername("modhu7");
        userPerson.getUser().setPassword("password");
        userPerson.getUser().setRoles(roles);
        userPersonService.create(userPerson);

        userPerson.setPerson(personService.getPersonByEmail("email-2@email.com"));
        userPerson.getUser().setUsername("imran");
        userPerson.getUser().setPassword("password");
        userPerson.getUser().setRoles(roles);
        userPersonService.create(userPerson);

        userPerson.setPerson(personService.getPersonByEmail("email-2@email.com"));
        userPerson.getUser().setUsername("ahmyousuf");
        userPerson.getUser().setPassword("password");
        userPerson.getUser().setRoles(roles);

        try {
            userPersonService.create(userPerson);
            fail("Should have failed!");
        } catch (SmartException e) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    e.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(e.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.PERSON.name());
        } catch (Exception e) {
            fail("Should have failed!");
        }

        userPerson.setPerson(personService.getPersonByEmail("email-3@email.com"));
        userPerson.getUser().setUsername("imran");
        userPerson.getUser().setPassword("password");
        userPerson.getUser().setRoles(roles);

        try {
            userPersonService.create(userPerson);
            fail("Should not be succeed to add more than one user with same person");
        } catch (SmartException e) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    e.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(e.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.USER_USERNAME.name());
        }

        userPerson.setPerson(personService.getPersonByEmail("email-4@email.com"));
        userPerson.getPerson().setId(null);
        userPerson.getUser().setUsername("username-1");
        userPerson.getUser().setPassword("password");
        userPerson.getUser().setRoles(roles);
        try {
            userPersonService.create(userPerson);
            fail("Should not be succeed to add more than one user with same person");
        } catch (SmartException e) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    e.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(e.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.PERSON_EMAIL.name());
        }

        userPerson.getPerson().setPrimaryEmail("another-" + userPerson.getPerson().
                getPrimaryEmail());
        try {
            userPersonService.create(userPerson);
            fail("Should not be succeed to add more than one user with same person");
        } catch (SmartException e) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    e.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(e.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.PERSON_NATIONAL_ID.name());
        }

        userPerson.setPerson(personService.getPersonByEmail("email-5@email.com"));
        userPerson.getUser().setUsername("ahmyousuf");
        userPerson.getUser().setPassword("password");
        userPerson.getUser().setRoles(roles);
        userPersonService.create(userPerson);
    }

    private void doTestCreatePerson() {
        PersonService personService = WebServiceClientFactory.getPersonService();
        Person person = new Person();
        for (int i = 0; i < 10; i++) {
            Name fatherName = new Name();
            BasicIdentity father = new BasicIdentity();
            fatherName.setFirstName("FFN" + i);
            fatherName.setLastName("FLN" + i);
            fatherName.setMiddleInitial("FM" + i);
            father.setName(fatherName);
            father.setNationalID("F123456789-" + i);
            person.setFather(father);

            Name motherName = new Name();
            BasicIdentity mother = new BasicIdentity();
            motherName.setFirstName("MFN" + i);
            motherName.setLastName("MLN" + i);
            motherName.setMiddleInitial("MM" + i);
            mother.setName(motherName);
            mother.setNationalID("M123456789-" + i);
            person.setMother(mother);

            Name spouseName = new Name();
            BasicIdentity spouse = new BasicIdentity();
            spouseName.setFirstName("SFN" + i);
            spouseName.setLastName("SLN" + i);
            spouseName.setMiddleInitial("SM" + i);
            spouse.setName(spouseName);
            spouse.setNationalID("S123456789-" + i);
            person.setSpouse(spouse);

            person.getSelf().getName().setFirstName("PersonFN-" + i);
            person.getSelf().getName().setLastName("PersonLN-" + i);
            person.getSelf().getName().setMiddleInitial("M-" + i);
            person.getSelf().setNationalID("P123456789-" + i);


            person.getAddress().setCity("Dhaka-" + i);
            person.getAddress().setCountry("Bangladesh-" + i);
            person.setBirthDay(new Date(System.currentTimeMillis() - new Long(
                    "788400000000") + i * 86400000));
            person.setCellPhoneNumber("01712345678-" + i);
            person.setFaxNumber("+8801254876932" + i);
            person.setPhoneNumber("+880123654789" + i);
            person.setPrimaryEmail("email-" + i + "@email.com");
            person.setSecondaryEmail("sec-email-" + i + "@email.com");

            try {
                personService.create(person);
            }
            catch (SmartException e) {
                fail(e.getMessage() + " " + e.getStatus());
            }
            catch (Exception e) {
                fail(e.getMessage());
            }
        }
        try {
            personService.create(person);
            fail("Should have failed!");
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.PERSON_EMAIL.name());
        }
        try {
            person.setPrimaryEmail("another_" + person.getPrimaryEmail());
            personService.create(person);
            fail("Should have failed!");
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.PERSON_NATIONAL_ID.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
        try {
            person.getSelf().setNationalID("another_" + person.getSelf().
                    getNationalID());
            personService.create(person);
            fail("Should have failed!");
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.PERSON_SPOUSE_NATIONAL_ID.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
        try {
            person.getSpouse().setNationalID("another_" + person.getSpouse().
                    getNationalID());
            personService.create(person);
            fail("Should have failed!");
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.PERSON_FATHER_NATIONAL_ID.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
        try {
            person.getFather().setNationalID("another_" + person.getFather().
                    getNationalID());
            personService.create(person);
            fail("Should have failed!");
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.PERSON_MOTHER_NATIONAL_ID.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }

    }

    private void doTestCreatePrivilege() {
        PrivilegeService privilegeService = WebServiceClientFactory.getPrivilegeService();
        Privilege privilege = new Privilege();
        for (int i = 0; i < 20; i++) {
            privilege.setDisplayName("Display Privilege-" + i);
            privilege.setName("Privilege-" + i);
            privilege.setShortDescription("No Description");
            privilegeService.create(privilege);
        }
        try {
            privilegeService.create(privilege);
            fail("Should have failed!");
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.PRIVILEGE_NAME.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    private void doTestCreateRole() {
        PrivilegeService privilegeService = WebServiceClientFactory.getPrivilegeService();
        RoleService roleService = WebServiceClientFactory.getRoleService();
        Role role = new Role();
        for (int i = 0; i < 10; i++) {
            role.setDisplayName("Display Role-" + i);
            role.setName("Role-" + i);
            role.setShortDescription("No Description");
            Set<Privilege> privileges = new HashSet<Privilege>();

            privileges.add(
                    privilegeService.getPrivilegeByName("Privilege-" + (2 * i)));
            privileges.add(privilegeService.getPrivilegeByName("Privilege-" +
                    (2 * i + 1)));
            role.setPrivileges(privileges);
            roleService.create(role);
        }
        try {
            roleService.create(role);
            fail("Should have failed!");
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.ROLE_NAME.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    private void doTestGetPersonByEmail() {
        PersonService personService = WebServiceClientFactory.getPersonService();
        Person person = personService.getPersonByEmail("email-6@email.com");
        assertNotNull(person);
        System.out.println(person.getPrimaryEmail());
        System.out.println(person.getSelf().getName().getFirstName());
        assertEquals("email-6@email.com", person.getPrimaryEmail());
    }

    private void doTestReadPerson() {
        PersonService personService = WebServiceClientFactory.getPersonService();
        List<Person> listPerson = new ArrayList<Person>(personService.getAllPerson());
        for (Person person : listPerson) {
            System.out.println("Birthday : " + person.getBirthDay());
        }
        assertTrue(listPerson.size() == 13);

    }

    private void doTestReadUser() {
        UserService userService = WebServiceClientFactory.getUserService();
        List<User> listUser = new ArrayList<User>(userService.getAllUser());
        assertEquals(6, listUser.size());
    }

    private void doTestReadUserPerson() {
        UserPersonService userPersonService = WebServiceClientFactory.getUserPersonService();
        List<UserPerson> list = new ArrayList<UserPerson>(userPersonService.getAllUserPerson());
        System.out.println(list.size());
        for (UserPerson userPerson : list) {
            System.out.println(userPerson.getUser().getUsername());
        }

    }

    private void doTestReadPrivilege() {
        PrivilegeService privilegeService = WebServiceClientFactory.getPrivilegeService();

        Privilege privilege = privilegeService.getPrivilegeByName("Privilege-5");
        assertNotNull(privilege);
        System.out.println(privilege.getDisplayName());

        privilege = privilegeService.getPrivilegeByName("Privilege-1");
        assertNotNull(privilege);
        assertTrue(privilege.getName().equals("Privilege-1"));
        System.out.println(privilege.getDisplayName());

        privilege = privilegeService.getPrivilegeByName("Privilege-2");
        assertNotNull(privilege);
        assertTrue(privilege.getName().equals("Privilege-2"));
        System.out.println(privilege.getDisplayName());

        privilege = privilegeService.getPrivilegeByName("Privilege-3");
        assertNotNull(privilege);
        assertTrue(privilege.getName().equals("Privilege-3"));
        System.out.println(privilege.getDisplayName());

        privilege = privilegeService.getPrivilegeByName("Privilege-17");
        assertNotNull(privilege);
        assertTrue(privilege.getName().equals("Privilege-17"));
        System.out.println(privilege.getDisplayName());

        privilege = privilegeService.getPrivilegeByName("Privilege-0");
        assertNotNull(privilege);
        assertTrue(privilege.getName().equals("Privilege-0"));
        System.out.println(privilege.getDisplayName());
    }

    private void doTestReadRole() {
        RoleService roleService = WebServiceClientFactory.getRoleService();
        Role role = roleService.getRoleByName("Role-1");
        assertNotNull(role);
        assertTrue(role.getName().equals("Role-1"));

        role = roleService.getRoleByName("Role-0");
        assertNotNull(role);
        assertTrue(role.getName().equals("Role-0"));

    }

    private void doTestSearchPerson() {
        PersonService personService = WebServiceClientFactory.getPersonService();
        PersonFilter personFilter = new PersonFilter();
        Name name = new Name();
        name.setFirstName("PersonFN");
        personFilter.setName(name);
        personFilter.setEmail("email-6@email.com");
        List<Person> listPerson = new ArrayList<Person>(personService.search(
                personFilter));
        for (Person person : listPerson) {
            System.out.println(person.getBirthDay());
        }
    }

    private void doTestServiceAggregator() {
        assertNotNull(UserServiceFactory.getInstance().getPersonService());
        assertNotNull(UserServiceFactory.getInstance().getUserService());
    }

    private void doTestUpdatePerson() {
        PersonService personService = WebServiceClientFactory.getPersonService();
        List<Person> listPerson = new ArrayList<Person>(personService.getAllPerson());
        Person person = new Person();
        person = listPerson.get(5);
        person.getSelf().getName().setFirstName(person.getSelf().getName().
                getFirstName() + " updated");
        personService.update(person);

        try {
            personService.update(person);
            fail("Should have failed");
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.OTHER.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
        listPerson = new ArrayList<Person>(personService.getAllPerson());

        System.out.println(listPerson.size());

        for (Person personR : listPerson) {
            System.out.println(personR.getSelf().getName().getFirstName());
        }
        assertTrue(listPerson.size() == 13);
        person = listPerson.get(6);

        person.setId(null);

        person.setPrimaryEmail("no-email");
        try {
            personService.update(person);
            fail("Should have failed!");
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.PERSON_NATIONAL_ID.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    private void doTestUpdatePrivilege() {
        PrivilegeService privilegeService = WebServiceClientFactory.getPrivilegeService();
        Privilege privilege = privilegeService.getPrivilegeByName("Privilege-6");
        privilege.setDisplayName(privilege.getDisplayName() + "-updated");
        privilegeService.update(privilege);

        try {
            privilegeService.update(privilege);
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.OTHER.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
        privilege =
                privilegeService.getPrivilegeByName("Privilege-6");
        assertNotNull(privilege);
        assertEquals("Display Privilege-6-updated", privilege.getDisplayName());

        privilege.setName("Privilege-5");
        try {
            privilegeService.update(privilege);
            fail("Should have failed!");
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.PRIVILEGE_NAME.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }

    }

    private void doTestUpdateRole() {
        RoleService roleService = WebServiceClientFactory.getRoleService();
        PrivilegeService privilegeService = WebServiceClientFactory.getPrivilegeService();
        Role role = roleService.getRoleByName("Role-4");
        role.getPrivileges().add(privilegeService.getPrivilegeByName(
                "Privilege-18"));
        roleService.update(role);

        try {
            roleService.update(role);
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.OTHER.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
        role = roleService.getRoleByName("Role-4");
        assertEquals(3, role.getPrivileges().size());
        role.setName("Role-3");
        try {
            roleService.update(role);
            fail("Should have failed!");
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.ROLE_NAME.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }

    }

    private void doTestUpdateUser() {
        UserService userService = WebServiceClientFactory.getUserService();
        User oldUser = new User();
        oldUser =
                userService.getUserByUsername("imran");
        User user = new User();
        user =
                userService.getUserByUsername("imran");
        user.setPassword("new" + user.getPassword());
        userService.update(user);
        user.setUsername("modhu7");
        try {
            userService.update(user);
            fail("Should have failed");
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.USER_USERNAME.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }

        try {
            userService.update(oldUser);
            fail("Should have failed");
        } catch (SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(
                    ex.getMessage());
            assertEquals(ExceptionMessage.STALE_OBJECT_STATE_EXCEPTION,
                    exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(),
                    UniqueConstrainedField.OTHER.name());
        } catch (Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }
}
