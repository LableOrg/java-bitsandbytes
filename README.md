Bits and Bytes
==============

A modest collection of utility classes that make some bit and byte-wise
operations in Java a little bit more readable.

## Purpose

This tiny library can be used in projects where bit and byte manipulation is
performed to increase the readability of the code. In particular, it was
designed to supplement (and sometimes replace) Hadoop's `Bytes` class, which is
very useful, but part of a much larger library.

This tiny library has no dependencies, which makes it useful in (for example)
Hadoop's MapReduce tasks.

## Installation

This library is available on Maven Central:

```
<dependency>
    <groupId>org.lable.oss.bitsandbytes</groupId>
    <artifactId>bitsandbytes</artifactId>
    <version>2.2</version>
</dependency>
```

## Example usage

### Binary

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

### ByteMangler

The `ByteMangler` class contains a number of methods that aim to make working
with byte arrays a little more pleasant:

```java
// Concatenate any number of byte arrays in a single call.
byte[] result = ByteMangler.add(part, anotherPart, andAnotherPart, yetAnotherPart);
```

### BitMask

HBase's
[FuzzyRowFilter](https://hbase.apache.org/apidocs/org/apache/hadoop/hbase/filter/FuzzyRowFilter.html)
expects a bitmask in the form of a byte array containing `0x00` or `0x01` for
each byte as one of its inputs. `BitMask` has a method that can compose this
byte array mask by specifying the consecutive number of ones and zeroes you
need:
 
```java
// 0011101111:
byte[] mask = BitMask.bitMask(2, 3, 1, 4);
 
// To start the mask with ones, pass 0 as the first argument.
// 11111111:
byte[] anotherMask = BitMask.bitMask(0, 8);
```

### ByteConversion

Convert Java primitives to and from byte arrays.

```java
// Strings are encoded as UTF-8; 0xE2 0x82 0xAC:
byte[] result = ByteConversion.fromString("€");
String euro = ByteConversion.toString(result);
```

Primitives such as ints are converted to their conventional Java byte
representation. This usually means [two's
complement](https://en.wikipedia.org/wiki/Two's_complement).

```java
// 0x7F 0xFF 0xFF 0xFF:
byte[] result = ByteConversion.fromInt(Integer.MAX_VALUE);
```

In cases where you want the byte array of an int or long to be naturally
sortable (which is often what you want when they are used as part of a HBase
row-key), *two's complement* causes negative numbers to be sorted after
positive numbers.

Use `ByteMangler` to flip the first bit when you read and write the byte
representation of an `int` or `long` to prevent this:

```java
// 0x7F 0xFF 0xFF 0xFF:
byte[] resultMinusOne = ByteMangler.flipTheFirstBit(ByteConversion.fromInt(-1));
int minusOne = ByteConversion.toInt(ByteMangler.flipTheFirstBit(resultMinusOne));
// 0x80 0x00 0x00 0x00:
byte[] resultZero = ByteMangler.flipTheFirstBit(ByteConversion.fromInt(0));
int zero = ByteConversion.toInt(ByteMangler.flipTheFirstBit(resultZero));
// 0x80 0x00 0x00 0x01:
byte[] resultOne = ByteMangler.flipTheFirstBit(ByteConversion.fromInt(1));
int one = ByteConversion.toInt(ByteMangler.flipTheFirstBit(resultOne));
```

Now `-1` sorts right before `0`.

Or, specify the `LEXICOGRAPHIC_SORT` *NumberRepresentation* parameter for the same effect:

```java
// 0x7F 0xFF 0xFF 0xFF:
byte[] resultMinusOne = ByteConversion.fromInt(-1, NumberRepresentation.LEXICOGRAPHIC_SORT);
int minusOne = ByteConversion.toInt(resultMinusOne, NumberRepresentation.LEXICOGRAPHIC_SORT);
```
