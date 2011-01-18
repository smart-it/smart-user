/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.guice;

import com.google.inject.Provider;
import com.smartitengineering.dao.impl.hbase.spi.FilterConfigs;
import com.smartitengineering.dao.impl.hbase.spi.impl.JsonConfigLoader;
import com.smartitengineering.user.domain.UserPerson;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author saumitra
 */
public class UserPersonFilterConfigsProvider implements Provider<FilterConfigs<UserPerson>>{

  @Override
  public FilterConfigs<UserPerson> get() {
    try {
      final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(
          "com/smartitengineering/user/impl/userPerson/UserPersonFilterConfigs.json");
      return JsonConfigLoader.parseJsonAsFilterConfigMap(resourceAsStream);
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

}
