/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.guice.binder;

import com.google.inject.PrivateModule;
import com.google.inject.TypeLiteral;
import com.smartitengineering.dao.common.cache.BasicKey;
import com.smartitengineering.dao.common.cache.CacheServiceProvider;
import com.smartitengineering.dao.common.cache.impl.ehcache.EhcacheCacheServiceProviderImpl;
import java.util.Properties;

/**
 *
 * @author imyousuf
 */
public class NameCacheModule extends PrivateModule {

  private final String prefixSeparator;

  public NameCacheModule(Properties properties) {
    prefixSeparator = properties.getProperty(ImplServiceModule.PREFIX_SEPARATOR_PROP_KEY,
                                             ImplServiceModule.PREFIX_SEPARATOR_PROP_DEFAULT);
  }

  @Override
  protected void configure() {
    bind(new TypeLiteral<CacheServiceProvider<String, Long>>() {
    }).to(new TypeLiteral<EhcacheCacheServiceProviderImpl<String, Long>>() {
    });
    bind(new TypeLiteral<BasicKey<String>>() {
    }).toInstance(ImplServiceModule.<String>getKeyInstance("NameCache", prefixSeparator));
    binder().expose(new TypeLiteral<CacheServiceProvider<String, Long>>() {
    });
  }
}
