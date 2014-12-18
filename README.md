hamcrest-submatcher
===================

Type-safe Hamcrest matcher for sub-properties.

Usage
-----

Single sub-property:

	assertThat(actual, is(such(that(Person.class).getName(), is("x"))));
	
Multiple sub-properties:

	assertThat(actual, allOf(
		is(such(that(Person.class).getName(), is("x"))),
		is(such(that(Person.class).getAge(), is(1)))
	));

Nested sub-properties:

	assertThat(actual, is(such(that(Person.class).getAddress(),
		is(such(that(Address.class).getCity(), is("x")))
	)));

Note that the use of the outer `is()` is syntactic sugar and can be omitted.
