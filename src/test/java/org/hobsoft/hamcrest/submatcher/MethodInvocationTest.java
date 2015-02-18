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

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;
import org.hobsoft.hamcrest.submatcher.test.Name;
import org.hobsoft.hamcrest.submatcher.test.Person;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests {@code MethodInvocation}.
 */
public class MethodInvocationTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private ExpectedException thrown = ExpectedException.none();
	
	// ----------------------------------------------------------------------------------------------------------------
	// JUnit methods
	// ----------------------------------------------------------------------------------------------------------------

	@Rule
	public ExpectedException getThrown()
	{
		return thrown;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void constructorSetsMethod()
	{
		MethodInvocation actual = new MethodInvocation(Person.GET_NAME);
		
		assertThat(actual.getMethod(), is(Person.GET_NAME));
	}
	
	@Test
	public void constructorSetsArguments()
	{
		MethodInvocation actual = new MethodInvocation(Person.GET_NAME_WITH_ARGUMENT, "x");
		
		assertThat(actual.getArguments(), is(Matchers.<Object>arrayContaining("x")));
	}
	
	@Test
	public void constructorWithNullMethodThrowsException()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("method");
		
		new MethodInvocation(null);
	}
	
	@Test
	public void constructorWithNullArgumentsThrowsException()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("arguments");
		
		new MethodInvocation(Person.GET_NAME, (Object[]) null);
	}
	
	@Test
	public void describeToAppendsDescription()
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME);
		StringDescription description = new StringDescription();
		
		invocation.describeTo(description);
		
		assertThat(description.toString(), is("getName()"));
	}
	
	@Test
	public void describeToWithArgumentAppendsDescription()
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME_WITH_ARGUMENT, "x");
		StringDescription description = new StringDescription();
		
		invocation.describeTo(description);
		
		assertThat(description.toString(), is("getNameWithArgument(\"x\")"));
	}
	
	@Test
	public void describeToWithArgumentsAppendsDescription()
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME_WITH_ARGUMENTS, "x", "y");
		StringDescription description = new StringDescription();
		
		invocation.describeTo(description);
		
		assertThat(description.toString(), is("getNameWithArguments(\"x\", \"y\")"));
	}
	
	@Test
	public void invokeInvokesMethod() throws IllegalAccessException, InvocationTargetException
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME);
		Person instance = mock(Person.class);
		
		invocation.invoke(instance);
		
		verify(instance).getName();
	}
	
	@Test
	public void invokeWithArgumentInvokesMethod() throws IllegalAccessException, InvocationTargetException
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME_WITH_ARGUMENT, "x");
		Person instance = mock(Person.class);
		
		invocation.invoke(instance);
		
		verify(instance).getNameWithArgument("x");
	}
	
	@Test
	public void invokeReturnsValue() throws IllegalAccessException, InvocationTargetException
	{
		MethodInvocation invocation = new MethodInvocation(Person.GET_NAME);
		Person instance = mock(Person.class);
		Name name = new Name("x");
		when(instance.getName()).thenReturn(name);
		
		Object actual = invocation.invoke(instance);
		
		assertThat(actual, CoreMatchers.<Object>is(name));
	}
}
