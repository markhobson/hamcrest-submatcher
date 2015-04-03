hamcrest-submatcher
===================

Type-safe Hamcrest matcher for sub-properties.

Usage
-----

Single sub-property:

	assertThat(actual, hasValue(that(Person.class).getName(), is("x")));
	
Multiple sub-properties:

	assertThat(actual, allOf(
		hasValue(that(Person.class).getName(), is("x")),
		hasValue(that(Person.class).getAge(), is(1))
	));

Nested sub-properties:

	assertThat(actual, hasValue(that(Person.class).getAddress(),
		hasValue(that(Address.class).getCity(), is("x"))
	));

License
-------

* [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

[![Build Status](https://travis-ci.org/markhobson/hamcrest-submatcher.svg?branch=master)](https://travis-ci.org/markhobson/hamcrest-submatcher)
