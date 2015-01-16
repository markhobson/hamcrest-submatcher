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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests {@code SpyHolder}.
 */
public class SpyHolderTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private SpyHolderRule spyHolderRule = new SpyHolderRule();
	
	private ExpectedException thrown = ExpectedException.none();

	// ----------------------------------------------------------------------------------------------------------------
	// JUnit methods
	// ----------------------------------------------------------------------------------------------------------------

	@Rule
	public SpyHolderRule getSpyHolderRule()
	{
		return spyHolderRule;
	}

	@Rule
	public ExpectedException getThrown()
	{
		return thrown;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test
	public void hasSpyWhenSetReturnsTrue()
	{
		SpyHolder.setSpy(mock(Spy.class));
		
		boolean actual = SpyHolder.hasSpy();
		
		assertThat(actual, is(true));
	}
	
	@Test
	public void hasSpyWhenUnsetReturnsFalse()
	{
		SpyHolder.setSpy(null);
		
		boolean actual = SpyHolder.hasSpy();
		
		assertThat(actual, is(false));
	}
	
	@Test
	public void getSpyWhenUnsetThrowsException()
	{
		thrown.expect(IllegalStateException.class);
		thrown.expectMessage("Spy has not been set");
		
		SpyHolder.getSpy();
	}

	@Test
	public void setSpySetsProperty()
	{
		Spy<Object> spy = mock(Spy.class);
		
		SpyHolder.setSpy(spy);
		
		assertThat(SpyHolder.getSpy(), CoreMatchers.<Object>is(spy));
	}
}
