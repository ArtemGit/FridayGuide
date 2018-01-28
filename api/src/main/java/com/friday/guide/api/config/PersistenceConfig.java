package com.friday.guide.api.config;

import com.friday.guide.api.data.entity.audit.AuditorAwareImpl;
import com.friday.guide.api.data.entity.audit.IdentifiedNamedEntity;
import net.sf.ehcache.config.CacheConfiguration;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.hibernate.HibernateException;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.SmartPersistenceUnitInfo;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableCaching
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class PersistenceConfig {

    public static final String ENTITY_PACKAGE = "com.friday.guide.api.data.entity";

    @Value("${dataSource.driver}")
    private String driver;
    @Value("${dataSource.url}")
    private String url;
    @Value("${dataSource.username}")
    private String username;
    @Value("${dataSource.password}")
    private String password;
    @Value("${dataSource.hbm2ddlAuto}")
    private String hbm2ddlAuto;
    @Value("${dataSource.hibernateShowSql}")
    private String hibernateShowSql;
    @Value("${dataSource.dialect}")
    private String dialect;
    @Value("${dataSource.dbcp.maxTotalConnections}")
    private int maxTotalConnections;
    @Value("${dataSource.dbcp.maxIdleConnections}")
    private int maxIdleConnections;
    @Value("${flyway.migration.locations}")
    private String[] migrations;
    @Value("#{'${APP_BASE_DIR}'}")
    private String generatedScriptLocation;

    private org.hibernate.cfg.Configuration configuration;

    @Bean
    AuditorAware<IdentifiedNamedEntity> auditorProvider() {
        return new AuditorAwareImpl();
    }

    @Bean
    public DataSource dataSource() {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxTotal(maxTotalConnections);
        dataSource.setMaxIdle(maxIdleConnections);
        dataSource.setPoolPreparedStatements(true);
        return dataSource;
    }

    @Bean
    @Autowired
    @DependsOn({"ehCacheManager", "flyway"})
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(Flyway flyway) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(Boolean.TRUE);
        vendorAdapter.setShowSql(Boolean.valueOf(hibernateShowSql));
        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan(ENTITY_PACKAGE);
        factory.setPersistenceProvider(new SpringHibernateJpaPersistenceProvider0());
        Properties jpaProperties = new Properties();
        jpaProperties.put(Environment.HBM2DDL_AUTO, hbm2ddlAuto);
        jpaProperties.put(Environment.DIALECT, dialect);
        jpaProperties.put(Environment.CACHE_REGION_FACTORY, SingletonEhCacheRegionFactory.class.getCanonicalName());
        factory.setJpaProperties(jpaProperties);
        try {
            factory.afterPropertiesSet();
        } catch (HibernateException e) {
            this.writeUpdateScript(flyway);
            throw e;
        }
        return factory;
    }

    @Autowired
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Autowired
    @Bean(name = "flyway", initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations(migrations);
        flyway.setOutOfOrder(true);
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersionAsString("0");
        flyway.setPlaceholderPrefix("##${");
        flyway.setPlaceholderSuffix("}");
        flyway.setClassLoader(this.getClass().getClassLoader());
        flyway.repair();
        return flyway;
    }

    @Bean(destroyMethod = "shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("default");
        cacheConfiguration.setMaxEntriesLocalHeap(100_000);
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addDefaultCache(cacheConfiguration);
        return net.sf.ehcache.CacheManager.create(config);
    }

    @Bean
    @Autowired
    public CacheManager cacheManager(net.sf.ehcache.CacheManager manager) {
        return new EhCacheCacheManager(manager);
    }

    @Bean
    @Autowired
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        final TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(transactionManager);
        return transactionTemplate;
    }

    @SuppressWarnings("unchecked")
    protected void writeUpdateScript(final Flyway flyway) {
        if (configuration == null) return;
        DatasourceConnectionProviderImpl connectionProvider = new DatasourceConnectionProviderImpl();
        connectionProvider.configure(new LinkedHashMap<String, Object>() {
            {
                put(Environment.DATASOURCE, dataSource());
            }
        });
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(configuration.getProperties())
            .addService(ConnectionProvider.class, connectionProvider)
            .build();
        SchemaUpdate su = new SchemaUpdate(serviceRegistry, configuration);
        try {
            File output = new File(generatedScriptLocation);
            if (!output.isDirectory()) {
                output.mkdirs();
            }
            MigrationVersion migrationVersion = flyway.info().current().getVersion();
            StringBuilder outputNameBuilder = new StringBuilder();
            outputNameBuilder.append(flyway.getSqlMigrationPrefix());
            String[] numParts = StringUtils.split(migrationVersion.getVersion(), ".");
            for (int i = 0; i < numParts.length; ++i) {
                if (i != numParts.length - 1) {
                    outputNameBuilder.append(numParts[i]);
                    outputNameBuilder.append("_");
                } else {
                    outputNameBuilder.append(Integer.valueOf(numParts[i]) + 1);
                }
            }
            outputNameBuilder
                .append(flyway.getSqlMigrationSeparator())
                .append(System.currentTimeMillis())
                .append(flyway.getSqlMigrationSuffix());
            output = new File(output, outputNameBuilder.toString());
            su.setHaltOnError(true);
            su.setOutputFile(output.getAbsolutePath());
            su.setDelimiter(";");
            su.setFormat(true);
            su.execute(true, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class SpringHibernateJpaPersistenceProvider0 extends HibernatePersistenceProvider {

        @Override
        public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {
            return new EntityManagerFactoryBuilderImpl(new PersistenceUnitInfoDescriptor(info), properties) {
                @Override
                public org.hibernate.cfg.Configuration buildHibernateConfiguration(ServiceRegistry serviceRegistry) {
                    org.hibernate.cfg.Configuration configuration = super.buildHibernateConfiguration(serviceRegistry);
                    if (info instanceof SmartPersistenceUnitInfo) {
                        for (String managedPackage : ((SmartPersistenceUnitInfo) info).getManagedPackages()) {
                            configuration.addPackage(managedPackage);
                        }
                    }
                    if (PersistenceConfig.this.configuration == null) {
                        PersistenceConfig.this.configuration = configuration;
                    }
                    return configuration;
                }
            }.build();
        }
    }
}
