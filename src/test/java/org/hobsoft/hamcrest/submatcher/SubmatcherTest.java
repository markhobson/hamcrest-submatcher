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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.hamcrest.submatcher.Submatcher.such;
import static org.hobsoft.hamcrest.submatcher.Submatcher.that;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
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
		
		Submatcher<Person> actual = new Submatcher<Person>(invocation, mock(Matcher.class));
		
		assertThat(actual.getInvocation(), is(invocation));
	}
	
	@Test
	public void constructorWithNullInvocationThrowsException()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("invocation");
		
		new Submatcher<Person>(null, mock(Matcher.class));
	}
	
	@Test
	public void constructorSetsMatcher()
	{
		Matcher<?> matcher = mock(Matcher.class);
		
		Submatcher<?> actual = new Submatcher<Object>(newInvocation(), matcher);
		
		assertThat(actual.getMatcher(), is((Object) matcher));
	}
	
	@Test
	public void constructorWithNullMatcherThrowsException()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("matcher");
		
		new Submatcher<Object>(newInvocation(), null);
	}
	
	@Test
	public void matchesSafelyInvokesSubmatcher()
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME);
		Matcher<Name> matcher = mock(Matcher.class);
		Submatcher<Person> submatcher = new Submatcher<Person>(invocation, matcher);
		Name name = new Name("x");
		
		submatcher.matchesSafely(new Person(name));

		verify(matcher).matches(name);
	}
	
	@Test
	public void matchesSafelyWhenArgumentInvokesSubmatcher()
	{
		Matcher<Name> matcher = mock(Matcher.class);
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME_WITH_ARGUMENT, "x");
		Submatcher<Person> submatcher = new Submatcher<Person>(invocation, matcher);
		Name name = new Name("y");
		
		submatcher.matchesSafely(new Person(name));

		verify(matcher).matches(name);
	}
	
	@Test
	public void matchesSafelyWhenMatchesReturnsTrue()
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME);
		Matcher<Name> matcher = mock(Matcher.class);
		when(matcher.matches(any())).thenReturn(true);
		Submatcher<Person> submatcher = new Submatcher<Person>(invocation, matcher);
		
		boolean actual = submatcher.matchesSafely(new Person());
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void matchesSafelyWhenDoesNotMatchReturnsFalse()
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME);
		Matcher<Name> matcher = mock(Matcher.class);
		when(matcher.matches(any())).thenReturn(false);
		Submatcher<Person> submatcher = new Submatcher<Person>(invocation, matcher);
		
		boolean actual = submatcher.matchesSafely(new Person());
		
		assertThat(actual, is(false));
	}

	@Test
	public void matchesSafelyWhenInvokedMethodThrowsExceptionReturnsFalse()
	{
		MethodInvocation invocation = new MethodInvocation(Person.THROW_EXCEPTION);
		Submatcher<Person> submatcher = new Submatcher<Person>(invocation, mockMatcher());
		
		boolean actual = submatcher.matchesSafely(new Person());
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void describeToAppendsDescription()
	{
		MethodInvocation invocation = mock(MethodInvocation.class);
		doAnswer(appendText("x")).when(invocation).describeTo(any(Description.class));
		Matcher<?> matcher = mock(Matcher.class);
		doAnswer(appendText("y")).when(matcher).describeTo(any(Description.class));
		Submatcher<Person> submatcher = new Submatcher<Person>(invocation, matcher);
		StringDescription description = new StringDescription();
		
		submatcher.describeTo(description);
		
		assertThat(description.toString(), is("such that x y"));
	}

	@Test
	public void suchReturnsSubmatcher()
	{
		SpyHolder.setSpy(mockSpy());
		
		Submatcher<?> actual = such(null, mockMatcher());
		
		assertThat(actual, is(instanceOf(Submatcher.class)));
	}
	
	@Test
	public void suchReturnsSubmatcherWithInvocation()
	{
		SpyHolder.setSpy(mockSpy(Person.GET_NAME));
		
		Submatcher<?> actual = such(null, mockMatcher());
		
		assertThat(actual.getInvocation().getMethod(), is(Person.GET_NAME));
	}
	
	@Test
	public void suchReturnsSubmatcherWithMatcher()
	{
		SpyHolder.setSpy(mockSpy());
		Matcher<?> matcher = mock(Matcher.class);
		
		Submatcher<?> actual = such(null, matcher);
		
		assertThat(actual.getMatcher(), is((Object) matcher));
	}
	
	@Test
	public void suchWithNonNullThatThrowsException()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("that() must be the first argument to such()");
		
		such("x", mockMatcher());
	}
	
	@Test
	public void suchWithNullMatcherThrowsException()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("matcher");
		
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

	@Test
	public void suchThatMatchesWithArgumentWhenMatchesReturnsTrue()
	{
		Name name = new Name("x");
		
		Matcher<Person> actual = such(that(Person.class).getNameWithArgument("y"), is(name));
		
		assertThat(actual.matches(new Person(name)), is(true));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

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

	private static Answer<Object> appendText(final String text)
	{
		return new Answer<Object>()
		{
			public Object answer(InvocationOnMock invocation)
			{
				Description description = invocation.getArgumentAt(0, Description.class);
				description.appendText(text);
				return null;
			}
		};
	}
}
