package com.order.module.config;

import com.order.module.exception.OrderSystemException;
import com.order.module.utils.OrderConstant;
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
@EnableJpaRepositories(basePackages = "com.order.module.repository", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "platformTransactionManager")
public class MySQLConfig {
    @Bean
    DataSource dataSource() {
        String dbUser = OrderConstant.DB_USER_NAME;
        String dbPassword = OrderConstant.DB_PASSWORD;
        String driverClassName = OrderConstant.DB_DRIVER_CLASS_NAME;
        DriverManagerDataSource ds = new DriverManagerDataSource(getDBUrl(), dbUser, dbPassword);
        try {
            ds.setDriverClassName(driverClassName);
        } catch (Exception e) {
            throw new OrderSystemException("ERR002", "DB ISSUE");
        }
        try {
            ds.getConnection().close();
        } catch (SQLException e) {
            throw new OrderSystemException(e);
        }
        return ds;
    }

    private String getDBUrl() {
        String dbHost = OrderConstant.DB_HOST;
        String dbPort = OrderConstant.DB_PORT;
        String dbName = OrderConstant.DB_NAME;
        String dbUrlPrefix = OrderConstant.DB_URL_PREFIX;
        //		baseUrl.append(EMPConstant.COLON);
        return dbUrlPrefix + dbHost +
                OrderConstant.COLON +
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
        factoryBean.setPackagesToScan("com.order.module.entity");
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        factoryBean.setJpaProperties(jpaProperties());
        return factoryBean;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put(OrderConstant.DIALECT_KEY, OrderConstant.DIALECT_VALUE);
        properties.put("hibernate.dialect", "com.order.module.config.DialectConfig");
        properties.put(OrderConstant.SHOW_SQL_KEY, OrderConstant.SHOW_SQL_VALUE);
        properties.put(OrderConstant.FORMAT_SQL_KEY, OrderConstant.FORMAT_SQL_VALUE);
        properties.put("spring.jpa.hibernate.ddl-auto", "update");
        return properties;
    }

}
