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
 * Simple type for unit tests.
 */
public class Person
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	public static final Method GET_NAME = getMethodQuietly(Person.class, "getName");
	
	public static final Method GET_NAME_WITH_ARGUMENT = getMethodQuietly(Person.class, "getNameWithArgument",
		String.class);
	
	public static final Method GET_NAME_WITH_ARGUMENTS = getMethodQuietly(Person.class, "getNameWithArguments",
		String.class, String.class);
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public Name getName()
	{
		return null;
	}
	
	public Name getNameWithArgument(String arg)
	{
		return null;
	}
	
	public Name getNameWithArguments(String arg1, String arg2)
	{
		return null;
	}

	public int getAge()
	{
		return 0;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static Method getMethodQuietly(Class<?> type, String name, Class<?>... parameterTypes)
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
