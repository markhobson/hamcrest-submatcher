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

import java.lang.reflect.Method;

import org.hamcrest.Matcher;
import org.hobsoft.hamcrest.submatcher.test.Name;
import org.hobsoft.hamcrest.submatcher.test.Person;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hobsoft.hamcrest.submatcher.Submatcher.such;
import static org.hobsoft.hamcrest.submatcher.Submatcher.that;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@code Submatcher}.
 */
public class SubmatcherTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private SpyHolderRule spyHolderRule = new SpyHolderRule();

	// ----------------------------------------------------------------------------------------------------------------
	// JUnit methods
	// ----------------------------------------------------------------------------------------------------------------

	@Rule
	public SpyHolderRule getSpyHolderRule()
	{
		return spyHolderRule;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test
	public void constructorSetsInvokedMethod() throws NoSuchMethodException
	{
		Submatcher<Person> actual = new Submatcher<Person>(Person.class.getMethod("getName"), null);
		
		assertThat(actual.getInvokedMethod(), is(Person.class.getMethod("getName")));
	}
	
	@Test
	public void constructorSetsMatcher()
	{
		Matcher<?> matcher = mock(Matcher.class);
		
		Submatcher<?> actual = new Submatcher<Object>(null, matcher);
		
		assertThat(actual.getMatcher(), is((Object) matcher));
	}
	
	@Test
	public void matchesSafelyInvokesSubmatcher() throws NoSuchMethodException
	{
		Matcher<Name> matcher = mock(Matcher.class);
		Submatcher<Person> submatcher = new Submatcher<Person>(Person.class.getMethod("getName"), matcher);
		Name name = new Name("x");
		
		submatcher.matchesSafely(new Person(name));

		verify(matcher).matches(name);
	}
	
	@Test
	public void matchesSafelyWhenMatchesReturnsTrue() throws NoSuchMethodException
	{
		Matcher<Name> matcher = mock(Matcher.class);
		when(matcher.matches(any())).thenReturn(true);
		Submatcher<Person> submatcher = new Submatcher<Person>(Person.class.getMethod("getName"), matcher);
		
		boolean actual = submatcher.matchesSafely(new Person());
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void matchesSafelyWhenDoesNotMatchReturnsFalse() throws NoSuchMethodException
	{
		Matcher<Name> matcher = mock(Matcher.class);
		when(matcher.matches(any())).thenReturn(false);
		Submatcher<Person> submatcher = new Submatcher<Person>(Person.class.getMethod("getName"), matcher);
		
		boolean actual = submatcher.matchesSafely(new Person());
		
		assertThat(actual, is(false));
	}

	@Test
	public void suchReturnsSubmatcher() throws NoSuchMethodException
	{
		SpyHolder.setSpy(mockSpy());
		
		Submatcher<?> actual = such(null, null);
		
		assertThat(actual, is(instanceOf(Submatcher.class)));
	}
	
	@Test
	public void suchReturnsSubmatcherWithInvokedMethod() throws NoSuchMethodException
	{
		SpyHolder.setSpy(mockSpy(Person.class.getMethod("getName")));
		
		Submatcher<?> actual = such(null, null);
		
		assertThat(actual.getInvokedMethod(), is(Person.class.getMethod("getName")));
	}
	
	@Test
	public void suchReturnsSubmatcherWithMatcher() throws NoSuchMethodException
	{
		SpyHolder.setSpy(mockSpy());
		Matcher<?> matcher = mock(Matcher.class);
		
		Submatcher<?> actual = such(null, matcher);
		
		assertThat(actual.getMatcher(), is((Object) matcher));
	}
	
	@Test
	public void thatWithClassReturnsInstance()
	{
		Person actual = that(Person.class);
		
		assertThat(actual, is(instanceOf(Person.class)));
	}
	
	@Test
	public void thatWithClassThenMethodReturnsNull()
	{
		Name actual = that(Person.class).getName();
		
		assertThat(actual, is(nullValue()));
	}
	
	@Test
	public void thatWithClassThenMethodSetsInvokedMethod() throws NoSuchMethodException
	{
		that(Person.class).getName();
		
		assertThat(SpyHolder.getSpy().getInvokedMethod(), is(Person.class.getMethod("getName")));
	}
	
	@Test
	public void suchThatMatchesWhenMatchesReturnsTrue()
	{
		Name name = new Name("x");
		
		Matcher<Person> actual = such(that(Person.class).getName(), is(name));
		
		assertThat(actual.matches(new Person(name)), is(true));
	}
	
	@Test
	public void suchThatMatchesWhenDoesNotMatchReturnsFalse()
	{
		Matcher<Person> actual = such(that(Person.class).getName(), is(new Name("x")));
		
		assertThat(actual.matches(new Person(new Name("y"))), is(false));
	}

	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static Spy<?> mockSpy() throws NoSuchMethodException
	{
		return mockSpy(Person.class.getMethod("getName"));
	}

	private static Spy<Person> mockSpy(Method invokedMethod)
	{
		Spy<Person> spy = mock(Spy.class);
		when(spy.getInvokedMethod()).thenReturn(invokedMethod);
		return spy;
	}
}
