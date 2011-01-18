/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smartitengineering.user.guice.binder;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.smartitengineering.common.dao.search.CommonFreeTextPersistentDao;
import com.smartitengineering.common.dao.search.CommonFreeTextSearchDao;
import com.smartitengineering.common.dao.search.impl.CommonAsyncFreeTextPersistentDaoImpl;
import com.smartitengineering.common.dao.search.solr.SolrFreeTextPersistentDao;
import com.smartitengineering.common.dao.search.solr.SolrFreeTextSearchDao;
import com.smartitengineering.common.dao.search.solr.spi.ObjectIdentifierQuery;
import com.smartitengineering.dao.common.CommonDao;
import com.smartitengineering.dao.common.CommonReadDao;
import com.smartitengineering.dao.common.CommonWriteDao;
import com.smartitengineering.dao.impl.hbase.spi.AsyncExecutorService;
import com.smartitengineering.dao.impl.hbase.spi.CellConfig;
import com.smartitengineering.dao.impl.hbase.spi.DomainIdInstanceProvider;
import com.smartitengineering.dao.impl.hbase.spi.FilterConfigs;
import com.smartitengineering.dao.impl.hbase.spi.LockAttainer;
import com.smartitengineering.dao.impl.hbase.spi.MergeService;
import com.smartitengineering.dao.impl.hbase.spi.ObjectRowConverter;
import com.smartitengineering.dao.impl.hbase.spi.RowCellIncrementor;
import com.smartitengineering.dao.impl.hbase.spi.SchemaInfoProvider;
import com.smartitengineering.dao.impl.hbase.spi.impl.CellConfigImpl;
import com.smartitengineering.dao.impl.hbase.spi.impl.DiffBasedMergeService;
import com.smartitengineering.dao.impl.hbase.spi.impl.LockAttainerImpl;
import com.smartitengineering.dao.impl.hbase.spi.impl.MixedExecutorServiceImpl;
import com.smartitengineering.dao.impl.hbase.spi.impl.RowCellIncrementorImpl;
import com.smartitengineering.dao.impl.hbase.spi.impl.SchemaInfoProviderBaseConfig;
import com.smartitengineering.dao.impl.hbase.spi.impl.SchemaInfoProviderImpl;
import com.smartitengineering.dao.impl.hbase.spi.impl.guice.GenericBaseConfigProvider;
import com.smartitengineering.dao.impl.hbase.spi.impl.guice.GenericFilterConfigsProvider;
import com.smartitengineering.dao.impl.search.CommonWriteDaoDecorator;
import com.smartitengineering.dao.solr.MultivalueMap;
import com.smartitengineering.dao.solr.ServerConfiguration;
import com.smartitengineering.dao.solr.ServerFactory;
import com.smartitengineering.dao.solr.SolrQueryDao;
import com.smartitengineering.dao.solr.SolrWriteDao;
import com.smartitengineering.dao.solr.impl.ServerConfigurationImpl;
import com.smartitengineering.dao.solr.impl.SingletonRemoteServerFactory;
import com.smartitengineering.dao.solr.impl.SolrDao;
import com.smartitengineering.user.domain.Organization;
import com.smartitengineering.user.domain.Person;
import com.smartitengineering.user.domain.Privilege;
import com.smartitengineering.user.domain.Role;
import com.smartitengineering.user.domain.SecuredObject;
import com.smartitengineering.user.domain.User;
import com.smartitengineering.user.domain.UserGroup;
import com.smartitengineering.user.domain.UserPerson;
import com.smartitengineering.user.observer.CRUDObservable;
import com.smartitengineering.user.service.OrganizationService;
import com.smartitengineering.user.service.PersonService;
import com.smartitengineering.user.service.PrivilegeService;
import com.smartitengineering.user.service.RoleService;
import com.smartitengineering.user.service.SecuredObjectService;
import com.smartitengineering.user.service.UserGroupService;
import com.smartitengineering.user.service.UserPersonService;
import com.smartitengineering.user.service.UserService;
import com.smartitengineering.user.service.impl.ObservableImpl;
import com.smartitengineering.user.service.impl.ObserverImpl;
import com.smartitengineering.user.service.impl.hbase.OrganizationServiceImpl;
import com.smartitengineering.user.service.impl.hbase.PersonServiceImpl;
import com.smartitengineering.user.service.impl.hbase.PrivilegeServiceImpl;
import com.smartitengineering.user.service.impl.hbase.RoleServiceImpl;
import com.smartitengineering.user.service.impl.hbase.SecuredObjectServiceImpl;
import com.smartitengineering.user.service.impl.hbase.UserGroupServiceImpl;
import com.smartitengineering.user.service.impl.hbase.UserPersonServiceImpl;
import com.smartitengineering.user.service.impl.hbase.UserServiceImpl;
import com.smartitengineering.user.service.impl.hbase.dao.AutoIdObjectConverter;
import com.smartitengineering.user.service.impl.hbase.dao.OrganizationObjectConverter;
import com.smartitengineering.user.service.impl.hbase.dao.PersonObjectConverter;
import com.smartitengineering.user.service.impl.hbase.dao.PrivilegeObjectConverter;
import com.smartitengineering.user.service.impl.hbase.dao.RoleObjectConverter;
import com.smartitengineering.user.service.impl.hbase.dao.SecuredObjectConverter;
import com.smartitengineering.user.service.impl.hbase.dao.UniqueKeyIndexObjectConverter;
import com.smartitengineering.user.service.impl.hbase.dao.UserGroupObjectConverter;
import com.smartitengineering.user.service.impl.hbase.dao.UserObjectConverter;
import com.smartitengineering.user.service.impl.hbase.dao.UserPersonObjectConverter;
import com.smartitengineering.user.service.impl.hbase.domain.AutoId;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKey;
import com.smartitengineering.user.service.impl.hbase.domain.UniqueKeyIndex;
import com.smartitengineering.user.service.impl.hbase.solr.OrganizationIdentifierQueryImpl;
import com.smartitengineering.user.service.impl.hbase.solr.OrganizationAdapterHelper;
import com.smartitengineering.user.service.impl.hbase.solr.PersonAdapterHelper;
import com.smartitengineering.user.service.impl.hbase.solr.PersonIdentifierQueryImpl;
import com.smartitengineering.user.service.impl.hbase.solr.PrivilegeAdapterHelper;
import com.smartitengineering.user.service.impl.hbase.solr.PrivilegeIdentifierQueryImpl;
import com.smartitengineering.user.service.impl.hbase.solr.RoleAdapterHelper;
import com.smartitengineering.user.service.impl.hbase.solr.RoleIdentifierQueryImpl;
import com.smartitengineering.user.service.impl.hbase.solr.SecuredObjectAdapterHelper;
import com.smartitengineering.user.service.impl.hbase.solr.SecuredObjectIdentifierQueryImpl;
import com.smartitengineering.user.service.impl.hbase.solr.UserAdapterHelper;
import com.smartitengineering.user.service.impl.hbase.solr.UserGroupAdapterHelper;
import com.smartitengineering.user.service.impl.hbase.solr.UserGroupMultiAdapterHelper;
import com.smartitengineering.user.service.impl.hbase.solr.UserGroupIdentifierQueryimpl;
import com.smartitengineering.user.service.impl.hbase.solr.UserIdentifierQueryImpl;
import com.smartitengineering.user.service.impl.hbase.solr.UserPersonAdapterHelper;
import com.smartitengineering.user.service.impl.hbase.solr.UserPersonIdentifierQueryImpl;
import com.smartitengineering.user.service.impl.ip.provider.DomainIdInstanceProviderImpl;
import com.smartitengineering.util.bean.adapter.AbstractAdapterHelper;
import com.smartitengineering.util.bean.adapter.GenericAdapter;
import com.smartitengineering.util.bean.adapter.GenericAdapterImpl;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author russel
 */
