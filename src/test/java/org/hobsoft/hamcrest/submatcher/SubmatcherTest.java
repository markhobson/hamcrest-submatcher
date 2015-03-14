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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hobsoft.hamcrest.submatcher.test.Name;
import org.hobsoft.hamcrest.submatcher.test.Person;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import net.sf.cglib.proxy.Factory;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.hamcrest.submatcher.Submatcher.such;
import static org.hobsoft.hamcrest.submatcher.Submatcher.that;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
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
	
	private ExpectedException thrown = ExpectedException.none();

	// ----------------------------------------------------------------------------------------------------------------
	// JUnit methods
	// ----------------------------------------------------------------------------------------------------------------

	@Rule
	public SpyHolderRule getSpyHolderRule()
	{
		return spyHolderRule;
	}

	@Rule
	public ExpectedException getThrown()
	{
		return thrown;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test
	public void constructorSetsInvocation()
	{
		MethodInvocation invocation = newInvocation();
		
		Submatcher<Person, Object> actual = new Submatcher<Person, Object>(invocation, mock(Matcher.class));
		
		assertThat(actual.getInvocation(), is(invocation));
	}
	
	@Test
	public void constructorWithNullInvocationThrowsException()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("invocation");
		
		new Submatcher<Person, Object>(null, mock(Matcher.class));
	}
	
	@Test
	public void constructorSetsSubmatcher()
	{
		Matcher<Object> submatcher = mock(Matcher.class);
		
		Submatcher<?, ?> actual = new Submatcher<Object, Object>(newInvocation(), submatcher);
		
		assertThat(actual.getSubmatcher(), is((Object) submatcher));
	}
	
	@Test
	public void constructorWithNullSubmatcherThrowsException()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("submatcher");
		
		new Submatcher<Object, Object>(newInvocation(), null);
	}
	
	@Test
	public void matchesSafelyInvokesSubmatcher()
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME);
		Matcher<Name> submatcher = mock(Matcher.class);
		Submatcher<Person, Name> matcher = new Submatcher<Person, Name>(invocation, submatcher);
		Name name = mock(Name.class);
		
		matcher.matchesSafely(newPersonWithName(name), Description.NONE);

		verify(submatcher).matches(name);
	}
	
	@Test
	public void matchesSafelyWhenArgumentInvokesSubmatcher()
	{
		Matcher<Name> submatcher = mock(Matcher.class);
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME_WITH_ARGUMENT, "x");
		Submatcher<Person, Name> matcher = new Submatcher<Person, Name>(invocation, submatcher);
		Name name = mock(Name.class);
		
		matcher.matchesSafely(newPersonWithName(name), Description.NONE);

		verify(submatcher).matches(name);
	}
	
	@Test
	public void matchesSafelyWhenMatchesReturnsTrue()
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME);
		Matcher<Name> submatcher = mock(Matcher.class);
		when(submatcher.matches(any())).thenReturn(true);
		Submatcher<Person, Name> matcher = new Submatcher<Person, Name>(invocation, submatcher);
		
		boolean actual = matcher.matchesSafely(mock(Person.class), Description.NONE);
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void matchesSafelyWhenDoesNotMatchReturnsFalse()
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME);
		Matcher<Name> submatcher = mock(Matcher.class);
		when(submatcher.matches(any())).thenReturn(false);
		Submatcher<Person, Name> matcher = new Submatcher<Person, Name>(invocation, submatcher);
		
		boolean actual = matcher.matchesSafely(mock(Person.class), Description.NONE);
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void matchesSafelyWhenDoesNotMatchAppendsMismatch()
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME);
		Matcher<Name> submatcher = mock(Matcher.class);
		when(submatcher.matches(any())).thenReturn(false);
		doAnswer(appendText(1, "x")).when(submatcher).describeMismatch(anyObject(), any(Description.class));
		Submatcher<Person, Name> matcher = new Submatcher<Person, Name>(invocation, submatcher);
		StringDescription mismatchDescription = new StringDescription();
		
		matcher.matchesSafely(newPersonWithName(mock(Name.class)), mismatchDescription);
		
		assertThat(mismatchDescription.toString(), is("x"));
	}

	@Test
	public void matchesSafelyWhenInvokedMethodThrowsExceptionReturnsFalse()
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME);
		Submatcher<Person, Object> matcher = new Submatcher<Person, Object>(invocation, mock(Matcher.class));
		Person person = mock(Person.class);
		when(person.getName()).thenThrow(new RuntimeException());
		
		boolean actual = matcher.matchesSafely(person, Description.NONE);
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void matchesSafelyWhenInvokedMethodThrowsExceptionAppendsMismatch()
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME);
		Submatcher<Person, Object> matcher = new Submatcher<Person, Object>(invocation, mock(Matcher.class));
		Person person = mock(Person.class);
		RuntimeException exception = new RuntimeException();
		when(person.getName()).thenThrow(exception);
		StringDescription mismatchDescription = new StringDescription();
		
		matcher.matchesSafely(person, mismatchDescription);
		
		assertThat(mismatchDescription.toString(), is("threw <" + exception + ">"));
	}
	
	@Test
	public void describeToAppendsDescription()
	{
		MethodInvocation invocation = mock(MethodInvocation.class);
		doAnswer(appendText(0, "x")).when(invocation).describeTo(any(Description.class));
		Submatcher<Person, Object> matcher = new Submatcher<Person, Object>(invocation, anything("y"));
		StringDescription description = new StringDescription();
		
		matcher.describeTo(description);
		
		assertThat(description.toString(), is("such that x y"));
	}

	@Test
	public void suchReturnsMatcher()
	{
		SpyHolder.setSpy(mockSpy());
		
		Submatcher<?, ?> actual = such(null, mockMatcher());
		
		assertThat(actual, is(instanceOf(Submatcher.class)));
	}
	
	@Test
	public void suchReturnsMatcherWithInvocation()
	{
		SpyHolder.setSpy(mockSpy(Person.GET_NAME));
		
		Submatcher<?, ?> actual = such(null, mockMatcher());
		
		assertThat(actual.getInvocation().getMethod(), is(Person.GET_NAME));
	}
	
	@Test
	public void suchReturnsMatcherWithSubmatcher()
	{
		SpyHolder.setSpy(mockSpy());
		Matcher<?> submatcher = mock(Matcher.class);
		
		Submatcher<?, ?> actual = such(null, submatcher);
		
		assertThat(actual.getSubmatcher(), is((Object) submatcher));
	}
	
	@Test
	public void suchWithNullSubmatcherThrowsException()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("submatcher");
		
		such(null, null);
	}
	
	@Test
	public void suchWhenNullSpyThrowsException()
	{
		SpyHolder.setSpy(null);
		
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("that() must be invoked before such()");
		
		such(null, mockMatcher());
	}
	
	@Test
	public void thatReturnsSpy()
	{
		Person actual = that(Person.class);
		
		assertThat(((Factory) actual).getCallback(0), is(instanceOf(Spy.class)));
	}
	
	@Test
	public void thatWithNullThrowsException()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("type");
		
		that(null);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static Person newPersonWithName(Name name)
	{
		Person person = mock(Person.class);
		when(person.getName()).thenReturn(name);
		when(person.getNameWithArgument(anyString())).thenReturn(name);
		when(person.getNameWithArguments(anyString(), anyString())).thenReturn(name);
		return person;
	}

	private static MethodInvocation newInvocation()
	{
		return new MethodInvocation(someMethod());
	}

	private static Method someMethod()
	{
		return Person.GET_NAME;
	}
	
	private static <T> Matcher<T> mockMatcher()
	{
		return mock(Matcher.class);
	}

	private static Spy<?> mockSpy()
	{
		return mockSpy(someMethod());
	}

	private static Spy<Person> mockSpy(Method invokedMethod)
	{
		Spy<Person> spy = mock(Spy.class);
		when(spy.getInvocation()).thenReturn(new MethodInvocation(invokedMethod));
		return spy;
	}

	private static Answer<Object> appendText(final int descriptionIndex, final String text)
	{
		return new Answer<Object>()
		{
			public Object answer(InvocationOnMock invocation)
			{
				Description description = invocation.getArgumentAt(descriptionIndex, Description.class);
				description.appendText(text);
				return null;
			}
		};
	}
}
