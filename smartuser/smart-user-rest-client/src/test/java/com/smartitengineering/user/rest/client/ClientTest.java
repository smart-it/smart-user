/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.domain.Name;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.UniqueConstrainedField;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.filter.PersonFilter;
import com.smartitengineering.user.rest.client.exception.SmartException;
import com.smartitengineering.user.service.ExceptionMessage;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.service.UserServiceFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.UriBuilder;
import junit.framework.TestCase;
import org.glassfish.embed.GlassFish;
import org.glassfish.embed.ScatteredWar;

/**
 *
 * @author modhu7
 */
public class ClientTest extends TestCase {

    private Properties properties = new Properties();
    private static String connectionUri;
    private static String connectionPort;
    private static String warname;

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
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("testConfiguration.properties"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        connectionUri = properties.getProperty("uri");
        connectionPort = properties.getProperty("port");
        warname = properties.getProperty("warname");
        System.out.println(connectionUri);
        System.out.println(connectionPort);
        System.out.println(warname);

    }

    @Override
    protected void setUp()
            throws Exception {
        super.setUp();
        // Start Glassfish
        String server = properties.getProperty("server");
        if (server.equalsIgnoreCase("glassfish")) {
            glassfish = new GlassFish(getBaseURI().getPort());
            // Deploy Glassfish referencing the web.xml
            ScatteredWar war = new ScatteredWar(getBaseURI().getRawPath(),
                    new File("./src/test/webapp"),
                    new File("./src/test/webapp/WEB-INF/web.xml"),
                    Collections.singleton(new File("target/classes").toURI().toURL()));
            System.out.println(war.name);

            glassfish.deploy(war);
        }
    }

    @Override
    protected void tearDown()
            throws Exception {
        super.tearDown();

        String server = properties.getProperty("server");
        if (server.equalsIgnoreCase("glassfish")) {
            glassfish.stop();
        }
    }

    public void testResources() {
        doTestPersonService();
        doTestPrivilegeService();
        doTestRoleService();
        doTestUserPersonService();
        doTestServiceAggregator();
    }