public class ImplServiceModule extends AbstractModule {

  private final String autoIncrementUri;
  private final String solrMasterUri;
  private final long waitTime, saveInterval, updateInterval, deleteInterval;

  public ImplServiceModule(Properties properties) {
    autoIncrementUri = properties.getProperty("autoIncrementUri", "http://localhost:8080/row/");
    solrMasterUri = properties.getProperty("solrMasterUri", "http://localhost:8080/solr/");
    long toLong = NumberUtils.toLong(properties.getProperty("com.smartitengineering.pos.waitTimeInSec"), 10L);
    waitTime = toLong > 0 ? toLong : 10l;
    toLong = NumberUtils.toLong(properties.getProperty("com.smartitengineering.pos.saveIntervalInSec"), 60L);
    saveInterval = toLong > 0 ? toLong : 60l;
    toLong = NumberUtils.toLong(properties.getProperty("com.smartitengineering.pos.updateIntervalInSec"), 60L);
    updateInterval = toLong > 0 ? toLong : 60l;
    toLong = NumberUtils.toLong(properties.getProperty("com.smartitengineering.pos.deleteIntervalInSec"), 60L);
    deleteInterval = toLong > 0 ? toLong : 60l;
  }

  @Override
  protected void configure() {

    bind(AsyncExecutorService.class).to(MixedExecutorServiceImpl.class).in(Singleton.class);
    bind(ExecutorService.class).toInstance(Executors.newCachedThreadPool());
    bind(Integer.class).annotatedWith(Names.named("maxRows")).toInstance(new Integer(50));
    bind(Long.class).annotatedWith(Names.named("waitTime")).toInstance(waitTime);
    bind(TimeUnit.class).annotatedWith(Names.named("unit")).toInstance(TimeUnit.SECONDS);
    bind(Boolean.class).annotatedWith(Names.named("mergeEnabled")).toInstance(Boolean.TRUE);
    //bind(MergeService.class).to(DiffBasedMergeService.class).in(Singleton.class);

    /*
     * Solr client
     * waitTime:long and ExecutorService.class from earlier config
     */
    bind(TimeUnit.class).annotatedWith(Names.named("waitTimeUnit")).toInstance(TimeUnit.SECONDS);
    bind(SolrQueryDao.class).to(SolrDao.class).in(Scopes.SINGLETON);
    bind(SolrWriteDao.class).to(SolrDao.class).in(Scopes.SINGLETON);
    bind(ServerFactory.class).to(SingletonRemoteServerFactory.class).in(Scopes.SINGLETON);
    bind(ServerConfiguration.class).to(ServerConfigurationImpl.class).in(Scopes.SINGLETON);
    bind(String.class).annotatedWith(Names.named("uri")).toInstance(solrMasterUri);
    bind(Long.class).annotatedWith(Names.named("saveInterval")).toInstance(saveInterval);
    bind(Long.class).annotatedWith(Names.named("updateInterval")).toInstance(updateInterval);
    bind(Long.class).annotatedWith(Names.named("deleteInterval")).toInstance(deleteInterval);
    bind(TimeUnit.class).annotatedWith(Names.named("intervalTimeUnit")).toInstance(TimeUnit.SECONDS);
    /*
     * END solr client
     */



    /*
     * Start injection specific to common dao of Organization
     */
    bind(new TypeLiteral<ObjectRowConverter<Organization>>() {
    }).to(OrganizationObjectConverter.class).in(Singleton.class);
    bind(new TypeLiteral<CommonReadDao<Organization, String>>() {
    }).to(new TypeLiteral<CommonDao<Organization, String>>() {
    }).in(Singleton.class);

    /*
     * Configure write dao
     */
    bind(new TypeLiteral<CommonWriteDao<Organization>>() {
    }).annotatedWith(Names.named("searchWriteDaoDecoratee")).to(new TypeLiteral<CommonDao<Organization, String>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<CommonWriteDao<Organization>>() {
    }).to(new TypeLiteral<CommonWriteDaoDecorator<Organization>>() {
    }).in(Singleton.class);
    TypeLiteral<CommonFreeTextPersistentDao<Organization>> orgLit =
                                                           new TypeLiteral<CommonFreeTextPersistentDao<Organization>>() {
    };
    bind(orgLit).to(new TypeLiteral<CommonAsyncFreeTextPersistentDaoImpl<Organization>>() {
    }).in(Scopes.SINGLETON);
    bind(orgLit).annotatedWith(Names.named("primaryFreeTextPersistentDao")).to(new TypeLiteral<SolrFreeTextPersistentDao<Organization>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<ObjectIdentifierQuery<Organization>>() {
    }).to(OrganizationIdentifierQueryImpl.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<GenericAdapter<Organization, MultivalueMap<String, Object>>>() {
    }).to(new TypeLiteral<GenericAdapterImpl<Organization, MultivalueMap<String, Object>>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<AbstractAdapterHelper<Organization, MultivalueMap<String, Object>>>() {
    }).to(OrganizationAdapterHelper.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<CommonFreeTextSearchDao<Organization>>() {
    }).to(new TypeLiteral<SolrFreeTextSearchDao<Organization>>() {
    }).in(Scopes.SINGLETON);

    bind(new TypeLiteral<CommonDao<Organization, String>>() {
    }).to(new TypeLiteral<com.smartitengineering.dao.impl.hbase.CommonDao<Organization, String>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<LockAttainer<Organization, String>>() {
    }).to(new TypeLiteral<LockAttainerImpl<Organization, String>>() {
    }).in(Scopes.SINGLETON);

    final TypeLiteral<SchemaInfoProviderImpl<Organization, String>> organizationTypeLiteral = new TypeLiteral<SchemaInfoProviderImpl<Organization, String>>() {
    };
    bind(new TypeLiteral<FilterConfigs<Organization>>() {
    }).toProvider(new GenericFilterConfigsProvider<Organization>(
        "com/smartitengineering/user/service/impl/hbase/config/OrganizationFilterConfigs.json")).in(Scopes.SINGLETON);
    bind(new TypeLiteral<SchemaInfoProviderBaseConfig<Organization>>() {
    }).toProvider(new GenericBaseConfigProvider<Organization>(
        "com/smartitengineering/user/service/impl/hbase/config/OrganizationSchemaBaseConfig.json")).in(Scopes.SINGLETON);

    bind(new TypeLiteral<Class<String>>() {
    }).toInstance(String.class);
    bind(new TypeLiteral<SchemaInfoProvider<Organization, String>>() {
    }).to(organizationTypeLiteral).in(Singleton.class);

    bind(new TypeLiteral<MergeService<Organization, String>>() {
    }).to(new TypeLiteral<DiffBasedMergeService<Organization, String>>() {
    }).in(Singleton.class);

    bind(OrganizationService.class).to(OrganizationServiceImpl.class).in(Singleton.class);

    ObservableImpl observableImpl = new ObservableImpl();
    observableImpl.addObserver(new ObserverImpl());

    bind(CRUDObservable.class).toInstance(observableImpl);

    bind(DomainIdInstanceProvider.class).to(DomainIdInstanceProviderImpl.class);

    /*
     * Start injection specific to common dao of User
     */
    bind(new TypeLiteral<ObjectRowConverter<User>>() {
    }).to(UserObjectConverter.class).in(Singleton.class);
    bind(new TypeLiteral<CommonReadDao<User, Long>>() {
    }).to(new TypeLiteral<CommonDao<User, Long>>() {
    }).in(Singleton.class);

    /*
     * Configure write dao
     */
    bind(new TypeLiteral<CommonWriteDao<User>>() {
    }).annotatedWith(Names.named("searchWriteDaoDecoratee")).to(new TypeLiteral<CommonDao<User, Long>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<CommonWriteDao<User>>() {
    }).to(new TypeLiteral<CommonWriteDaoDecorator<User>>() {
    }).in(Singleton.class);
    TypeLiteral<CommonFreeTextPersistentDao<User>> userLit =
                                                   new TypeLiteral<CommonFreeTextPersistentDao<User>>() {
    };
    bind(userLit).to(new TypeLiteral<CommonAsyncFreeTextPersistentDaoImpl<User>>() {
    }).in(Scopes.SINGLETON);
    bind(userLit).annotatedWith(Names.named("primaryFreeTextPersistentDao")).to(new TypeLiteral<SolrFreeTextPersistentDao<User>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<ObjectIdentifierQuery<User>>() {
    }).to(UserIdentifierQueryImpl.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<GenericAdapter<User, MultivalueMap<String, Object>>>() {
    }).to(new TypeLiteral<GenericAdapterImpl<User, MultivalueMap<String, Object>>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<AbstractAdapterHelper<User, MultivalueMap<String, Object>>>() {
    }).to(UserAdapterHelper.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<CommonFreeTextSearchDao<User>>() {
    }).to(new TypeLiteral<SolrFreeTextSearchDao<User>>() {
    }).in(Scopes.SINGLETON);

    bind(new TypeLiteral<CommonDao<User, Long>>() {
    }).to(new TypeLiteral<com.smartitengineering.dao.impl.hbase.CommonDao<User, Long>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<LockAttainer<User, Long>>() {
    }).to(new TypeLiteral<LockAttainerImpl<User, Long>>() {
    }).in(Scopes.SINGLETON);

    final TypeLiteral<SchemaInfoProviderImpl<User, Long>> userTypeLiteral = new TypeLiteral<SchemaInfoProviderImpl<User, Long>>() {
    };
    bind(new TypeLiteral<FilterConfigs<User>>() {
    }).toProvider(new GenericFilterConfigsProvider<User>(
        "com/smartitengineering/user/service/impl/hbase/config/UserFilterConfigs.json")).in(Scopes.SINGLETON);
    bind(new TypeLiteral<SchemaInfoProviderBaseConfig<User>>() {
    }).toProvider(new GenericBaseConfigProvider<User>(
        "com/smartitengineering/user/service/impl/hbase/config/UserSchemaBaseConfig.json")).in(Scopes.SINGLETON);

//    bind(new TypeLiteral<Class<Long>>() {
//    }).toInstance(Long.class);
    bind(new TypeLiteral<SchemaInfoProvider<User, Long>>() {
    }).to(userTypeLiteral).in(Singleton.class);

    bind(new TypeLiteral<MergeService<User, Long>>() {
    }).to(new TypeLiteral<DiffBasedMergeService<User, Long>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<RowCellIncrementor<User, AutoId, String>>() {
    }).to(new TypeLiteral<RowCellIncrementorImpl<User, AutoId, String>>() {
    });
    CellConfigImpl<User> configImpl = new CellConfigImpl<User>();
    configImpl.setFamily("self");
    configImpl.setQualifier("userId");

    bind(new TypeLiteral<CellConfig<User>>() {
    }).toInstance(configImpl);

    // ---------------------




    bind(new TypeLiteral<ObjectRowConverter<UniqueKeyIndex>>() {
    }).to(UniqueKeyIndexObjectConverter.class).in(Singleton.class);

    bind(new TypeLiteral<CommonReadDao<UniqueKeyIndex, UniqueKey>>() {
    }).to(new TypeLiteral<CommonDao<UniqueKeyIndex, UniqueKey>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<CommonWriteDao<UniqueKeyIndex>>() {
    }).to(new TypeLiteral<CommonDao<UniqueKeyIndex, UniqueKey>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<CommonDao<UniqueKeyIndex, UniqueKey>>() {
    }).to(new TypeLiteral<com.smartitengineering.dao.impl.hbase.CommonDao<UniqueKeyIndex, UniqueKey>>() {
    }).in(Singleton.class);

    final TypeLiteral<SchemaInfoProviderImpl<UniqueKeyIndex, UniqueKey>> uniqueKeyTypeLiteral = new TypeLiteral<SchemaInfoProviderImpl<UniqueKeyIndex, UniqueKey>>() {
    };
    bind(new TypeLiteral<FilterConfigs<UniqueKeyIndex>>() {
    }).toProvider(new GenericFilterConfigsProvider<UniqueKeyIndex>(
        "com/smartitengineering/user/service/impl/hbase/config/UniquekeyFilterConfigs.json")).in(Scopes.SINGLETON);
    bind(new TypeLiteral<SchemaInfoProviderBaseConfig<UniqueKeyIndex>>() {
    }).toProvider(new GenericBaseConfigProvider<UniqueKeyIndex>(
        "com/smartitengineering/user/service/impl/hbase/config/UniqueKeySchemaBaseConfig.json")).in(Scopes.SINGLETON);

    bind(new TypeLiteral<Class<UniqueKey>>() {
    }).toInstance(UniqueKey.class);
    bind(new TypeLiteral<SchemaInfoProvider<UniqueKeyIndex, UniqueKey>>() {
    }).to(uniqueKeyTypeLiteral).in(Singleton.class);

    bind(new TypeLiteral<MergeService<UniqueKeyIndex, UniqueKey>>() {
    }).to(new TypeLiteral<DiffBasedMergeService<UniqueKeyIndex, UniqueKey>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<LockAttainer<UniqueKeyIndex, UniqueKey>>() {
    }).to(new TypeLiteral<LockAttainerImpl<UniqueKeyIndex, UniqueKey>>() {
    }).in(Scopes.SINGLETON);


    // ----------------------------------------

    bind(new TypeLiteral<ObjectRowConverter<AutoId>>() {
    }).to(AutoIdObjectConverter.class).in(Singleton.class);

    bind(new TypeLiteral<CommonReadDao<AutoId, String>>() {
    }).to(new TypeLiteral<CommonDao<AutoId, String>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<CommonDao<AutoId, String>>() {
    }).to(new TypeLiteral<com.smartitengineering.dao.impl.hbase.CommonDao<AutoId, String>>() {
    }).in(Singleton.class);


    bind(new TypeLiteral<CommonWriteDao<AutoId>>() {
    }).to(new TypeLiteral<CommonDao<AutoId, String>>() {
    }).in(Singleton.class);

    final TypeLiteral<SchemaInfoProviderImpl<AutoId, String>> autoIdTypeLiteral = new TypeLiteral<SchemaInfoProviderImpl<AutoId, String>>() {
    };
    bind(new TypeLiteral<FilterConfigs<AutoId>>() {
    }).toProvider(new GenericFilterConfigsProvider<AutoId>(
        "com/smartitengineering/user/service/impl/hbase/config/AutoIdFilterConfigs.json")).in(Scopes.SINGLETON);
    bind(new TypeLiteral<SchemaInfoProviderBaseConfig<AutoId>>() {
    }).toProvider(new GenericBaseConfigProvider<AutoId>(
        "com/smartitengineering/user/service/impl/hbase/config/AutoIdSchemaBaseConfig.json")).in(Scopes.SINGLETON);

    bind(new TypeLiteral<Class<AutoId>>() {
    }).toInstance(AutoId.class);
    bind(new TypeLiteral<SchemaInfoProvider<AutoId, String>>() {
    }).to(autoIdTypeLiteral).in(Singleton.class);

    bind(new TypeLiteral<MergeService<AutoId, String>>() {
    }).to(new TypeLiteral<DiffBasedMergeService<AutoId, String>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<LockAttainer<AutoId, String>>() {
    }).to(new TypeLiteral<LockAttainerImpl<AutoId, String>>() {
    }).in(Scopes.SINGLETON);

    //-----------------------------    
    bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);


