package ru.softlab.efr.services.test.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author khudyakov
 * @since 28.07.2017
 */
@Configuration
@EnableTransactionManagement
@PropertySource(value = "classpath:hibernate.properties", ignoreResourceNotFound = true)
@EnableJpaRepositories("ru.softlab.efr.services.auth.services.impl.dao.repositories")
public class HibernateConfig {

    @Autowired
    private Environment env;

    @SuppressWarnings("Duplicates")
    @Bean
    @Primary
    public DataSource dataSource() throws ClassNotFoundException {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(env.getProperty("hibernate.connection.driver_class"));
        driver.setUrl(env.getProperty("hibernate.connection.url"));
        driver.setUsername(env.getProperty("hibernate.connection.username"));
        driver.setPassword(env.getProperty("hibernate.connection.password"));
        return driver;
    }

    @SuppressWarnings("Duplicates")
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) throws IOException {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("ru.softlab.efr.services.auth.model");
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setJpaProperties(jpaPropertiesFactoryBean().getObject());
        return entityManagerFactoryBean;
    }

    @Bean
    public PropertiesFactoryBean jpaPropertiesFactoryBean(){
        PropertiesFactoryBean propertiesFactory = new PropertiesFactoryBean();
        propertiesFactory.setLocation(new ClassPathResource("hibernate.properties"));
        return propertiesFactory;
    }

    @Bean
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }
}
