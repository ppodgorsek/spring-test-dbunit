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

package com.github.springtestdbunit.expected;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.config.CoreTestConfiguration;
import com.github.springtestdbunit.testutils.MustFailDbUnitTestExecutionListener;

@SpringJUnitConfig(CoreTestConfiguration.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, MustFailDbUnitTestExecutionListener.class })
@Transactional
public class ExpectedQueryNonStrictFailureOnMethodTest {

	@Test
	@ExpectedDatabase(value = "/META-INF/db/expected_query_nonstrict.xml", assertionMode = DatabaseAssertionMode.NON_STRICT, query = "select * from SampleEntity where id=1", table = "SampleEntity")
	public void test() throws Exception {
	}

}
