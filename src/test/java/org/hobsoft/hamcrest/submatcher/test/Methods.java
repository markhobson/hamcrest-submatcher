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
package org.hobsoft.hamcrest.submatcher.test;

import java.lang.reflect.Method;

/**
 * Methods for working with methods reflectively.
 */
final class Methods
{
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	private Methods()
	{
		throw new AssertionError();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public static Method getQuietly(Class<?> type, String name, Class<?>... parameterTypes)
	{
		try
		{
			return type.getMethod(name, parameterTypes);
		}
		catch (NoSuchMethodException exception)
		{
			throw new AssertionError("Cannot find method", exception);
		}
	}
}
