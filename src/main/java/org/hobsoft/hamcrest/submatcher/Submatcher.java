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
import org.hamcrest.TypeSafeMatcher;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
 * Matcher for a sub-property of an instance.
 * 
 * @param <T>
 *            the instance type
 */
public class Submatcher<T> extends TypeSafeMatcher<T>
{
	// ----------------------------------------------------------------------------------------------------------------
	// TypeSafeMatcher methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected boolean matchesSafely(T item)
	{
		return false;
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

	public static <U> U that(Class<U> type)
	{
		Objenesis objenesis = new ObjenesisStd();
		return objenesis.newInstance(type);
	}
}
