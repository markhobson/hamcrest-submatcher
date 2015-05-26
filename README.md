Hamcrest Submatcher
===================

Type-safe Hamcrest matcher for method values.

Usage
-----

Single method value:

	assertThat(actual, hasValue(on(Person.class).getName(), equalTo("x")));
	
Multiple method values:

	assertThat(actual, allOf(
		hasValue(on(Person.class).getName(), equalTo("x")),
		hasValue(on(Person.class).getAge(), equalTo(1))
	));

Nested method value:

	assertThat(actual, hasValue(on(Person.class).getAddress(),
		hasValue(on(Address.class).getCity(), equalTo("x"))
	));

License
-------

* [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

[![Build Status](https://travis-ci.org/markhobson/hamcrest-submatcher.svg?branch=master)](https://travis-ci.org/markhobson/hamcrest-submatcher)
