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

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hobsoft.hamcrest.submatcher.test.Name;
import org.hobsoft.hamcrest.submatcher.test.Person;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hobsoft.hamcrest.submatcher.Submatcher.hasValue;
import static org.hobsoft.hamcrest.submatcher.Submatcher.that;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests {@code Submatcher} using the {@code Matcher} API.
 */
public class SubmatcherIntegrationTest
{
	// ----------------------------------------------------------------------------------------------------------------
	// fields
	// ----------------------------------------------------------------------------------------------------------------

	private SpyHolderRule spyHolderRule = new SpyHolderRule();
	
	// ----------------------------------------------------------------------------------------------------------------
	// JUnit methods
	// ----------------------------------------------------------------------------------------------------------------

	@Rule
	public SpyHolderRule getSpyHolderRule()
	{
		return spyHolderRule;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// tests
	// ----------------------------------------------------------------------------------------------------------------
	
	@Test
	public void describeToAppendsDescription()
	{
		Matcher<Person> matcher = hasValue(that(Person.class).getName(), is(newName("x")));
		StringDescription description = new StringDescription();
		
		matcher.describeTo(description);
		
		assertThat(description.toString(), is("has value that getName() is <x>"));
	}
	
	@Test
	public void describeToWithArgumentAppendsDescription()
	{
		Matcher<Person> matcher = hasValue(that(Person.class).getNameWithArgument("x"), is(newName("y")));
		StringDescription description = new StringDescription();
		
		matcher.describeTo(description);
		
		assertThat(description.toString(), is("has value that getNameWithArgument(\"x\") is <y>"));
	}
	
	@Test
	public void describeToWithArgumentsAppendsDescription()
	{
		Matcher<Person> matcher = hasValue(that(Person.class).getNameWithArguments("x", "y"), is(newName("z")));
		StringDescription description = new StringDescription();
		
		matcher.describeTo(description);
		
		assertThat(description.toString(), is("has value that getNameWithArguments(\"x\", \"y\") is <z>"));
	}
	
	@Test
	public void matchesWhenMatchesReturnsTrue()
	{
		Name name = mock(Name.class);
		
		Matcher<Person> actual = hasValue(that(Person.class).getName(), is(name));
		
		assertThat(actual.matches(newPersonWithName(name)), is(true));
	}
	
	@Test
	public void matchesWhenDoesNotMatchReturnsFalse()
	{
		Matcher<Person> actual = hasValue(that(Person.class).getName(), is(mock(Name.class)));
		
		assertThat(actual.matches(newPersonWithName(mock(Name.class))), is(false));
	}

	@Test
	public void matchesWithArgumentWhenMatchesReturnsTrue()
	{
		Name name = mock(Name.class);
		
		Matcher<Person> actual = hasValue(that(Person.class).getNameWithArgument("x"), is(name));
		
		assertThat(actual.matches(newPersonWithName(name)), is(true));
	}
	
	@Test
	public void matchesWithArgumentWhenDoesNotMatchReturnsFalse()
	{
		Matcher<Person> actual = hasValue(that(Person.class).getNameWithArgument("x"), is(mock(Name.class)));
		
		assertThat(actual.matches(newPersonWithName(mock(Name.class))), is(false));
	}
	
	@Test
	public void matchesWithArgumentsWhenMatchesReturnsTrue()
	{
		Name name = mock(Name.class);
		
		Matcher<Person> actual = hasValue(that(Person.class).getNameWithArguments("x", "y"), is(name));
		
		assertThat(actual.matches(newPersonWithName(name)), is(true));
	}
	
	@Test
	public void matchesWithArgumentsWhenDoesNotMatchReturnsFalse()
	{
		Matcher<Person> actual = hasValue(that(Person.class).getNameWithArguments("x", "y"), is(mock(Name.class)));
		
		assertThat(actual.matches(newPersonWithName(mock(Name.class))), is(false));
	}
	
	@Test
	public void matchesWhenPrimitiveAndMatchesReturnsTrue()
	{
		Matcher<Person> actual = hasValue(that(Person.class).getAge(), is(1));
		
		assertThat(actual.matches(newPersonWithAge(1)), is(true));
	}
	
	@Test
	public void matchesWhenPrimitiveAndDoesNotMatchReturnsFalse()
	{
		Matcher<Person> actual = hasValue(that(Person.class).getAge(), is(1));
		
		assertThat(actual.matches(newPersonWithAge(2)), is(false));
	}
	
	@Test
	public void describeMismatchAppendsMismatch()
	{
		Matcher<Person> matcher = hasValue(that(Person.class).getName(), is(mock(Name.class)));
		StringDescription mismatchDescription = new StringDescription();

		matcher.describeMismatch(newPersonWithName(newName("x")), mismatchDescription);
		
		assertThat(mismatchDescription.toString(), is("was <x>"));
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------------------------------------------------

	private static Person newPersonWithName(Name name)
	{
		Person person = mock(Person.class);
		when(person.getName()).thenReturn(name);
		when(person.getNameWithArgument(anyString())).thenReturn(name);
		when(person.getNameWithArguments(anyString(), anyString())).thenReturn(name);
		return person;
	}

	private static Person newPersonWithAge(int age)
	{
		Person person = mock(Person.class);
		when(person.getAge()).thenReturn(age);
		return person;
	}
	
	private static Name newName(String string)
	{
		Name name = mock(Name.class);
		when(name.toString()).thenReturn(string);
		return name;
	}
}
