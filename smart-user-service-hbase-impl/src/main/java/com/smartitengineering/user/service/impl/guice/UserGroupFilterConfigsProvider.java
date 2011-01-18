/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.guice;

import com.google.inject.Provider;
import com.smartitengineering.dao.impl.hbase.spi.FilterConfigs;
import com.smartitengineering.dao.impl.hbase.spi.impl.JsonConfigLoader;
import com.smartitengineering.user.domain.UserGroup;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author saumitra
 */
public class UserGroupFilterConfigsProvider implements Provider<FilterConfigs<UserGroup>>{

  @Override
  public FilterConfigs<UserGroup> get() {
    try {
      final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(
          "com/smartitengineering/user/impl/userGroup/UserGroupFilterConfigs.json");
      return JsonConfigLoader.parseJsonAsFilterConfigMap(resourceAsStream);
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

}