    /*
     * Start injection specific to common dao of Role
     */
    bind(new TypeLiteral<ObjectRowConverter<Role>>() {
    }).to(RoleObjectConverter.class).in(Singleton.class);
    bind(new TypeLiteral<CommonReadDao<Role, Long>>() {
    }).to(new TypeLiteral<CommonDao<Role, Long>>() {
    }).in(Singleton.class);

    /*
     * Configure write dao
     */
    bind(new TypeLiteral<CommonWriteDao<Role>>() {
    }).annotatedWith(Names.named("searchWriteDaoDecoratee")).to(new TypeLiteral<CommonDao<Role, Long>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<CommonWriteDao<Role>>() {
    }).to(new TypeLiteral<CommonWriteDaoDecorator<Role>>() {
    }).in(Singleton.class);
    TypeLiteral<CommonFreeTextPersistentDao<Role>> roleLit =
                                                   new TypeLiteral<CommonFreeTextPersistentDao<Role>>() {
    };
    bind(roleLit).to(new TypeLiteral<CommonAsyncFreeTextPersistentDaoImpl<Role>>() {
    }).in(Scopes.SINGLETON);
    bind(roleLit).annotatedWith(Names.named("primaryFreeTextPersistentDao")).to(new TypeLiteral<SolrFreeTextPersistentDao<Role>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<ObjectIdentifierQuery<Role>>() {
    }).to(RoleIdentifierQueryImpl.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<GenericAdapter<Role, MultivalueMap<String, Object>>>() {
    }).to(new TypeLiteral<GenericAdapterImpl<Role, MultivalueMap<String, Object>>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<AbstractAdapterHelper<Role, MultivalueMap<String, Object>>>() {
    }).to(RoleAdapterHelper.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<CommonFreeTextSearchDao<Role>>() {
    }).to(new TypeLiteral<SolrFreeTextSearchDao<Role>>() {
    }).in(Scopes.SINGLETON);

