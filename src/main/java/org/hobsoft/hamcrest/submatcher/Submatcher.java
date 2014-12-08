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
		// no properties
	}

	private static class SubmatcherMethodInterceptor implements MethodInterceptor
	{
		// ------------------------------------------------------------------------------------------------------------
		// MethodInterceptor methods
		// ------------------------------------------------------------------------------------------------------------

		public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
		{
			return proxy(method.getReturnType(), this);
		}
	}

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
