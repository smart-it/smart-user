/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user;

import com.google.inject.AbstractModule;
import com.smartitengineering.clientrest.smartuserclientimpl.ComprehensiveClientTest;
import com.smartitengineering.user.guice.binder.Initializer;
import com.smartitengineering.util.bean.guice.GuiceUtil;
import com.smartitengineering.util.rest.client.ApplicationWideClientFactoryImpl;
import com.smartitengineering.util.rest.client.ConnectionConfig;
import java.io.File;
import java.util.Properties;
import junit.framework.Assert;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class ComprehensiveTest {

  private static final int PORT = 10090;
  public static final String DEFAULT_NS = "com.smartitengineering";
  public static final String ROOT_URI_STRING = "http://localhost:" + PORT + "/smartuser/";
  public static final String TEST = "test";
  public static final String TEST_NS = "testNS";
  private static final HBaseTestingUtility TEST_UTIL = new HBaseTestingUtility();
  private static final Logger LOGGER = LoggerFactory.getLogger(ComprehensiveTest.class);
  private static Server jettyServer;
  private final ComprehensiveClientTest comprehensiveClientTest = new ComprehensiveClientTest();

  @BeforeClass
  public static void globalSetup() throws Exception {
    /*
     * Start HBase and initialize tables
     */
    //-Djavax.xml.parsers.DocumentBuilderFactory=com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl
    System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                       "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
    try {
      TEST_UTIL.startMiniCluster();
    }
    catch (Exception ex) {
      LOGGER.error(ex.getMessage(), ex);
    }
    /*
     * Ensure DIs done
     */
    Properties properties = new Properties();
    properties.setProperty(GuiceUtil.CONTEXT_NAME_PROP,
                           "com.smartitengineering.dao.impl.hbase,com.smartitengineering.user.client");
    properties.setProperty(GuiceUtil.IGNORE_MISSING_DEP_PROP, Boolean.TRUE.toString());
    properties.setProperty(GuiceUtil.MODULES_LIST_PROP, ConfigurationModule.class.getName());
    GuiceUtil.getInstance(properties).register();
    Initializer.init();

    /*
     * Start web application container
     */
    jettyServer = new Server(PORT);
    HandlerList handlerList = new HandlerList();
    /*
     * The following is for solr for later, when this is to be used it
     */
    System.setProperty("solr.solr.home", "./target/sample-conf/");
    Handler solr = new WebAppContext("./target/solr/", "/solr");
    handlerList.addHandler(solr);

    final String webapp = "./src/main/webapp/";
    if (!new File(webapp).exists()) {
      throw new IllegalStateException("WebApp file/dir does not exist!");
    }
    WebAppContext webAppHandler = new WebAppContext(webapp, "/smartuser");
    handlerList.addHandler(webAppHandler);
    jettyServer.setHandler(handlerList);
    jettyServer.setSendDateHeader(true);
    jettyServer.start();

    /*
     * Setup client properties
     */
    System.setProperty(ApplicationWideClientFactoryImpl.TRACE, "true");
  }

  @AfterClass
  public static void globalTearDown() throws Exception {
    TEST_UTIL.shutdownMiniCluster();
    jettyServer.stop();
  }

  @Test
  public void testBootstraping() throws InterruptedException {
    comprehensiveClientTest.testBootstraping();
  }

  @Test
  public void doTestCreateOrganization() {
    try {
      comprehensiveClientTest.doTestCreateOrganization();
    }
    catch (Exception ex) {
      Assert.fail("Exception from create-organization-test");
    }
  }

  @Test
  public void doTestUpdateOrganization() throws InterruptedException {
    comprehensiveClientTest.doTestUpdateOrganization();
  }

  @Test
  public void doInitialTest() throws InterruptedException {
    LOGGER.info("starting initial test");
    comprehensiveClientTest.doInitialTest();
  }

  @Test
  public void doTestCreateUser() throws InterruptedException {
    comprehensiveClientTest.doTestCreateUser();
  }

  @Test
  public void doTestCreateAnotherUser() {
    comprehensiveClientTest.doTestCreateAnotherUser();
  }

  @Test
  public void doTestUpdateUser() throws InterruptedException {
    comprehensiveClientTest.doTestUpdateUser();
  }
  
  @Test
  public void doTestUserUpdateSelf() throws InterruptedException {
    comprehensiveClientTest.doTestUserUpdateSelf();
  }

  @Test
  public void doTestCreatePrivilegesofOrganization() {
    comprehensiveClientTest.doTestCreatePrivilegesofOrganization();
  }

  @Test
  public void doTestAddPrivilegesToUser() {
    comprehensiveClientTest.doTestAddPrivilegesToUser();
  }

  @Test
  public void doTestRemoveUserPrivilegeFromUser() throws InterruptedException {
    comprehensiveClientTest.doTestRemoveUserPrivilegeFromUser();
  }

  @Test
  public void doTestCreateUserGroup() {
    comprehensiveClientTest.doTestCreateUserGroup();
  }

  @Test
  public void doTestAddUsersToUserGroup() {
    comprehensiveClientTest.doTestAddUsersToUserGroup();
  }

  @Test
  public void doTestRemoveUserFromUserGroup() throws InterruptedException {
    comprehensiveClientTest.doTestRemoveUserFromUserGroup();
  }

  @Test
  public void doTestAddPrivilegesToUserGroup() throws InterruptedException {
    comprehensiveClientTest.doTestAddPrivilegesToUserGroup();
  }

  @Test
  public void doTestRemovePrivilegeFromUserGroup() {
    comprehensiveClientTest.doTestRemovePrivilegeFromUserGroup();
  }

  @Test
  public void doTestAddRoleToUserGroup() {
    comprehensiveClientTest.doTestAddRoleToUserGroup();
  }

  @Test
  public void doTestRemoveRoleFromUserGroup() {
    comprehensiveClientTest.doTestRemoveRoleFromUserGroup();
  }

  @Test
  public void doTestAuthoriztionForAdminUser() {
    comprehensiveClientTest.doTestAuthoriztionForAdminUser();
  }

  @Test
  public void doTestGetUser() {
    comprehensiveClientTest.doTestGetUser();
  }

  @Test
  public void doTestAuthorizationForUser() throws InterruptedException {
    comprehensiveClientTest.doTestAuthorizationForUser();
  }
  @Test
  public void doTestAuthorizationForUserGroup() {
    comprehensiveClientTest.doTestAuthorizationForUserGroup();
  }
  @Test
  public void doTestDeleteOrganization() throws InterruptedException {
    comprehensiveClientTest.doTestDeleteOrganization();
  }
   @Test
  public void doTestUsersAndPrivsAfterRemovingOrg() throws InterruptedException {
     comprehensiveClientTest.doTestUsersAndPrivsAfterRemovingOrg();
   }

  public static class ConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
      bind(Configuration.class).toInstance(TEST_UTIL.getConfiguration());
      ConnectionConfig config = new ConnectionConfig();
      config.setBasicUri("");
      config.setContextPath("/smartuser/");
      config.setHost("localhost");
      config.setPort(PORT);
      bind(ConnectionConfig.class).toInstance(config);
    }
  }
}
