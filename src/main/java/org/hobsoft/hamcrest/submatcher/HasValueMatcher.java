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

import java.lang.reflect.InvocationTargetException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hobsoft.hamcrest.submatcher.Preconditions.checkNotNull;

/**
 * Matcher for a sub-property of an instance.
 * 
 * @param <T>
 *            the instance type
 * @param <U>
 *            the sub-property type
 */
public class HasValueMatcher<T, U> extends TypeSafeDiagnosingMatcher<T>
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final MethodInvocation invocation;
	
	private final Matcher<U> submatcher;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	HasValueMatcher(MethodInvocation invocation, Matcher<U> submatcher)
	{
		this.invocation = checkNotNull(invocation, "invocation");
		this.submatcher = checkNotNull(submatcher, "submatcher");
	}

	// ----------------------------------------------------------------------------------------------------------------
	// TypeSafeDiagnosingMatcher methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected boolean matchesSafely(T actual, Description mismatchDescription)
	{
		Object subactual;
		try
		{
			subactual = invocation.invoke(actual);
		}
		catch (InvocationTargetException exception)
		{
			mismatchDescription.appendText("threw ")
				.appendValue(exception.getCause());
			
			return false;
		}
		catch (Exception exception)
		{
			// TODO: handle
			throw new AssertionError();
		}
		
		boolean matches = submatcher.matches(subactual);
		
		if (!matches)
		{
			submatcher.describeMismatch(subactual, mismatchDescription);
		}
		
		return matches;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// SelfDescribing methods
	// ----------------------------------------------------------------------------------------------------------------

	public void describeTo(Description description)
	{
		description.appendText("has value ")
			.appendDescriptionOf(invocation)
			.appendText(" ")
			.appendDescriptionOf(submatcher);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static <T, U> HasValueMatcher<T, U> hasValue(U on, Matcher<U> submatcher)
	{
		checkNotNull(submatcher, "submatcher");
		
		if (!SpyHolder.hasSpy())
		{
			throw new IllegalStateException("on() must be invoked before hasValue()");
		}
		
		MethodInvocation invocation = SpyHolder.getSpy().getInvocation();
		
		return new HasValueMatcher<T, U>(invocation, submatcher);
	}
	
	public static <T> T on(Class<T> type)
	{
		checkNotNull(type, "type");
		
		Spy<T> spy = new Spy<T>(type);
		
		SpyHolder.setSpy(spy);
		
		return spy.create();
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// package methods
	// ----------------------------------------------------------------------------------------------------------------

	MethodInvocation getInvocation()
	{
		return invocation;
	}

	Matcher<U> getSubmatcher()
	{
		return submatcher;
	}
}
