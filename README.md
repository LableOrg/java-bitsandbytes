Bits and Bytes
==============

A modest collection of utility classes that make some bit and byte-wise
operations in Java a little bit more readable.

## Purpose

We use this tiny library in projects where bit and byte manipulation is
performed. In particular, we use it to supplement Hadoop's `Bytes` class,
which is usually available in our projects.

## Example usage

In unit tests the `Binary` class can aid in making the testing of code that
performs bit-wise operations more readable.

For example:

```java
// Instead of:
assertThat(someBitManipulatingMethodUnderTest(0),
           is(new byte[] {-1, -86, 15, 0})));

// Write:
assertThat(someBitManipulatingMethodUnderTest(0),
           is(Binary.decode("10000000 10101010 00001111 00000000")));
```