    bind(new TypeLiteral<CommonDao<Role, Long>>() {
    }).to(new TypeLiteral<com.smartitengineering.dao.impl.hbase.CommonDao<Role, Long>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<LockAttainer<Role, Long>>() {
    }).to(new TypeLiteral<LockAttainerImpl<Role, Long>>() {
    }).in(Scopes.SINGLETON);

    final TypeLiteral<SchemaInfoProviderImpl<Role, Long>> roleTypeLiteral = new TypeLiteral<SchemaInfoProviderImpl<Role, Long>>() {
    };
    bind(new TypeLiteral<FilterConfigs<Role>>() {
    }).toProvider(new GenericFilterConfigsProvider<Role>(
        "com/smartitengineering/user/service/impl/hbase/config/RoleFilterConfigs.json")).in(Scopes.SINGLETON);
    bind(new TypeLiteral<SchemaInfoProviderBaseConfig<Role>>() {
    }).toProvider(new GenericBaseConfigProvider<Role>(
        "com/smartitengineering/user/service/impl/hbase/config/RoleSchemaBaseConfig.json")).in(Scopes.SINGLETON);

    bind(new TypeLiteral<Class<Long>>() {
    }).toInstance(Long.class);
    bind(new TypeLiteral<SchemaInfoProvider<Role, Long>>() {
    }).to(roleTypeLiteral).in(Singleton.class);

