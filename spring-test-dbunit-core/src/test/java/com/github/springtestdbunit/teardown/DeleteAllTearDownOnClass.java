/*
 * Copyright 2002-2016 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.springtestdbunit.teardown;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.config.CoreTestConfiguration;
import com.github.springtestdbunit.entity.EntityAssert;
import com.github.springtestdbunit.testutils.AfterTearDownDbUnitTestExecutionListener;

@SpringJUnitConfig(CoreTestConfiguration.class)
@DbUnitConfiguration(databaseConnection = "databaseDataSourceConnectionFactory")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		AfterTearDownDbUnitTestExecutionListener.class })
@DatabaseTearDown(type = DatabaseOperation.DELETE, value = "/META-INF/db/delete.xml")
@Transactional
public class DeleteAllTearDownOnClass {

	@Autowired
	private EntityAssert entityAssert;

	@Test
	public void test() {
		entityAssert.assertValues("existing1", "existing2");
	}

	public void afterTest() {
		entityAssert.assertValues("existing2");
	}

}
