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
package com.github.springtestdbunit.sample.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.dataset.CsvUrlDataSetLoader;
import com.github.springtestdbunit.sample.config.SampleTestConfiguration;
import com.github.springtestdbunit.sample.entity.Person;

@SpringJUnitConfig(SampleTestConfiguration.class)
@DbUnitConfiguration(dataSetLoader = CsvUrlDataSetLoader.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class PersonServiceCsvUrlTest {

	@Resource
	private PersonService personService;

	@Test
	@DatabaseSetup("csv/sample/")
	public void testFind() throws Exception {
		List<Person> personList = personService.find("gor");
		assertEquals(1, personList.size(), "Wrong number of results");
		assertEquals("Paul", personList.get(0).getFirstName(), "Wrong user");
	}

	@Test
	@DatabaseSetup("csv/sample/")
	@ExpectedDatabase("csv/expected/")
	public void testRemove() throws Exception {
		personService.remove(1);
	}

}
