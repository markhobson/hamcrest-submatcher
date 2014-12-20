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

import org.hamcrest.CoreMatchers;
import org.hobsoft.hamcrest.submatcher.test.Name;
import org.hobsoft.hamcrest.submatcher.test.Person;
import org.junit.Test;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests {@code Spy}.
 */
public class SpyTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------

	@Test
	public void createReturnsInstance()
	{
		Person actual = new Spy<Person>(Person.class, null).create();
		
		assertThat(actual, is(instanceOf(Person.class)));
	}

	@Test
	// SUPPRESS CHECKSTYLE IllegalThrows
	public void createReturnsInstanceWithInterceptor() throws Throwable
	{
		MethodInterceptor interceptor = mock(MethodInterceptor.class);
		Name name = new Name("x");
		
		new Spy<Person>(Person.class, interceptor).create().setName(name);
		
		verify(interceptor).intercept(
			argThat(is(instanceOf(Person.class))),
			argThat(is(Person.class.getMethod("setName", Name.class))),
			argThat(is(new Object[] {name})),
			argThat(is(CoreMatchers.<MethodProxy>instanceOf(MethodProxy.class)))
		);
	}
}
