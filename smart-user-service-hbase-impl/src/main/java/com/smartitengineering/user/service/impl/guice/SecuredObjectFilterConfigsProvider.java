/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.guice;

import com.google.inject.Provider;
import com.smartitengineering.dao.impl.hbase.spi.FilterConfigs;
import com.smartitengineering.dao.impl.hbase.spi.impl.JsonConfigLoader;
import com.smartitengineering.user.domain.SecuredObject;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author saumitra
 */
public class SecuredObjectFilterConfigsProvider implements Provider<FilterConfigs<SecuredObject>>{

  @Override
  public FilterConfigs<SecuredObject> get() {
    try {
      final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(
          "com/smartitengineering/user/impl/securedObject/SecuredObjectFilterConfigs.json");
      return JsonConfigLoader.parseJsonAsFilterConfigMap(resourceAsStream);
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

}
