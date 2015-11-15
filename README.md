# Scala Mockito Cheatsheet

This repository is intended to help explain some of the caveats you will run into when
attempting to use [Mockito](https://github.com/mockito/mockito) in a Scala project. It
also contains a few helpful patterns for more extended uses of Mockito.

The examples are all written as tests:
[ScalaMockitoSpec](https://github.com/Originate/scala_mockito_cheatsheet/blob/master/src/test/scala/ScalaMockitoSpec.scala)

## Topics

The examples cover the following scenarios

* Implicits and how they interact with matchers
* Custom matchers and utilizing `match` expressions
* Stubbing methods with a function that depends on matched values

## Notes

### Building this project

This repository uses a macro to test that a specific line of Scala will not typecheck. The
implementation was copied from [Slick](https://github.com/slick/slick/blob/master/slick-testkit/src/main/scala/com/typesafe/slick/testkit/util/ShouldNotTypecheck.scala).

Unfortunately, Scala will not allow you to compile a macro and a file that uses it, in the same pass.
To solve this, I could convert this into a multi-project SBT build, but I feel like that
is overkill. As a hack, If you want to try and run these tests locally, you'll have to comment out
the test that uses `ShouldNotTypecheck`. It turns out that once your macro has compiled, you can then
use it in the rest of your code (so you could uncomment afterwards to see it in action).

### Implicits

A common caveat you will run into when using Mockito with Scala is that when you stub a method,
if you pass in matchers for the arguments (versus using exact values), then all of the arguments
need to be matchers. This gets hairy when you have a method with implicits, because Scala will
automatically plug in an explicit value, implicitly, and Mockito will complain because it wants a
matcher. See the examples for clarification on how to deal with this.

