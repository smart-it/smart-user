/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.service.impl.guice;

import com.google.inject.Provider;
import com.smartitengineering.dao.impl.hbase.spi.impl.JsonConfigLoader;
import com.smartitengineering.dao.impl.hbase.spi.impl.SchemaInfoProviderBaseConfig;
import com.smartitengineering.user.service.impl.hbase.domain.AutoId;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author saumitra
 */
public class AutoIdSchemaBaseConfigProvider implements Provider<SchemaInfoProviderBaseConfig<AutoId>> {

  @Override
  public SchemaInfoProviderBaseConfig<AutoId> get() {
    try {
      final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(
          "com/smartitengineering/user/impl/autoId/AutoIdSchemaBaseConfig.json");
      return JsonConfigLoader.parseJsonAsBaseConfig(resourceAsStream);
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
