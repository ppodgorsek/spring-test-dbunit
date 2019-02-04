/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.springtestdbunit.config;

import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Generic configuration which can be used across all tests.
 *
 * @author Paul Podgorsek
 */
@Configuration
@EnableTransactionManagement
public class GenericTestConfiguration {

	@Resource
	private List<String> hibernatePackagesToScan;

	@Bean
	public IDataTypeFactory dataTypeFactory() {
		return new HsqldbDataTypeFactory();
	}

	@Bean
	public DatabaseConfigBean databaseConfig() {

		DatabaseConfigBean databaseConfig = new DatabaseConfigBean();
		databaseConfig.setDatatypeFactory(dataTypeFactory());

		return databaseConfig;
	}

	@Bean
	public DatabaseDataSourceConnectionFactoryBean databaseDataSourceConnectionFactory() {

		DatabaseDataSourceConnectionFactoryBean databaseDataSourceConnectionFactory = new DatabaseDataSourceConnectionFactoryBean(
				dataSource());
		databaseDataSourceConnectionFactory.setDatabaseConfig(databaseConfig());

		return databaseDataSourceConnectionFactory;
	}

	@Bean
	public HikariConfig hikariConfig() {

		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setPoolName("springHikariCP");
		hikariConfig.setDriverClassName("org.hsqldb.jdbcDriver");
		hikariConfig.setJdbcUrl("jdbc:hsqldb:mem:springtestdbunit");
		hikariConfig.setUsername("sa");
		hikariConfig.setPassword("");
		hikariConfig.setMaximumPoolSize(50);

		return hikariConfig;
	}

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		return new HikariDataSource(hikariConfig());
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(true);

		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		jpaProperties.put("hibernate.format_sql", true);
		jpaProperties.put("hibernate.show_sql", true);
		jpaProperties.put("hibernate.hbm2ddl.auto", "create-drop");
		jpaProperties.put("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider");
		jpaProperties.put("hibernate.id.new_generator_mappings", "true");

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(
				this.hibernatePackagesToScan.toArray(new String[this.hibernatePackagesToScan.size()]));
		factory.setDataSource(dataSource());
		factory.setJpaProperties(jpaProperties);

		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());

		return txManager;
	}

}
