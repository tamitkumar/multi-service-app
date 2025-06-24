package com.user.module.config;

import com.user.module.exception.UserSystemException;
import com.user.module.utils.UserConstant;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.user.module.repository", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "platformTransactionManager")
public class MySQLConfig {
    @Bean
    DataSource dataSource() {
        String dbUser = UserConstant.DB_USER_NAME;
        String dbPassword = UserConstant.DB_PASSWORD;
        String driverClassName = UserConstant.DB_DRIVER_CLASS_NAME;
        DriverManagerDataSource ds = new DriverManagerDataSource(getDBUrl(), dbUser, dbPassword);
        try {
            ds.setDriverClassName(driverClassName);
        } catch (Exception e) {
            throw new UserSystemException("ERR002", "DB ISSUE");
        }
        try {
            ds.getConnection().close();
        } catch (SQLException e) {
            throw new UserSystemException(e);
        }
        return ds;
    }

    private String getDBUrl() {
        String dbHost = UserConstant.DB_HOST;
        String dbPort = UserConstant.DB_PORT;
        String dbName = UserConstant.DB_NAME;
        String dbUrlPrefix = UserConstant.DB_URL_PREFIX;
        //		baseUrl.append(EMPConstant.COLON);
        return dbUrlPrefix + dbHost +
                UserConstant.COLON +
                dbPort +
                dbName;
    }

    @Bean
    JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    PlatformTransactionManager platformTransactionManager(EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.user.module.model");
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        factoryBean.setJpaProperties(jpaProperties());
        return factoryBean;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put(UserConstant.DIALECT_KEY, UserConstant.DIALECT_VALUE);
        properties.put("hibernate.dialect", "com.user.module.config.DialectConfig");
        properties.put(UserConstant.SHOW_SQL_KEY, UserConstant.SHOW_SQL_VALUE);
        properties.put(UserConstant.FORMAT_SQL_KEY, UserConstant.FORMAT_SQL_VALUE);
        properties.put("spring.jpa.hibernate.ddl-auto", "update");
        return properties;
    }

}

