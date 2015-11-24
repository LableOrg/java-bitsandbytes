Bits and Bytes
==============

A modest collection of utility classes that make some bit and byte-wise
operations in Java a little bit more readable.

## Purpose

We use this tiny library in projects where bit and byte manipulation is
performed. In particular, we use it to supplement Hadoop's `Bytes` class,
which is usually available in our projects.

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

The `ByteMangler` class contains a number of methods that aim to make working with byte arrays a little more pleasant:

```java
// Concatenate any number of byte arrays in a single call.
byte[] result = ByteMangler.add(part, anotherPart, andAnotherPart, yetAnotherPart);
```

### BitMask

HBase's [FuzzyRowFilter](https://hbase.apache.org/apidocs/org/apache/hadoop/hbase/filter/FuzzyRowFilter.html) expects
a bitmask in the form of a byte array containing `0x00` or `0x01` for each byte as one of its inputs. `BitMask` has 
a method that can compose this byte array mask by specifying the consecutive number of ones and zeroes you need:
 
```java
// 0011101111:
byte[] mask = BitMask.bitMask(2, 3, 1, 4);
 
// To start the mask with ones, pass 0 as the first argument.
// 11111111:
byte[] anotherMask = BitMask.bitMask(0, 8);
```