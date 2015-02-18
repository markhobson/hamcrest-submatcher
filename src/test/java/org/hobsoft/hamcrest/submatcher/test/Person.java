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

	public static final Method GET_NAME;
	
	public static final Method GET_NAME_WITH_ARGUMENT;
	
	public static final Method GET_NAME_WITH_ARGUMENTS;
	
	public static final Method THROW_EXCEPTION;
	
	static
	{
		try
		{
			GET_NAME = Person.class.getMethod("getName");
			GET_NAME_WITH_ARGUMENT = Person.class.getMethod("getNameWithArgument", String.class);
			GET_NAME_WITH_ARGUMENTS = Person.class.getMethod("getNameWithArguments", String.class, String.class);
			THROW_EXCEPTION = Person.class.getMethod("throwException");
		}
		catch (NoSuchMethodException exception)
		{
			throw new AssertionError("Cannot find Person method", exception);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private Name name;
	
	private int age;
	
	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------
	
	public Person()
	{
		this(new Name());
	}

	public Person(Name name)
	{
		setName(name);
	}
	
	public Person(int age)
	{
		setAge(age);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public Name getName()
	{
		return name;
	}
	
	public void setName(Name name)
	{
		this.name = name;
	}
	
	public Name getNameWithArgument(String arg)
	{
		return name;
	}
	
	public Name getNameWithArguments(String arg1, String arg2)
	{
		return name;
	}

	public int getAge()
	{
		return age;
	}
	
	public void setAge(int age)
	{
		this.age = age;
	}
	
	public void throwException() throws Exception
	{
		throw new Exception();
	}
}
