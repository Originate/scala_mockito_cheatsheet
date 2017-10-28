# Scala Mockito Cheatsheet

This repository is intended to help explain some of the caveats you will run into when
attempting to use [Mockito](https://github.com/mockito/mockito) in a Scala project. It
also contains a few helpful patterns for more extended uses of Mockito.

The examples are all written as tests: [ScalaMockitoSpec](src/test/scala/ScalaMockitoSpec.scala)

## Topics

The examples cover the following scenarios

* Implicits and how they interact with matchers
* Custom matchers and utilizing `match` expressions
* Stubbing methods with a function that depends on matched values

## Notes

### Implicits

A common caveat you will run into when using Mockito with Scala is that when you stub a method,
if you pass in matchers for the arguments (versus using exact values), then all of the arguments
need to be matchers. This gets hairy when you have a method with implicits, because Scala will
automatically plug in an exact value, implicitly, and Mockito will complain because it wants a
matcher. See the examples for clarification on how to deal with this.
