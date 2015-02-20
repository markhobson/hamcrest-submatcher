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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hobsoft.hamcrest.submatcher.Preconditions.checkNotNull;

/**
 * Matcher for a sub-property of an instance.
 * 
 * @param <T>
 *            the instance type
 */
public class Submatcher<T> extends TypeSafeDiagnosingMatcher<T>
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final MethodInvocation invocation;
	
	private final Matcher<?> matcher;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	Submatcher(MethodInvocation invocation, Matcher<?> matcher)
	{
		this.invocation = checkNotNull(invocation, "invocation");
		this.matcher = checkNotNull(matcher, "matcher");
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
		catch (Exception exception)
		{
			return false;
		}
		
		return matcher.matches(subactual);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// SelfDescribing methods
	// ----------------------------------------------------------------------------------------------------------------

	public void describeTo(Description description)
	{
		description.appendText("such that ")
			.appendDescriptionOf(invocation)
			.appendText(" ")
			.appendDescriptionOf(matcher);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static <T, U> Submatcher<T> such(U that, Matcher<U> matcher)
	{
		checkNotNull(matcher, "matcher");
		
		if (!SpyHolder.hasSpy())
		{
			throw new IllegalStateException("that() must be invoked before such()");
		}
		
		MethodInvocation invocation = SpyHolder.getSpy().getInvocation();
		
		return new Submatcher<T>(invocation, matcher);
	}
	
	public static <U> U that(Class<U> type)
	{
		checkNotNull(type, "type");
		
		Spy<U> spy = new Spy<U>(type);
		
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

	Matcher<?> getMatcher()
	{
		return matcher;
	}
}
