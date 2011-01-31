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
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.impl.cache.PersonServiceCacheImpl;
import java.util.Properties;

/**
 *
 * @author imyousuf
 */
public class PersonCacheModule extends PrivateModule {

  private final String prefixSeparator;

  public PersonCacheModule(Properties properties) {
    prefixSeparator = properties.getProperty(ImplServiceModule.PREFIX_SEPARATOR_PROP_KEY,
                                             ImplServiceModule.PREFIX_SEPARATOR_PROP_DEFAULT);
  }

  @Override
  protected void configure() {
    bind(new TypeLiteral<CacheServiceProvider<Long, Person>>() {
    }).to(new TypeLiteral<EhcacheCacheServiceProviderImpl<Long, Person>>() {
    });
    bind(new TypeLiteral<BasicKey<Long>>() {
    }).toInstance(ImplServiceModule.<Long>getKeyInstance("Person", prefixSeparator));
    bind(PersonService.class).to(PersonServiceCacheImpl.class).in(Singleton.class);
    binder().expose(PersonService.class);
  }
}
