/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.domain.Address;
import com.smartitengineering.user.domain.BasicPerson;
import com.smartitengineering.user.domain.Name;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
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
import java.util.List;
import java.util.Properties;
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
        doTestCreate();
        doTestServiceAggregator();
    }

    private void doTestCreate() {
        System.out.println("test");
        UserService service = WebServiceClientFactory.getUserService();
        PersonService personService = WebServiceClientFactory.getPersonService();
        System.out.println("test");
        UserPerson userPerson = new UserPerson();
        User user = new User();
        Name fatherName = new Name();
        fatherName.setFirstName("A");
        fatherName.setLastName("Sen");
        fatherName.setMiddleInitial("B");
        BasicPerson father = new BasicPerson();
        father.setName(fatherName);
        father.setNationalID("123456789");

        Name motherName = new Name();
        motherName.setFirstName("D");
        motherName.setLastName("Sen");
        motherName.setMiddleInitial("K");
        BasicPerson mother = new BasicPerson();
        mother.setName(motherName);
        mother.setNationalID("123456788");

        Name spouseName = new Name();
        spouseName.setFirstName("S");
        spouseName.setLastName("Gupta");
        spouseName.setMiddleInitial("R");
        BasicPerson spouse = new BasicPerson();
        spouse.setName(spouseName);
        spouse.setNationalID("123456787");


        Name name = new Name();
        name.setFirstName("S");
        name.setLastName("Gupta");
        name.setMiddleInitial("S");

        Person person = new Person();
        person.setFather(father);
        person.setMother(mother);
        person.setSpouse(spouse);
        person.setName(name);
        person.setNationalID("0123456721");
        person.setFaxNumber("fax-0123456");
        Address address = new Address();
        address.setCity("Dhaka");
        address.setCountry("BD");
        address.setZip("1207");
        person.setAddress(new Address());
        person.setBirthDay(new Date(System.currentTimeMillis()));
        person.setCellPhoneNumber("0123");

        user.setUsername("modhu7");
        user.setPassword("password");

        userPerson.setPerson(person);

        userPerson.setUser(user);

        personService.create(person);

        service.create(userPerson);

        List<User> list = new ArrayList<User>(service.getAllUser());

        System.out.println(list.size());

        for (User user1 : list) {
            System.out.println(user1.getUsername());
            System.out.println(user1.getPassword());
            System.out.println(user1.getRoles());
        }

        List<Person> listPerson = new ArrayList<Person>(personService.getAllPerson());

        System.out.println(listPerson.size());

        for (Person person1 : listPerson) {
            System.out.println(person1.getCellPhoneNumber());

        }

    }

    private void doTestServiceAggregator() {
        assertNotNull(UserServiceFactory.getInstance().getPersonService());
        assertNotNull(UserServiceFactory.getInstance().getUserService());
    }
}