    bind(new TypeLiteral<MergeService<Role, Long>>() {
    }).to(new TypeLiteral<DiffBasedMergeService<Role, Long>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<RowCellIncrementor<Role, AutoId, String>>() {
    }).to(new TypeLiteral<RowCellIncrementorImpl<Role, AutoId, String>>() {
    });
    CellConfigImpl<Role> roleConfigImpl = new CellConfigImpl<Role>();
    roleConfigImpl.setFamily("self");
    roleConfigImpl.setQualifier("roleId");

    bind(new TypeLiteral<CellConfig<Role>>() {
    }).toInstance(roleConfigImpl);
    bind(RoleService.class).to(RoleServiceImpl.class).in(Singleton.class);


    /*
     * Start injection specific to common dao of Privilege
     */
    bind(new TypeLiteral<ObjectRowConverter<Privilege>>() {
    }).to(PrivilegeObjectConverter.class).in(Singleton.class);
    bind(new TypeLiteral<CommonReadDao<Privilege, Long>>() {
    }).to(new TypeLiteral<CommonDao<Privilege, Long>>() {
    }).in(Singleton.class);

    /*
     * Configure write dao
     */
    bind(new TypeLiteral<CommonWriteDao<Privilege>>() {
    }).annotatedWith(Names.named("searchWriteDaoDecoratee")).to(new TypeLiteral<CommonDao<Privilege, Long>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<CommonWriteDao<Privilege>>() {
    }).to(new TypeLiteral<CommonWriteDaoDecorator<Privilege>>() {
    }).in(Singleton.class);
    TypeLiteral<CommonFreeTextPersistentDao<Privilege>> privilegeLit =
                                                        new TypeLiteral<CommonFreeTextPersistentDao<Privilege>>() {
    };
    bind(privilegeLit).to(new TypeLiteral<CommonAsyncFreeTextPersistentDaoImpl<Privilege>>() {
    }).in(Scopes.SINGLETON);
    bind(privilegeLit).annotatedWith(Names.named("primaryFreeTextPersistentDao")).to(new TypeLiteral<SolrFreeTextPersistentDao<Privilege>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<ObjectIdentifierQuery<Privilege>>() {
    }).to(PrivilegeIdentifierQueryImpl.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<GenericAdapter<Privilege, MultivalueMap<String, Object>>>() {
    }).to(new TypeLiteral<GenericAdapterImpl<Privilege, MultivalueMap<String, Object>>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<AbstractAdapterHelper<Privilege, MultivalueMap<String, Object>>>() {
    }).to(PrivilegeAdapterHelper.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<CommonFreeTextSearchDao<Privilege>>() {
    }).to(new TypeLiteral<SolrFreeTextSearchDao<Privilege>>() {
    }).in(Scopes.SINGLETON);

    bind(new TypeLiteral<CommonDao<Privilege, Long>>() {
    }).to(new TypeLiteral<com.smartitengineering.dao.impl.hbase.CommonDao<Privilege, Long>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<LockAttainer<Privilege, Long>>() {
    }).to(new TypeLiteral<LockAttainerImpl<Privilege, Long>>() {
    }).in(Scopes.SINGLETON);

    final TypeLiteral<SchemaInfoProviderImpl<Privilege, Long>> privilegeTypeLiteral = new TypeLiteral<SchemaInfoProviderImpl<Privilege, Long>>() {
    };

    bind(new TypeLiteral<FilterConfigs<Privilege>>() {
    }).toProvider(new GenericFilterConfigsProvider<Privilege>(
        "com/smartitengineering/user/service/impl/hbase/config/PrivilegeFilterConfigs.json")).in(Scopes.SINGLETON);
    bind(new TypeLiteral<SchemaInfoProviderBaseConfig<Privilege>>() {
    }).toProvider(new GenericBaseConfigProvider<Privilege>(
        "com/smartitengineering/user/service/impl/hbase/config/PrivilegeSchemaBaseConfig.json")).in(Scopes.SINGLETON);
    

//    bind(new TypeLiteral<Class<Long>>() {
//    }).toInstance(Long.class);
    bind(new TypeLiteral<SchemaInfoProvider<Privilege, Long>>() {
    }).to(privilegeTypeLiteral).in(Singleton.class);

