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

import org.hamcrest.Matcher;
import org.hobsoft.hamcrest.submatcher.Submatcher.InvocationInfo;
import org.hobsoft.hamcrest.submatcher.test.Name;
import org.hobsoft.hamcrest.submatcher.test.Person;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.hamcrest.submatcher.Submatcher.such;
import static org.hobsoft.hamcrest.submatcher.Submatcher.that;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@code Submatcher}.
 */
public class SubmatcherTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// types
	// ----------------------------------------------------------------------------------------------------------------

	private abstract static class NameInvocationInfo extends Name implements InvocationInfo
	{
		// simple subtype
	}

	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test
	public void suchReturnsMatcher()
	{
		Matcher<?> actual = such(null, null);
		
		assertThat(actual, is(instanceOf(Matcher.class)));
	}
	
	@Test
	public void suchMatchesInvokesSubmatcher() throws NoSuchMethodException
	{
		NameInvocationInfo invocationInfo = mock(NameInvocationInfo.class);
		when(invocationInfo.getInvokedMethod()).thenReturn(Person.class.getMethod("getName"));
		Matcher<Name> matcher = mock(Matcher.class);
		Name name = new Name("x");
		
		such(invocationInfo, matcher).matches(new Person(name));

		verify(matcher).matches(name);
	}

	@Test
	public void thatWithClassReturnsInstance()
	{
		Person actual = that(Person.class);
		
		assertThat(actual, is(instanceOf(Person.class)));
	}
	
	@Test
	public void thatWithClassThenMethodReturnsInstance()
	{
		Name actual = that(Person.class).getName();
		
		assertThat(actual, is(instanceOf(Name.class)));
	}
	
	@Test
	public void thatWithClassThenMethodReturnsInvocationInfo()
	{
		Name actual = that(Person.class).getName();
		
		assertThat(actual, is(instanceOf(InvocationInfo.class)));
	}
	
	@Test
	public void thatWithClassThenMethodReturnsInvocationInfoWithMethod() throws NoSuchMethodException
	{
		InvocationInfo actual = (InvocationInfo) that(Person.class).getName();
		
		assertThat(actual.getInvokedMethod(), is(Person.class.getMethod("getName")));
	}
}
