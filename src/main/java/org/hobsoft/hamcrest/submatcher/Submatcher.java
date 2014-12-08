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
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Matcher for a sub-property of an instance.
 * 
 * @param <T>
 *            the instance type
 */
public class Submatcher<T> extends TypeSafeMatcher<T>
{
	// ----------------------------------------------------------------------------------------------------------------
	// types
	// ----------------------------------------------------------------------------------------------------------------
	
	/**
	 * Provides details about the invoked proxy method.
	 */
	public interface InvocationInfo
	{
		// ------------------------------------------------------------------------------------------------------------
		// public methods
		// ------------------------------------------------------------------------------------------------------------

		Method getInvokedMethod();
	}

	private static class SubmatcherMethodInterceptor implements MethodInterceptor, InvocationInfo
	{
		// ------------------------------------------------------------------------------------------------------------
		// fields
		// ------------------------------------------------------------------------------------------------------------

		private Method invokedMethod;

		// ------------------------------------------------------------------------------------------------------------
		// MethodInterceptor methods
		// ------------------------------------------------------------------------------------------------------------

		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
			throws NoSuchMethodException
		{
			if (InvocationInfo.class.getMethod("getInvokedMethod").equals(method))
			{
				return getInvokedMethod();
			}
			
			invokedMethod = method;
			
			return proxy(method.getReturnType(), this);
		}
		
		// ------------------------------------------------------------------------------------------------------------
		// InvocationInfo method
		// ------------------------------------------------------------------------------------------------------------

		public Method getInvokedMethod()
		{
			return invokedMethod;
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final InvocationInfo invocationInfo;
	
	private final Matcher<?> matcher;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public Submatcher(InvocationInfo invocationInfo, Matcher<?> matcher)
	{
		this.invocationInfo = invocationInfo;
		this.matcher = matcher;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// TypeSafeMatcher methods
	// ----------------------------------------------------------------------------------------------------------------

	@Override
	protected boolean matchesSafely(T actual)
	{
		Method invokedMethod = invocationInfo.getInvokedMethod();
		
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
		InvocationInfo invocationInfo = (InvocationInfo) actual;
		
		return new Submatcher<T>(invocationInfo, matcher);
	}
	
	public static <U> U that(Class<U> type)
	{
		return proxy(type, new SubmatcherMethodInterceptor());
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static <U> U proxy(Class<U> type, MethodInterceptor interceptor)
	{
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(type);
		enhancer.setInterfaces(new Class<?>[] {InvocationInfo.class});
		enhancer.setCallbackType(MethodInterceptor.class);
		Class<?> proxyType = enhancer.createClass();
		
		Objenesis objenesis = new ObjenesisStd();
		Factory proxy = (Factory) objenesis.newInstance(proxyType);
		proxy.setCallbacks(new Callback[] {interceptor});
		
		return type.cast(proxy);
	}
}
