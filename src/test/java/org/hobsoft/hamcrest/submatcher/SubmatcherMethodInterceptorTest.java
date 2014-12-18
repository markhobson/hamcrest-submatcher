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

import org.hobsoft.hamcrest.submatcher.test.Person;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code SubmatcherMethodInterceptor}.
 */
public class SubmatcherMethodInterceptorTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private SubmatcherMethodInterceptor interceptor;

	// ----------------------------------------------------------------------------------------------------------------
	// JUnit methods
	// ----------------------------------------------------------------------------------------------------------------

	@Before
	public void setUp()
	{
		SubmatcherMethodInterceptor.setInvokedMethod(null);
		
		interceptor = new SubmatcherMethodInterceptor();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void initializerSetsInvokedMethod()
	{
		assertThat(SubmatcherMethodInterceptor.getInvokedMethod(), is(nullValue()));
	}
	
	@Test
	public void interceptSetsInvokedMethod() throws NoSuchMethodException
	{
		Method method = getMethod();
		
		interceptor.intercept(null, method, null, null);
		
		assertThat(SubmatcherMethodInterceptor.getInvokedMethod(), is(method));
	}
	
	@Test
	public void setInvokedMethodSetsProperty() throws NoSuchMethodException
	{
		Method method = getMethod();
		
		SubmatcherMethodInterceptor.setInvokedMethod(method);
		
		assertThat(SubmatcherMethodInterceptor.getInvokedMethod(), is(method));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static Method getMethod() throws NoSuchMethodException
	{
		return Person.class.getMethod("getName");
	}
}
