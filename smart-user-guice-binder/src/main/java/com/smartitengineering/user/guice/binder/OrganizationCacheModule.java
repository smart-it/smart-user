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
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.impl.cache.OrganizationServiceCacheImpl;
import java.util.Properties;

/**
 *
 * @author imyousuf
 */
public class OrganizationCacheModule extends PrivateModule {

  private final String prefixSeparator;

  public OrganizationCacheModule(Properties properties) {
    prefixSeparator = properties.getProperty(ImplServiceModule.PREFIX_SEPARATOR_PROP_KEY,
                                             ImplServiceModule.PREFIX_SEPARATOR_PROP_DEFAULT);
  }

  @Override
  protected void configure() {
    bind(new TypeLiteral<CacheServiceProvider<String, Organization>>() {
    }).to(new TypeLiteral<EhcacheCacheServiceProviderImpl<String, Organization>>() {
    });
    bind(new TypeLiteral<BasicKey<String>>() {
    }).toInstance(ImplServiceModule.<String>getKeyInstance("Organization", prefixSeparator));
    bind(OrganizationService.class).to(OrganizationServiceCacheImpl.class).in(Singleton.class);
    binder().expose(OrganizationService.class);
  }
}
