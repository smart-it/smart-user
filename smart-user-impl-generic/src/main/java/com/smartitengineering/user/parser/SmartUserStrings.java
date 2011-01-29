/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.parser;

import com.smartitengineering.util.bean.PropertiesLocator;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.io.IOException;

/**
 *
 * @author modhu7
 */
public class SmartUserStrings {

  public static final String ADMIN_USERNAME;
  public static final String ADMIN_PASSWORD;
  public static final String SUPER_ADMIN_USERNAME;
  public static final String SUPER_ADMIN_PASSWORD;
  public static final String ORGANIZATIONS_URL;
  public static final String USERS_URL;
  public static final String SECURED_OBJECTS_URL;
  public static final String PRIVILEGES_URL;
  public static final String ROLES_URL;
  public static final String ORGANIZATION_UNIQUE_URL_FRAGMENT;
  public static final String USER_UNIQUE_URL_FRAGMENT;
  public static final String PRIVILEGE_UNIQUE_FRAGMENT;
  public static final String SECURED_OBJECT_UNIQUE_URL_FRAGMENT;
  public static final String ROLE_UNIQUE_FRAGMENT;
  public static final String CONTENT_URI_FRAGMENT;
  public static final String DELETE_URI_FRAGMENT;
  public static final String UPDATE_URI_FRAGMENT;
  public static final String FIRST_ORGANIZATION_NAME;
  public static final String FIRST_ORGANIZATION_SHORT_NAME;
  public static final String EMAIL_DOMAIN;
  private static final String ADMIN_USERNAME_KEY = "admin_username";
  private static final String ADMIN_PASSWORD_KEY = "admin_password";
  private static final String SUPER_ADMIN_USERNAME_KEY = "super_admin_username";
  private static final String SUPER_ADMIN_PASSWORD_KEY = "super_admin_password";
  private static final String ORGANIZATIONS_URL_KEY = "organization_url";
  private static final String USERS_URL_KEY = "users_url";
  private static final String SECURED_OBJECTS_URL_KEY = "secured_objects_url";
  private static final String PRIVILEGES_URL_KEY = "privileges_url";
  private static final String ROLES_URL_KEY = "roles_url";
  private static final String ORGANIZATION_UNIQUE_URL_FRAGMENT_KEY = "org_unique_uri_fragment";
  private static final String USER_UNIQUE_URL_FRAGMENT_KEY = "user_unique_uri_fragment";
  private static final String PRIVILEGE_UNIQUE_FRAGMENT_KEY = "privilege_unique_uri_fragment";
  private static final String SECURED_OBJECT_UNIQUE_URL_FRAGMENT_KEY = "secured_object_unique_uri_fragment";
  private static final String ROLE_UNIQUE_FRAGMENT_KEY = "role_unique_uri_fragment";
  private static final String CONTENT_URI_FRAGMENT_KEY = "content_uri_fragment";
  private static final String DELETE_URI_FRAGMENT_KEY = "delete_uri_fragment";
  private static final String UPDATE_URI_FRAGMENT_KEY = "update_uri_fragment";
  private static final String FIRST_ORGANIZATION_NAME_KEY = "first_organization_name";
  private static final String FIRST_ORGANIZATION_SHORT_NAME_KEY = "first_organization_short_name";
  private static final String EMAIL_DOMAIN_KEY = "email_domain";

  static {
    Properties properties = new Properties();
    PropertiesLocator propertiesLocator = new PropertiesLocator();
    propertiesLocator.setSmartLocations("com/smartitengineering/user/properties/user-static-strings.properties");
    
    try {
      propertiesLocator.loadProperties(properties);
      ADMIN_USERNAME = properties.getProperty(ADMIN_USERNAME_KEY);
      ADMIN_PASSWORD = properties.getProperty(ADMIN_PASSWORD_KEY);
      SUPER_ADMIN_USERNAME = properties.getProperty(SUPER_ADMIN_USERNAME_KEY);
      SUPER_ADMIN_PASSWORD = properties.getProperty(SUPER_ADMIN_PASSWORD_KEY);
      CONTENT_URI_FRAGMENT = properties.getProperty(CONTENT_URI_FRAGMENT_KEY);
      DELETE_URI_FRAGMENT = properties.getProperty(DELETE_URI_FRAGMENT_KEY);
      EMAIL_DOMAIN = properties.getProperty(EMAIL_DOMAIN_KEY);
      FIRST_ORGANIZATION_NAME = properties.getProperty(FIRST_ORGANIZATION_NAME_KEY);
      FIRST_ORGANIZATION_SHORT_NAME = properties.getProperty(FIRST_ORGANIZATION_SHORT_NAME_KEY);
      ORGANIZATIONS_URL = properties.getProperty(ORGANIZATIONS_URL_KEY);
      ORGANIZATION_UNIQUE_URL_FRAGMENT = properties.getProperty(ORGANIZATION_UNIQUE_URL_FRAGMENT_KEY);
      PRIVILEGES_URL = properties.getProperty(PRIVILEGES_URL_KEY);
      PRIVILEGE_UNIQUE_FRAGMENT = properties.getProperty(PRIVILEGE_UNIQUE_FRAGMENT_KEY);
      ROLES_URL = properties.getProperty(ROLES_URL_KEY);
      ROLE_UNIQUE_FRAGMENT = properties.getProperty(ROLE_UNIQUE_FRAGMENT_KEY);
      SECURED_OBJECTS_URL = properties.getProperty(SECURED_OBJECTS_URL_KEY);
      SECURED_OBJECT_UNIQUE_URL_FRAGMENT = properties.getProperty(SECURED_OBJECT_UNIQUE_URL_FRAGMENT_KEY);
      UPDATE_URI_FRAGMENT = properties.getProperty(UPDATE_URI_FRAGMENT_KEY);
      USERS_URL = properties.getProperty(USERS_URL_KEY);
      USER_UNIQUE_URL_FRAGMENT = properties.getProperty(USER_UNIQUE_URL_FRAGMENT_KEY);

    }
    catch (FileNotFoundException ex) {
      throw new RuntimeException("Properties file is not found");
    }
    catch (IOException ex) {
      throw new RuntimeException("Properties input output exception");
    }
  }
}
