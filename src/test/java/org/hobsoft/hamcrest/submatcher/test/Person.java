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
public interface Person
{
	// ----------------------------------------------------------------------------------------------------------------
	// constants
	// ----------------------------------------------------------------------------------------------------------------

	Method GET_NAME = Methods.getQuietly(Person.class, "getName");
	
	Method GET_NAME_WITH_ARGUMENT = Methods.getQuietly(Person.class, "getNameWithArgument", String.class);
	
	Method GET_NAME_WITH_ARGUMENTS = Methods.getQuietly(Person.class, "getNameWithArguments", String.class,
		String.class);
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	Name getName();
	
	Name getNameWithArgument(String arg);
	
	Name getNameWithArguments(String arg1, String arg2);

	int getAge();
}
