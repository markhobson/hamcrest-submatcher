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
import java.lang.reflect.Method;

import org.hamcrest.CoreMatchers;
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
	public void constructorSetsMethod() throws NoSuchMethodException
	{
		Method method = Person.class.getMethod("getName");
		
		MethodInvocation actual = new MethodInvocation(method);
		
		assertThat(actual.getMethod(), is(method));
	}
	
	@Test
	public void constructorWithNullMethodThrowsException()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("method");
		
		new MethodInvocation(null);
	}
	
	@Test
	public void describeToAppendsDescription() throws NoSuchMethodException
	{
		Method method = Person.class.getMethod("getName");
		MethodInvocation invocation = new MethodInvocation(method);
		StringDescription description = new StringDescription();
		
		invocation.describeTo(description);
		
		assertThat(description.toString(), is("getName()"));
	}
	
	@Test
	public void invokeInvokesMethod() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		MethodInvocation invocation = new MethodInvocation(Person.class.getMethod("getName"));
		Person instance = mock(Person.class);
		
		invocation.invoke(instance);
		
		verify(instance).getName();
	}
	
	@Test
	public void invokeReturnsValue() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		MethodInvocation invocation = new MethodInvocation(Person.class.getMethod("getName"));
		Person instance = mock(Person.class);
		Name name = new Name("x");
		when(instance.getName()).thenReturn(name);
		
		Object actual = invocation.invoke(instance);
		
		assertThat(actual, CoreMatchers.<Object>is(name));
	}
}
