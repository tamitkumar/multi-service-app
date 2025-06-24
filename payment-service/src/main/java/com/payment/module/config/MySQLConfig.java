package com.payment.module.config;

import com.payment.module.exception.PaymentSystemException;
import com.payment.module.utils.PaymentConstant;
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
@EnableJpaRepositories(basePackages = "com.payment.module.repository", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "platformTransactionManager")
public class MySQLConfig {
    @Bean
    DataSource dataSource() {
        String dbUser = PaymentConstant.DB_USER_NAME;
        String dbPassword = PaymentConstant.DB_PASSWORD;
        String driverClassName = PaymentConstant.DB_DRIVER_CLASS_NAME;
        DriverManagerDataSource ds = new DriverManagerDataSource(getDBUrl(), dbUser, dbPassword);
        try {
            ds.setDriverClassName(driverClassName);
        } catch (Exception e) {
            throw new PaymentSystemException("ERR002", "DB ISSUE");
        }
        try {
            ds.getConnection().close();
        } catch (SQLException e) {
            throw new PaymentSystemException(e);
        }
        return ds;
    }

    private String getDBUrl() {
        String dbHost = PaymentConstant.DB_HOST;
        String dbPort = PaymentConstant.DB_PORT;
        String dbName = PaymentConstant.DB_NAME;
        String dbUrlPrefix = PaymentConstant.DB_URL_PREFIX;
        //		baseUrl.append(EMPConstant.COLON);
        return dbUrlPrefix + dbHost +
                PaymentConstant.COLON +
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
        factoryBean.setPackagesToScan("com.payment.module.entity");
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        factoryBean.setJpaProperties(jpaProperties());
        return factoryBean;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put(PaymentConstant.DIALECT_KEY, PaymentConstant.DIALECT_VALUE);
        properties.put("hibernate.dialect", "com.payment.module.config.DialectConfig");
        properties.put(PaymentConstant.SHOW_SQL_KEY, PaymentConstant.SHOW_SQL_VALUE);
        properties.put(PaymentConstant.FORMAT_SQL_KEY, PaymentConstant.FORMAT_SQL_VALUE);
        properties.put("spring.jpa.hibernate.ddl-auto", "update");
        return properties;
    }
}