    bind(new TypeLiteral<MergeService<Privilege, Long>>() {
    }).to(new TypeLiteral<DiffBasedMergeService<Privilege, Long>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<RowCellIncrementor<Privilege, AutoId, String>>() {
    }).to(new TypeLiteral<RowCellIncrementorImpl<Privilege, AutoId, String>>() {
    });
    CellConfigImpl<Privilege> privilegeConfigImpl = new CellConfigImpl<Privilege>();
    privilegeConfigImpl.setFamily("self");
    privilegeConfigImpl.setQualifier("privilegeId");

    bind(new TypeLiteral<CellConfig<Privilege>>() {
    }).toInstance(privilegeConfigImpl);

    bind(PrivilegeService.class).to(PrivilegeServiceImpl.class).in(Singleton.class);

    /*
     * Start injection specific to common dao of SecuredObject
     */
    bind(new TypeLiteral<ObjectRowConverter<SecuredObject>>() {
    }).to(SecuredObjectConverter.class).in(Singleton.class);
    bind(new TypeLiteral<CommonReadDao<SecuredObject, Long>>() {
    }).to(new TypeLiteral<CommonDao<SecuredObject, Long>>() {
    }).in(Singleton.class);

    /*
     * Configure write dao
     */
    bind(new TypeLiteral<CommonWriteDao<SecuredObject>>() {
    }).annotatedWith(Names.named("searchWriteDaoDecoratee")).to(new TypeLiteral<CommonDao<SecuredObject, Long>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<CommonWriteDao<SecuredObject>>() {
    }).to(new TypeLiteral<CommonWriteDaoDecorator<SecuredObject>>() {
    }).in(Singleton.class);
    TypeLiteral<CommonFreeTextPersistentDao<SecuredObject>> securedObjectLit =
                                                            new TypeLiteral<CommonFreeTextPersistentDao<SecuredObject>>() {
    };
    bind(securedObjectLit).to(new TypeLiteral<CommonAsyncFreeTextPersistentDaoImpl<SecuredObject>>() {
    }).in(Scopes.SINGLETON);
    bind(securedObjectLit).annotatedWith(Names.named("primaryFreeTextPersistentDao")).to(new TypeLiteral<SolrFreeTextPersistentDao<SecuredObject>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<ObjectIdentifierQuery<SecuredObject>>() {
    }).to(SecuredObjectIdentifierQueryImpl.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<GenericAdapter<SecuredObject, MultivalueMap<String, Object>>>() {
    }).to(new TypeLiteral<GenericAdapterImpl<SecuredObject, MultivalueMap<String, Object>>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<AbstractAdapterHelper<SecuredObject, MultivalueMap<String, Object>>>() {
    }).to(SecuredObjectAdapterHelper.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<CommonFreeTextSearchDao<SecuredObject>>() {
    }).to(new TypeLiteral<SolrFreeTextSearchDao<SecuredObject>>() {
    }).in(Scopes.SINGLETON);

    bind(new TypeLiteral<CommonDao<SecuredObject, Long>>() {
    }).to(new TypeLiteral<com.smartitengineering.dao.impl.hbase.CommonDao<SecuredObject, Long>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<LockAttainer<SecuredObject, Long>>() {
    }).to(new TypeLiteral<LockAttainerImpl<SecuredObject, Long>>() {
    }).in(Scopes.SINGLETON);

    final TypeLiteral<SchemaInfoProviderImpl<SecuredObject, Long>> securedObjectTypeLiteral = new TypeLiteral<SchemaInfoProviderImpl<SecuredObject, Long>>() {
    };
    bind(new TypeLiteral<FilterConfigs<SecuredObject>>() {
    }).toProvider(new GenericFilterConfigsProvider<SecuredObject>(
        "com/smartitengineering/user/service/impl/hbase/config/SecuredObjectFilterConfigs.json")).in(Scopes.SINGLETON);
    bind(new TypeLiteral<SchemaInfoProviderBaseConfig<SecuredObject>>() {
    }).toProvider(new GenericBaseConfigProvider<SecuredObject>(
        "com/smartitengineering/user/service/impl/hbase/config/SecuredObjectSchemaBaseConfig.json")).in(Scopes.SINGLETON);
//    bind(new TypeLiteral<Class<Long>>() {
//    }).toInstance(Long.class);
    bind(new TypeLiteral<SchemaInfoProvider<SecuredObject, Long>>() {
    }).to(securedObjectTypeLiteral).in(Singleton.class);

    bind(new TypeLiteral<MergeService<SecuredObject, Long>>() {
    }).to(new TypeLiteral<DiffBasedMergeService<SecuredObject, Long>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<RowCellIncrementor<SecuredObject, AutoId, String>>() {
    }).to(new TypeLiteral<RowCellIncrementorImpl<SecuredObject, AutoId, String>>() {
    });
    CellConfigImpl<SecuredObject> securedObjectConfigImpl = new CellConfigImpl<SecuredObject>();
    securedObjectConfigImpl.setFamily("self");
    securedObjectConfigImpl.setQualifier("securedObjectId");

    bind(new TypeLiteral<CellConfig<SecuredObject>>() {
    }).toInstance(securedObjectConfigImpl);

    bind(SecuredObjectService.class).to(SecuredObjectServiceImpl.class).in(Singleton.class);
    /*
     * Start injection specific to common dao of Person
     */
    bind(new TypeLiteral<ObjectRowConverter<Person>>() {
    }).to(PersonObjectConverter.class).in(Singleton.class);
    bind(new TypeLiteral<CommonReadDao<Person, Long>>() {
    }).to(new TypeLiteral<CommonDao<Person, Long>>() {
    }).in(Singleton.class);

