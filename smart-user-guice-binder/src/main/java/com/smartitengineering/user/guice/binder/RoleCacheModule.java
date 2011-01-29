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
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.service.impl.cache.RoleServiceCacheImpl;
import java.util.Properties;

/**
 *
 * @author imyousuf
 */
public class RoleCacheModule extends PrivateModule {

  private final String prefixSeparator;

  public RoleCacheModule(Properties properties) {
    prefixSeparator = properties.getProperty(ImplServiceModule.PREFIX_SEPARATOR_PROP_KEY,
                                             ImplServiceModule.PREFIX_SEPARATOR_PROP_DEFAULT);
  }

  @Override
  protected void configure() {
    bind(new TypeLiteral<CacheServiceProvider<Long, Role>>() {
    }).to(new TypeLiteral<EhcacheCacheServiceProviderImpl<Long, Role>>() {
    });
    bind(new TypeLiteral<BasicKey<Long>>() {
    }).toInstance(ImplServiceModule.<Long>getKeyInstance("Role", prefixSeparator));
    bind(RoleService.class).to(RoleServiceCacheImpl.class).in(Singleton.class);
    binder().expose(RoleService.class);
  }
}
