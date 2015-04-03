hamcrest-submatcher
===================

Type-safe Hamcrest matcher for sub-properties.

Usage
-----

Single sub-property:

	assertThat(actual, hasValue(on(Person.class).getName(), equalTo("x")));
	
Multiple sub-properties:

	assertThat(actual, allOf(
		hasValue(on(Person.class).getName(), equalTo("x")),
		hasValue(on(Person.class).getAge(), equalTo(1))
	));

Nested sub-properties:

	assertThat(actual, hasValue(on(Person.class).getAddress(),
		hasValue(on(Address.class).getCity(), equalTo("x"))
	));

License
-------

* [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

[![Build Status](https://travis-ci.org/markhobson/hamcrest-submatcher.svg?branch=master)](https://travis-ci.org/markhobson/hamcrest-submatcher)
