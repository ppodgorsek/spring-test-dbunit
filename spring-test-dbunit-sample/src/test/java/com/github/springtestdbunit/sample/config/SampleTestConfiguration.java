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
package com.github.springtestdbunit.sample.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import com.github.springtestdbunit.test.config.TestConfiguration;

/**
 * Hibernate configuration for the Sample module.
 *
 * @author Paul Podgorsek
 */
@Configuration
@Import({ TestConfiguration.class, SampleConfiguration.class })
public class SampleTestConfiguration {

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

	@Bean
	public List<String> hibernatePackagesToScan() {

		List<String> hibernatePackagesToScan = new ArrayList<String>();
		hibernatePackagesToScan.add("com.github.springtestdbunit.sample.entity");

		return hibernatePackagesToScan;
	}

}