    /*
     * Configure write dao
     */
    bind(new TypeLiteral<CommonWriteDao<Person>>() {
    }).annotatedWith(Names.named("searchWriteDaoDecoratee")).to(new TypeLiteral<CommonDao<Person, Long>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<CommonWriteDao<Person>>() {
    }).to(new TypeLiteral<CommonWriteDaoDecorator<Person>>() {
    }).in(Singleton.class);
    TypeLiteral<CommonFreeTextPersistentDao<Person>> personLit =
                                                     new TypeLiteral<CommonFreeTextPersistentDao<Person>>() {
    };
    bind(personLit).to(new TypeLiteral<CommonAsyncFreeTextPersistentDaoImpl<Person>>() {
    }).in(Scopes.SINGLETON);
    bind(personLit).annotatedWith(Names.named("primaryFreeTextPersistentDao")).to(new TypeLiteral<SolrFreeTextPersistentDao<Person>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<ObjectIdentifierQuery<Person>>() {
    }).to(PersonIdentifierQueryImpl.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<GenericAdapter<Person, MultivalueMap<String, Object>>>() {
    }).to(new TypeLiteral<GenericAdapterImpl<Person, MultivalueMap<String, Object>>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<AbstractAdapterHelper<Person, MultivalueMap<String, Object>>>() {
    }).to(PersonAdapterHelper.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<CommonFreeTextSearchDao<Person>>() {
    }).to(new TypeLiteral<SolrFreeTextSearchDao<Person>>() {
    }).in(Scopes.SINGLETON);

    bind(new TypeLiteral<CommonDao<Person, Long>>() {
    }).to(new TypeLiteral<com.smartitengineering.dao.impl.hbase.CommonDao<Person, Long>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<LockAttainer<Person, Long>>() {
    }).to(new TypeLiteral<LockAttainerImpl<Person, Long>>() {
    }).in(Scopes.SINGLETON);

    final TypeLiteral<SchemaInfoProviderImpl<Person, Long>> personTypeLiteral = new TypeLiteral<SchemaInfoProviderImpl<Person, Long>>() {
    };
    bind(new TypeLiteral<FilterConfigs<Person>>() {
    }).toProvider(new GenericFilterConfigsProvider<Person>(
        "com/smartitengineering/user/service/impl/hbase/config/PersonFilterConfigs.json")).in(Scopes.SINGLETON);
    bind(new TypeLiteral<SchemaInfoProviderBaseConfig<Person>>() {
    }).toProvider(new GenericBaseConfigProvider<Person>(
        "com/smartitengineering/user/service/impl/hbase/config/PersonSchemaBaseConfig.json")).in(Scopes.SINGLETON);

//    bind(new TypeLiteral<Class<Long>>() {
//    }).toInstance(Long.class);
    bind(new TypeLiteral<SchemaInfoProvider<Person, Long>>() {
    }).to(personTypeLiteral).in(Singleton.class);

    bind(new TypeLiteral<MergeService<Person, Long>>() {
    }).to(new TypeLiteral<DiffBasedMergeService<Person, Long>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<RowCellIncrementor<Person, AutoId, String>>() {
    }).to(new TypeLiteral<RowCellIncrementorImpl<Person, AutoId, String>>() {
    });
    CellConfigImpl<Person> personConfigImpl = new CellConfigImpl<Person>();
    personConfigImpl.setFamily("self");
    personConfigImpl.setQualifier("personId");

    bind(new TypeLiteral<CellConfig<Person>>() {
    }).toInstance(personConfigImpl);
    bind(PersonService.class).to(PersonServiceImpl.class).in(Singleton.class);


    /*
     * Start injection specific to common dao of UserPerson
     */
    bind(new TypeLiteral<ObjectRowConverter<UserPerson>>() {
    }).to(UserPersonObjectConverter.class).in(Singleton.class);
    bind(new TypeLiteral<CommonReadDao<UserPerson, Long>>() {
    }).to(new TypeLiteral<CommonDao<UserPerson, Long>>() {
    }).in(Singleton.class);

    /*
     * Configure write dao
     */
    bind(new TypeLiteral<CommonWriteDao<UserPerson>>() {
    }).annotatedWith(Names.named("searchWriteDaoDecoratee")).to(new TypeLiteral<CommonDao<UserPerson, Long>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<CommonWriteDao<UserPerson>>() {
    }).to(new TypeLiteral<CommonWriteDaoDecorator<UserPerson>>() {
    }).in(Singleton.class);
    TypeLiteral<CommonFreeTextPersistentDao<UserPerson>> userPersonLit =
                                                         new TypeLiteral<CommonFreeTextPersistentDao<UserPerson>>() {
    };
    bind(userPersonLit).to(new TypeLiteral<CommonAsyncFreeTextPersistentDaoImpl<UserPerson>>() {
    }).in(Scopes.SINGLETON);
    bind(userPersonLit).annotatedWith(Names.named("primaryFreeTextPersistentDao")).to(new TypeLiteral<SolrFreeTextPersistentDao<UserPerson>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<ObjectIdentifierQuery<UserPerson>>() {
    }).to(UserPersonIdentifierQueryImpl.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<GenericAdapter<UserPerson, MultivalueMap<String, Object>>>() {
    }).to(new TypeLiteral<GenericAdapterImpl<UserPerson, MultivalueMap<String, Object>>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<AbstractAdapterHelper<UserPerson, MultivalueMap<String, Object>>>() {
    }).to(UserPersonAdapterHelper.class).in(Scopes.SINGLETON);
    bind(new TypeLiteral<CommonFreeTextSearchDao<UserPerson>>() {
    }).to(new TypeLiteral<SolrFreeTextSearchDao<UserPerson>>() {
    }).in(Scopes.SINGLETON);

    bind(new TypeLiteral<CommonDao<UserPerson, Long>>() {
    }).to(new TypeLiteral<com.smartitengineering.dao.impl.hbase.CommonDao<UserPerson, Long>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<LockAttainer<UserPerson, Long>>() {
    }).to(new TypeLiteral<LockAttainerImpl<UserPerson, Long>>() {
    }).in(Scopes.SINGLETON);

    final TypeLiteral<SchemaInfoProviderImpl<UserPerson, Long>> userPersonTypeLiteral = new TypeLiteral<SchemaInfoProviderImpl<UserPerson, Long>>() {
    };
    bind(new TypeLiteral<FilterConfigs<UserPerson>>() {
    }).toProvider(new GenericFilterConfigsProvider<UserPerson>(
        "com/smartitengineering/user/service/impl/hbase/config/UserPersonFilterConfigs.json")).in(Scopes.SINGLETON);
    bind(new TypeLiteral<SchemaInfoProviderBaseConfig<UserPerson>>() {
    }).toProvider(new GenericBaseConfigProvider<UserPerson>(
        "com/smartitengineering/user/service/impl/hbase/config/UserPersonSchemaBaseConfig.json")).in(Scopes.SINGLETON);

//    bind(new TypeLiteral<Class<Long>>() {
//    }).toInstance(Long.class);
    bind(new TypeLiteral<SchemaInfoProvider<UserPerson, Long>>() {
    }).to(userPersonTypeLiteral).in(Singleton.class);

