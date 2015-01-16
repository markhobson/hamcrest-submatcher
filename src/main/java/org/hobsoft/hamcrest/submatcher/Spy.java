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

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Factory for proxies that record their method invocations.
 */
class Spy<T> implements MethodInterceptor
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private final Class<T> type;
	
	private Method invokedMethod;

	// ----------------------------------------------------------------------------------------------------------------
	// constructors
	// ----------------------------------------------------------------------------------------------------------------

	public Spy(Class<T> type)
	{
		if (type == null)
		{
			throw new NullPointerException("type");
		}
		
		this.type = type;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// MethodInterceptor methods
	// ----------------------------------------------------------------------------------------------------------------

	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
	{
		invokedMethod = method;
		
		return null;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// public methods
	// ----------------------------------------------------------------------------------------------------------------

	public T create()
	{
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(type);
		enhancer.setCallbackType(MethodInterceptor.class);
		Class<?> proxyType = enhancer.createClass();
		
		Objenesis objenesis = new ObjenesisStd();
		Factory proxy = (Factory) objenesis.newInstance(proxyType);
		proxy.setCallbacks(new Callback[] {this});
		
		return type.cast(proxy);
	}
	
	public Method getInvokedMethod()
	{
		return invokedMethod;
	}
}
