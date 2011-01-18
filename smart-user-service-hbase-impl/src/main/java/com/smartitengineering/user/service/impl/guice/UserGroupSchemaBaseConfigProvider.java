/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.service.impl.guice;

import com.google.inject.Provider;
import com.smartitengineering.dao.impl.hbase.spi.impl.JsonConfigLoader;
import com.smartitengineering.dao.impl.hbase.spi.impl.SchemaInfoProviderBaseConfig;
import com.smartitengineering.user.domain.UserGroup;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author saumitra
 */
public class UserGroupSchemaBaseConfigProvider implements Provider<SchemaInfoProviderBaseConfig<UserGroup>>{

  @Override
  public SchemaInfoProviderBaseConfig<UserGroup> get() {
   try {
      final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(
          "com/smartitengineering/user/impl/userGroup/UserGroupSchemaBaseConfig.json");
      return JsonConfigLoader.parseJsonAsBaseConfig(resourceAsStream);
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

}
