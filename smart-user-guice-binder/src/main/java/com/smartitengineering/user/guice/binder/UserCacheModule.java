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
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.service.impl.cache.UserServiceCacheImpl;
import java.util.Properties;

/**
 *
 * @author imyousuf
 */
public class UserCacheModule extends PrivateModule {

  private final String prefixSeparator;

  public UserCacheModule(Properties properties) {
    prefixSeparator = properties.getProperty(ImplServiceModule.PREFIX_SEPARATOR_PROP_KEY,
                                             ImplServiceModule.PREFIX_SEPARATOR_PROP_DEFAULT);
  }

  @Override
  protected void configure() {
    bind(new TypeLiteral<CacheServiceProvider<Long, User>>() {
    }).to(new TypeLiteral<EhcacheCacheServiceProviderImpl<Long, User>>() {
    });
    bind(new TypeLiteral<BasicKey<Long>>() {
    }).toInstance(ImplServiceModule.<Long>getKeyInstance("User", prefixSeparator));
    bind(UserService.class).to(UserServiceCacheImpl.class).in(Singleton.class);
    binder().expose(UserService.class);
  }
}
