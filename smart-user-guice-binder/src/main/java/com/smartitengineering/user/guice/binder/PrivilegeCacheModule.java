/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.smartitengineering.user.guice.binder;

import com.google.inject.PrivateModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.smartitengineering.dao.common.cache.BasicKey;
import com.smartitengineering.dao.common.cache.CacheServiceProvider;
import com.smartitengineering.dao.common.cache.impl.ehcache.EhcacheCacheServiceProviderImpl;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.impl.cache.PrivilegeServiceCacheImpl;
import java.util.Properties;

/**
 *
 * @author imyousuf
 */
public class PrivilegeCacheModule extends PrivateModule {

  private final String prefixSeparator;

  public PrivilegeCacheModule(Properties properties) {
    prefixSeparator = properties.getProperty(ImplServiceModule.PREFIX_SEPARATOR_PROP_KEY,
                                             ImplServiceModule.PREFIX_SEPARATOR_PROP_DEFAULT);
  }

  @Override
  protected void configure() {
    bind(new TypeLiteral<CacheServiceProvider<Long, Privilege>>() {
    }).to(new TypeLiteral<EhcacheCacheServiceProviderImpl<Long, Privilege>>() {
    });
    bind(new TypeLiteral<BasicKey<Long>>() {
    }).toInstance(ImplServiceModule.<Long>getKeyInstance("Privilege", prefixSeparator));
    bind(PrivilegeService.class).to(PrivilegeServiceCacheImpl.class).in(Singleton.class);
    binder().expose(PrivilegeService.class);
  }
}