    private void doTestGetUserPersonByUserName() {
        UserService userService = WebServiceClientFactory.getUserService();
        UserPerson userPerson = userService.getUserPersonByUsername("modhu7");
        System.out.println(userPerson.getPerson().getPrimaryEmail());
        System.out.println(userPerson.getPerson().getSelf().getName().getFirstName());
        System.out.println(userPerson.getUser().getRoles().size());
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

    private void doTestUpdateUserPerson() {
        UserService userService = WebServiceClientFactory.getUserService();
    //UserPerson userPerson = userService

    }

    private void doTestUserPersonService() {
        doTestCreateUserPerson();
        doTestReadUserPerson();
        doTestGetUserPersonByUserName();
        doTestUpdateUserPerson();
    }

    private void doTestCreateUserPerson() {
        UserService userService = WebServiceClientFactory.getUserService();
        PersonService personService = WebServiceClientFactory.getPersonService();
        Set<Role> roles = new HashSet<Role>();
        roles.add(userService.getRoleByName("Role-5"));
        roles.add(userService.getRoleByName("Role-9"));
        UserPerson userPerson = new UserPerson();
        userPerson.setPerson(personService.getPersonByEmail("email-1@email.com"));
        userPerson.getUser().setUsername("modhu7");
        userPerson.getUser().setPassword("password");
        userPerson.getUser().setRoles(roles);
        userService.create(userPerson);

        userPerson.setPerson(personService.getPersonByEmail("email-2@email.com"));
        userPerson.getUser().setUsername("imyousuf");
        userPerson.getUser().setPassword("password");
        userPerson.getUser().setRoles(roles);
        userService.create(userPerson);

        userPerson.setPerson(personService.getPersonByEmail("email-2@email.com"));
        userPerson.getUser().setUsername("ahmyousuf");
        userPerson.getUser().setPassword("password");
        userPerson.getUser().setRoles(roles);
//        try {
//            userService.create(userPerson);
//            fail("Should not be succeed to add more than one user with same person");
//        } catch (Exception e) {
//        }
    }

    private void doTestCreatePerson() {
        PersonService personService = WebServiceClientFactory.getPersonService();
        Person person = new Person();
        for (int i = 0; i < 10; i++) {            
            person.getFather().getName().setFirstName("FFN" + i);
            person.getFather().getName().setLastName("FLN" + i);
            person.getFather().getName().setMiddleInitial("FM" + i);
            person.getFather().setNationalID("F123456789-" + i);

            person.getMother().getName().setFirstName("MFN" + i);
            person.getMother().getName().setLastName("MLN" + i);
            person.getMother().getName().setMiddleInitial("MM" + i);
            person.getMother().setNationalID("M123456789-" + i);

            person.getSpouse().getName().setFirstName("SFN" + i);
            person.getSpouse().getName().setLastName("SLN" + i);
            person.getSpouse().getName().setMiddleInitial("SM" + i);
            person.getSpouse().setNationalID("S123456789-" + i);

            person.getSelf().getName().setFirstName("PersonFN-" + i);
            person.getSelf().getName().setLastName("PersonLN-" + i);
            person.getSelf().getName().setMiddleInitial("M-" + i);
            person.getSelf().setNationalID("P123456789-" + i);


            person.getAddress().setCity("Dhaka-" + i);
            person.getAddress().setCountry("Bangladesh-" + i);
            person.setBirthDay(new Date(System.currentTimeMillis() - new Long("788400000000") + i * 86400000));
            person.setCellPhoneNumber("01712345678-" + i);
            person.setFaxNumber("+8801254876932" + i);
            person.setPhoneNumber("+880123654789" + i);
            person.setPrimaryEmail("email-" + i + "@email.com");
            person.setSecondaryEmail("sec-email-" + i + "@email.com");
            personService.create(person);           
        }
        try {
            personService.create(person);
            fail("Should have failed!");
        }
        catch(SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(ex.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION, exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(), UniqueConstrainedField.PERSON_EMAIL.name());
        }
        try {
            person.setPrimaryEmail("another_"+person.getPrimaryEmail());
            personService.create(person);
            fail("Should have failed!");
        }
        catch(SmartException ex) {
            ExceptionMessage exception = ExceptionMessage.valueOf(ex.getMessage());
            assertEquals(ExceptionMessage.CONSTRAINT_VIOLATION_EXCEPTION, exception);
            assertEquals(ex.getExceptionElement().getFieldCausedBy(), UniqueConstrainedField.PERSON_NATIONAL_ID.name());
        }
        catch(Exception ex) {
            fail("Unexpected exception: " + ex.getMessage());
        }
    }

    private void doTestCreatePrivilege() {
        UserService userService = WebServiceClientFactory.getUserService();
        for (int i = 0; i < 20; i++) {
            Privilege privilege = new Privilege();
            privilege.setDisplayName("Display Privilege-" + i);
            privilege.setName("Privilege-" + i);
            privilege.setShortDescription("No Description");
            userService.create(privilege);
        }
    }

    private void doTestCreateRole() {
        UserService userService = WebServiceClientFactory.getUserService();
        for (int i = 0; i < 10; i++) {
            Role role = new Role();
            role.setDisplayName("Display Role-" + i);
            role.setName("Role-" + i);
            role.setShortDescription("No Description");
            Set<Privilege> privileges = new HashSet<Privilege>();

            privileges.add(userService.getPrivilegeByName("Privilege-" + (2 * i)));
            privileges.add(userService.getPrivilegeByName("Privilege-" + (2 * i + 1)));
            role.setPrivileges(privileges);
            userService.create(role);
        }
    }

    private void doTestGetPersonByEmail() {
        PersonService personService = WebServiceClientFactory.getPersonService();
        Person person = personService.getPersonByEmail("email-6@email.com");
        assertNotNull(person);
        System.out.println(person.getPrimaryEmail());
        System.out.println(person.getSelf().getName().getFirstName());
    }

    private void doTestReadPerson() {
        PersonService personService = WebServiceClientFactory.getPersonService();
        List<Person> listPerson = new ArrayList<Person>(personService.getAllPerson());
        System.out.println(listPerson.size());
        for (Person person : listPerson) {
            System.out.println(person.getBirthDay());
        }
        assertTrue(listPerson.size() == 10);

    }

    private void doTestReadUserPerson() {
        UserService userService = WebServiceClientFactory.getUserService();
        List<UserPerson> list = new ArrayList<UserPerson>(userService.getAllUserPerson());
        System.out.println(list.size());
        for (UserPerson userPerson : list) {
            System.out.println(userPerson.getUser().getUsername());
        }

    }

    private void doTestReadPrivilege() {
        UserService userService = WebServiceClientFactory.getUserService();
        Privilege privilege = userService.getPrivilegeByName("Privilege-5");
        assertNotNull(privilege);
        System.out.println(privilege.getDisplayName());

        privilege = userService.getPrivilegeByName("Privilege-1");
        assertNotNull(privilege);
        assertTrue(privilege.getName().equals("Privilege-1"));
        System.out.println(privilege.getDisplayName());

        privilege = userService.getPrivilegeByName("Privilege-2");
        assertNotNull(privilege);
        assertTrue(privilege.getName().equals("Privilege-2"));
        System.out.println(privilege.getDisplayName());

        privilege = userService.getPrivilegeByName("Privilege-3");
        assertNotNull(privilege);
        assertTrue(privilege.getName().equals("Privilege-3"));
        System.out.println(privilege.getDisplayName());

        privilege = userService.getPrivilegeByName("Privilege-17");
        assertNotNull(privilege);
        assertTrue(privilege.getName().equals("Privilege-17"));
        System.out.println(privilege.getDisplayName());

        privilege = userService.getPrivilegeByName("Privilege-0");
        assertNotNull(privilege);
        assertTrue(privilege.getName().equals("Privilege-0"));
        System.out.println(privilege.getDisplayName());
    }

    private void doTestReadRole() {
        UserService userService = WebServiceClientFactory.getUserService();
        Role role = userService.getRoleByName("Role-1");
        assertNotNull(role);
        assertTrue(role.getName().equals("Role-1"));

        role = userService.getRoleByName("Role-0");
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
        List<Person> listPerson = new ArrayList<Person>(personService.search(personFilter));
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
        person.getSelf().getName().setFirstName(person.getSelf().getName().getFirstName() + " updated");
        personService.update(person);

        listPerson = new ArrayList<Person>(personService.getAllPerson());
        System.out.println(listPerson.size());
        for (Person personR : listPerson) {
            System.out.println(personR.getSelf().getName().getFirstName());
        }
        assertTrue(listPerson.size() == 10);
    }

    private void doTestUpdatePrivilege() {
        UserService userService = WebServiceClientFactory.getUserService();
        Privilege privilege = userService.getPrivilegeByName("Privilege-6");
        privilege.setDisplayName(privilege.getDisplayName() + "-updated");
        userService.update(privilege);
        privilege = userService.getPrivilegeByName("Privilege-6");
        assertNotNull(privilege);
        System.out.println("Display Name: " + privilege.getDisplayName());
    }

    private void doTestUpdateRole() {
        UserService userService = WebServiceClientFactory.getUserService();
        Role role = userService.getRoleByName("Role-4");
        role.getPrivileges().add(userService.getPrivilegeByName("Privilege-18"));
        userService.update(role);
        role = userService.getRoleByName("Role-4");
        System.out.println(role.getPrivileges().size());
    }
}
