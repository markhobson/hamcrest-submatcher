hamcrest-submatcher
===================

Type-safe Hamcrest matcher for sub-properties.

Usage
-----

Single sub-property:

	assertThat(actual, such(that(Person.class).getName(), is("x")));
	
Multiple sub-properties:

	assertThat(actual, both(such(that(Person.class).getName(), is("x")))
		.and(such(that(Person.class).getAge(), is(1)))
	);

Nested sub-properties:

	assertThat(actual, such(that(Person.class).getAddress(),
		such(that(Address.class).getCity(), is("x"))
	));