    bind(new TypeLiteral<MergeService<UserPerson, Long>>() {
    }).to(new TypeLiteral<DiffBasedMergeService<UserPerson, Long>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<RowCellIncrementor<UserPerson, AutoId, String>>() {
    }).to(new TypeLiteral<RowCellIncrementorImpl<UserPerson, AutoId, String>>() {
    });
    CellConfigImpl<UserPerson> userPersonConfigImpl = new CellConfigImpl<UserPerson>();
    userPersonConfigImpl.setFamily("self");
    userPersonConfigImpl.setQualifier("userPersonId");

    bind(new TypeLiteral<CellConfig<UserPerson>>() {
    }).toInstance(userPersonConfigImpl);
    bind(UserPersonService.class).to(UserPersonServiceImpl.class).in(Singleton.class);

    /*
     * Start injection specific to common dao of UserGroup
     */

    bind(new TypeLiteral<ObjectRowConverter<UserGroup>>() {
    }).to(UserGroupObjectConverter.class).in(Singleton.class);
    bind(new TypeLiteral<CommonReadDao<UserGroup, Long>>() {
    }).to(new TypeLiteral<CommonDao<UserGroup, Long>>() {
    }).in(Singleton.class);

    /*
     * Configure write dao
     */
    bind(new TypeLiteral<CommonWriteDao<UserGroup>>() {
    }).annotatedWith(Names.named("searchWriteDaoDecoratee")).to(new TypeLiteral<CommonDao<UserGroup, Long>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<CommonWriteDao<UserGroup>>() {
    }).to(new TypeLiteral<CommonWriteDaoDecorator<UserGroup>>() {
    }).in(Singleton.class);
    TypeLiteral<CommonFreeTextPersistentDao<UserGroup>> userGroupLit =
                                                        new TypeLiteral<CommonFreeTextPersistentDao<UserGroup>>() {
    };
    bind(userGroupLit).to(new TypeLiteral<CommonAsyncFreeTextPersistentDaoImpl<UserGroup>>() {
    }).in(Scopes.SINGLETON);
    bind(userGroupLit).annotatedWith(Names.named("primaryFreeTextPersistentDao")).to(new TypeLiteral<SolrFreeTextPersistentDao<UserGroup>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<ObjectIdentifierQuery<UserGroup>>() {
    }).to(UserGroupIdentifierQueryimpl.class).in(Scopes.SINGLETON);

    bind(new TypeLiteral<GenericAdapter<UserGroup, MultivalueMap<String, Object>>>() {
    }).to(new TypeLiteral<GenericAdapterImpl<UserGroup, MultivalueMap<String, Object>>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<AbstractAdapterHelper<UserGroup, MultivalueMap<String, Object>>>() {
    }).to(UserGroupAdapterHelper.class).in(Scopes.SINGLETON);

    bind(new TypeLiteral<GenericAdapter<UserGroup, List<MultivalueMap<String, Object>>>>() {
    }).to(new TypeLiteral<GenericAdapterImpl<UserGroup, List<MultivalueMap<String, Object>>>>() {
    }).in(Scopes.SINGLETON);
    bind(new TypeLiteral<AbstractAdapterHelper<UserGroup, List<MultivalueMap<String, Object>>>>() {
    }).to(UserGroupMultiAdapterHelper.class).in(Scopes.SINGLETON);


    bind(new TypeLiteral<CommonFreeTextSearchDao<UserGroup>>() {
    }).to(new TypeLiteral<SolrFreeTextSearchDao<UserGroup>>() {
    }).in(Scopes.SINGLETON);

    bind(new TypeLiteral<CommonDao<UserGroup, Long>>() {
    }).to(new TypeLiteral<com.smartitengineering.dao.impl.hbase.CommonDao<UserGroup, Long>>() {
    }).in(Singleton.class);

    bind(new TypeLiteral<LockAttainer<UserGroup, Long>>() {
    }).to(new TypeLiteral<LockAttainerImpl<UserGroup, Long>>() {
    }).in(Scopes.SINGLETON);

    final TypeLiteral<SchemaInfoProviderImpl<UserGroup, Long>> userGroupTypeLiteral = new TypeLiteral<SchemaInfoProviderImpl<UserGroup, Long>>() {
    };
    bind(new TypeLiteral<FilterConfigs<UserGroup>>() {
    }).toProvider(new GenericFilterConfigsProvider<UserGroup>(
        "com/smartitengineering/service/impl/hbase/config/UserGroupFilterConfigs.json")).in(Scopes.SINGLETON);
    bind(new TypeLiteral<SchemaInfoProviderBaseConfig<UserGroup>>() {
    }).toProvider(new GenericBaseConfigProvider<UserGroup>(
        "com/smartitengineering/service/impl/hbase/config/ UserGroupSchemaBaseConfig.json")).in(Scopes.SINGLETON);
//    bind(new TypeLiteral<Class<Long>>() {
//    }).toInstance(Long.class);
    bind(new TypeLiteral<SchemaInfoProvider<UserGroup, Long>>() {
    }).to(userGroupTypeLiteral).in(Singleton.class);

    bind(new TypeLiteral<MergeService<UserGroup, Long>>() {
    }).to(new TypeLiteral<DiffBasedMergeService<UserGroup, Long>>() {
    }).in(Singleton.class);
    bind(new TypeLiteral<RowCellIncrementor<UserGroup, AutoId, String>>() {
    }).to(new TypeLiteral<RowCellIncrementorImpl<UserGroup, AutoId, String>>() {
    });
    CellConfigImpl<UserGroup> userGroupConfigImpl = new CellConfigImpl<UserGroup>();
    userGroupConfigImpl.setFamily("self");
    userGroupConfigImpl.setQualifier("userGroupId");

    bind(new TypeLiteral<CellConfig<UserGroup>>() {
    }).toInstance(userGroupConfigImpl);

    bind(UserGroupService.class).to(UserGroupServiceImpl.class).in(Singleton.class);
  }
}
