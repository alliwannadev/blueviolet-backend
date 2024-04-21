package com.blueviolet.backend.common.config;

import com.blueviolet.backend.common.annotation.SecondaryDatabase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Profile({"prod"})
@EnableJpaRepositories(
        basePackages = "com.blueviolet.backend",
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ANNOTATION,
                        classes = SecondaryDatabase.class
                )
        },
        entityManagerFactoryRef = "secondaryEntityManagerFactory",
        transactionManagerRef = "secondaryTransactionManager"
)
@EnableTransactionManagement
@RequiredArgsConstructor
@Configuration
public class SecondaryDataSourceConfig {

    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-second.hikari")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(secondaryDataSource());
        entityManagerFactory.setPackagesToScan("com.blueviolet.backend");
        entityManagerFactory.setPersistenceUnitName("secondaryPersistenceUnit");
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Map<String, Object> properties =
                hibernateProperties.determineHibernateProperties(
                        jpaProperties.getProperties(),
                        new HibernateSettings()
                );
        entityManagerFactory.setJpaPropertyMap(properties);

        return entityManagerFactory;
    }

    @Bean
    public PlatformTransactionManager secondaryTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(secondaryEntityManagerFactory().getObject());

        return transactionManager;
    }
}
