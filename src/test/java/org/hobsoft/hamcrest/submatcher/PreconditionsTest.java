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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code Preconditions}.
 */
public class PreconditionsTest
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
	public void checkNullWithNonNullDoesNotThrowException()
	{
		Preconditions.checkNotNull(new Object(), "x");
	}
	
	@Test
	public void checkNullWithNonNullReturnsInstance()
	{
		Object instance = new Object();
		
		Object actual = Preconditions.checkNotNull(instance, "x");
		
		assertThat(actual, is(instance));
	}
	
	@Test
	public void checkNullWithNullThrowsException()
	{
		thrown.expect(NullPointerException.class);
		thrown.expectMessage("x");
		
		Preconditions.checkNotNull(null, "x");
	}
}
