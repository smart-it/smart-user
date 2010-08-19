/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.client.impl;

import com.smartitengineering.util.bean.BeanFactoryRegistrar;
import com.smartitengineering.util.bean.annotations.Aggregator;
import com.smartitengineering.util.bean.annotations.InjectableField;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author russel
 */
@Aggregator(contextName = "userRestClientContext")
public final class ConfigFactory {

  @InjectableField
  private ConnectionConfig connectionConfig;
  private static ConfigFactory configFactory;

  public static ConfigFactory getInstance() {
    if (configFactory == null) {
      configFactory = new ConfigFactory();
    }
    return configFactory;
  }

  private ConfigFactory() {
    BeanFactoryRegistrar.aggregate(this);
    if (connectionConfig == null) {
      String propFileName = "user-client-config.properties";
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
      if (inputStream != null) {
        Properties properties = new Properties();
        try {
          properties.load(inputStream);
          connectionConfig = new ConnectionConfig();
          connectionConfig.setBasicUri(properties.getProperty("baseUri", ""));
          connectionConfig.setContextPath(properties.getProperty("contextPath", "/"));
          connectionConfig.setHost(properties.getProperty("host", "localhost"));
          connectionConfig.setPort(NumberUtils.toInt(properties.getProperty("port", ""), 9090));
        }
        catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  public ConnectionConfig getConnectionConfig() {
    return connectionConfig;
  }
}
