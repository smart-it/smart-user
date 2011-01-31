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
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.service.SecuredObjectService;
import com.smartitengineering.user.service.impl.cache.SecuredObjectServiceCacheImpl;
import java.util.Properties;

/**
 *
 * @author imyousuf
 */
public class SecuredObjectCacheModule extends PrivateModule {

  private final String prefixSeparator;

  public SecuredObjectCacheModule(Properties properties) {
    prefixSeparator = properties.getProperty(ImplServiceModule.PREFIX_SEPARATOR_PROP_KEY,
                                             ImplServiceModule.PREFIX_SEPARATOR_PROP_DEFAULT);
  }

  @Override
  protected void configure() {
    bind(new TypeLiteral<CacheServiceProvider<Long, SecuredObject>>() {
    }).to(new TypeLiteral<EhcacheCacheServiceProviderImpl<Long, SecuredObject>>() {
    });
    bind(new TypeLiteral<BasicKey<Long>>() {
    }).toInstance(ImplServiceModule.<Long>getKeyInstance("SecuredObject", prefixSeparator));
    bind(SecuredObjectService.class).to(SecuredObjectServiceCacheImpl.class).in(Singleton.class);
    binder().expose(SecuredObjectService.class);
  }
}
