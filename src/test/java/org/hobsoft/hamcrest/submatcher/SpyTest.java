/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hobsoft.hamcrest.submatcher;

import org.hobsoft.hamcrest.submatcher.test.Name;
import org.hobsoft.hamcrest.submatcher.test.Person;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code Spy}.
 */
public class SpyTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private ExpectedException thrown = ExpectedException.none();
	
	// ----------------------------------------------------------------------------------------------------------------
	// JUnit methods
	// ----------------------------------------------------------------------------------------------------------------

	@Rule
	public ExpectedException getThrown()
	{
		return thrown;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void constructorWithNullThrowsException()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("type");
		
		new Spy<Object>(null);
	}
	
	@Test
	public void createReturnsInstance()
	{
		Person actual = new Spy<Person>(Person.class).create();
		
		assertThat(actual, is(instanceOf(Person.class)));
	}

	@Test
	public void createThenMethodReturnsNull()
	{
		Spy<Person> spy = new Spy<Person>(Person.class);
		
		Name actual = spy.create().getName();
		
		assertThat(actual, is(nullValue()));
	}
	
	@Test
	public void createThenMethodSetsInvocation() throws NoSuchMethodException
	{
		Spy<Person> spy = new Spy<Person>(Person.class);
		
		spy.create().getName();
		
		assertThat(spy.getInvocation().getMethod(), is(Person.class.getMethod("getName")));
	}
}
