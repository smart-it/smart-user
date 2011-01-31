/*
It is a application for event distribution to event n-consumers with m-sources.
Copyright (C) 2010 "Imran M Yousuf <imran@smartitengineering.com>"

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or any later
version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.smartitengineering.user.service.impl.hbase.servlet;


import com.smartitengineering.dao.hbase.ddl.HBaseTableGenerator;
import com.smartitengineering.dao.hbase.ddl.config.json.ConfigurationJsonParser;
import com.smartitengineering.dao.impl.hbase.HBaseConfigurationFactory;
import com.smartitengineering.user.guice.binder.Initializer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author imyousuf
 */
public class InitializerContextListener implements ServletContextListener {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    Initializer.init();
    Configuration config = HBaseConfigurationFactory.getConfigurationInstance();
    try {
      new HBaseTableGenerator(ConfigurationJsonParser.getConfigurations(getClass().getClassLoader().
          getResourceAsStream("com/smartitengineering/user/service/impl/hbase/config/schema.json")), config, false).generateTables();
    }
    catch (MasterNotRunningException ex) {
      logger.error("Master could not be found!", ex);
    }
    catch (Exception ex) {
      logger.error("Could not create table!", ex);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
  }
}
