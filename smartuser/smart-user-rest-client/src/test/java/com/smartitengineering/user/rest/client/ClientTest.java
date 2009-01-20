/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.rest.client;

import com.smartitengineering.user.domain.Address;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.service.UserServiceFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
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
public class ClientTest extends TestCase{

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
        if (server.equalsIgnoreCase("glassfish")){
            glassfish.stop();
        }
    }
    
    public void testResources(){
        doTestCreate();
        doTestServiceAggregator();
    }

    private void doTestCreate() {
        System.out.println("test");
        UserService service = WebServiceClientFactory.getUserService();
        System.out.println("test");
        UserPerson userPerson = new UserPerson();
        User user = new User();
        Person person = new Person();
        user.setUsername("modhu7");
        user.setPassword("password");
        person.setAddress(new Address());
        person.setBirthDay(new Date());
        person.setCellPhoneNumber("0123");        
        userPerson.setPerson(person);
        System.out.println("test");
        userPerson.setUser(user);
        
        service.create(userPerson);
        service.getUserByID(1);
    }

    private void doTestServiceAggregator() {
        assertNotNull(UserServiceFactory.getInstance().getPersonService());
        assertNotNull(UserServiceFactory.getInstance().getUserService());
    }
         
    
}
