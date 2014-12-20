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
import org.hamcrest.TypeSafeMatcher;

/**
 * Matcher for a sub-property of an instance.
 * 
 * @param <T>
 *            the instance type
 */
public class Submatcher<T> extends TypeSafeMatcher<T>
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final Method invokedMethod;
	
	private final Matcher<?> matcher;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public Submatcher(Method invokedMethod, Matcher<?> matcher)
	{
		this.invokedMethod = invokedMethod;
		this.matcher = matcher;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// TypeSafeMatcher methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected boolean matchesSafely(T actual)
	{
		Object subactual;
		try
		{
			subactual = invokedMethod.invoke(actual);
		}
		catch (Exception exception)
		{
			// TODO: handle
			throw new AssertionError();
		}
		
		return matcher.matches(subactual);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// SelfDescribing methods
	// ----------------------------------------------------------------------------------------------------------------

	public void describeTo(Description description)
	{
		// TODO: implement
	}

	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static <T, U> Submatcher<T> such(U actual, Matcher<U> matcher)
	{
		return new Submatcher<T>(SubmatcherMethodInterceptor.getInvokedMethod(), matcher);
	}
	
	public static <U> U that(Class<U> type)
	{
		return new Spy<U>(type, new SubmatcherMethodInterceptor()).create();
	}
}
