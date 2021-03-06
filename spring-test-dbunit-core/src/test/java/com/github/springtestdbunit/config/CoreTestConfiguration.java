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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptException;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import com.github.springtestdbunit.entity.EntityAssert;
import com.github.springtestdbunit.entity.OtherEntityAssert;
import com.github.springtestdbunit.test.config.TestConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Hibernate configuration for the Core module.
 *
 * @author Paul Podgorsek
 */
@Configuration
@Import(TestConfiguration.class)
public class CoreTestConfiguration {

	@Value("${database.datasource.poolname}")
	private String dataSourcePoolName;

	@Value("${database.datasource.driver}")
	private String dataSourceDriver;

	@Value("${database.datasource.url}")
	private String dataSourceUrl;

	@Value("${database.datasource.username}")
	private String dataSourceUsername;

	@Value("${database.datasource.password}")
	private String dataSourcePassword;

	@Resource
	private IDataTypeFactory dataTypeFactory;

	@Resource
	private DataSource dataSource;

	@Bean
	public DatabaseConfigBean databaseConfig() {

		DatabaseConfigBean databaseConfig = new DatabaseConfigBean();
		databaseConfig.setDatatypeFactory(dataTypeFactory);

		return databaseConfig;
	}

	@Bean(DbUnitTestExecutionListener.DEFAULT_DBUNIT_DATABASE_CONNECTION_BEAN_NAME)
	public DatabaseDataSourceConnectionFactoryBean databaseDataSourceConnectionFactory() {

		DatabaseDataSourceConnectionFactoryBean databaseDataSourceConnectionFactory = new DatabaseDataSourceConnectionFactoryBean(
				dataSource);
		databaseDataSourceConnectionFactory.setDatabaseConfig(databaseConfig());

		return databaseDataSourceConnectionFactory;
	}

	@Bean(DbUnitTestExecutionListener.DEFAULT_DBUNIT_DATABASE_CONNECTION_BEAN_NAME + "2")
	public DatabaseDataSourceConnectionFactoryBean databaseDataSourceConnectionFactory2() {

		DatabaseDataSourceConnectionFactoryBean databaseDataSourceConnectionFactory = new DatabaseDataSourceConnectionFactoryBean(
				dataSource2());
		databaseDataSourceConnectionFactory.setDatabaseConfig(databaseConfig());

		return databaseDataSourceConnectionFactory;
	}

	@Bean(name = DbUnitTestExecutionListener.DEFAULT_DATASOURCE_BEAN_NAME + "2", destroyMethod = "close")
	public DataSource dataSource2() {
		return new HikariDataSource(hikariConfig2());
	}

	@Bean
	public List<String> hibernatePackagesToScan() {

		List<String> hibernatePackagesToScan = new ArrayList<String>();
		hibernatePackagesToScan.add("com.github.springtestdbunit.entity");

		return hibernatePackagesToScan;
	}

	@Bean
	public HikariConfig hikariConfig2() {

		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setPoolName(dataSourcePoolName + "2");
		hikariConfig.setDriverClassName(dataSourceDriver);
		hikariConfig.setJdbcUrl(dataSourceUrl + "2");
		hikariConfig.setUsername(dataSourceUsername);
		hikariConfig.setPassword(dataSourcePassword);
		hikariConfig.setMaximumPoolSize(50);

		return hikariConfig;
	}

	@Bean
	public DefaultResourceLoader resourceLoader2() throws ScriptException {

		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(true, false,
				StandardCharsets.UTF_8.name(), resourceLoader.getResource("init-datasource2.sql"));
		databasePopulator.execute(dataSource2());

		return resourceLoader;
	}

	@Bean
	public EntityAssert entityAssert() {
		return new EntityAssert();
	}

	@Bean
	public OtherEntityAssert otherEntityAssert() {
		return new OtherEntityAssert();
	}

}
